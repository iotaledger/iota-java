package org.iota.jota.account.deposits.methods;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.iota.jota.account.deposits.ConditionalDepositAddress;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.errors.MagnetError;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.JCurl;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.types.Hash;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.InputValidator;

public class MagnetMethod implements DepositMethod<String> {
    
    private ICurl curl;
    
    private static final String SCHEME = "iota";
    
    public static final String CONDITION_EXPIRES = "timeout_at";
    public static final String CONDITION_MULTI_USE = "multi_use";
    public static final String CONDITION_AMOUNT = "expected_amount";
    
    private static final String magnetUrl = SCHEME + "://%s/?" 
            + CONDITION_EXPIRES + "=%d&"
            + CONDITION_MULTI_USE + "=%b&"
            + CONDITION_AMOUNT + "=%d";

    public MagnetMethod() {
        this.curl = SpongeFactory.create(SpongeFactory.Mode.CURL_P81);
    }
    
    public void setCurl(ICurl curl) {
        this.curl = curl;
    }

    @Override
    public ConditionalDepositAddress parse(String method) throws MagnetError {
        try {
            return parse(new URI(method));
        } catch (URISyntaxException e) {
            throw new MagnetError(e);
        }
    }

    private ConditionalDepositAddress parse(URI uri) throws MagnetError {
        if (!SCHEME.equals(uri.getScheme())) {
            throw new MagnetError("Invalid scheme: " + uri.getScheme());
        }

        String address = uri.toString().substring(
                SCHEME.length() + 3, SCHEME.length() + 3 + Constants.ADDRESS_LENGTH_WITH_CHECKSUM);
        
        if (!InputValidator.checkAddress(address)) {
            throw new MagnetError("Invalid Address: " + address);
        }
        
        Map<String, List<String>> paramsMap = collectParams(uri);

        long timeOut = parseLong(getParam(CONDITION_EXPIRES, paramsMap, "0"));
        boolean multiUse = parseBoolean(getParam(CONDITION_MULTI_USE, paramsMap, "false"));
        long expectedAmount = parseLong(getParam(CONDITION_AMOUNT, paramsMap, "0"));
        
        if (!magnetChecksum(address.substring(0, 81), timeOut, multiUse, expectedAmount)
                .equals(address.substring(81, 90))) {
            throw new MagnetError("Magnet checksum does not match fields");
        }
        
        DepositRequest request = new DepositRequest(new Date(timeOut), multiUse, expectedAmount);
        ConditionalDepositAddress conditions = new ConditionalDepositAddress(
                request, 
                new Hash(address.substring(0, 81)));
        
        return conditions;
    }

    private boolean parseBoolean(String param) {
        return BooleanUtils.toBoolean(param) || param.equals("1");
    }

    private long parseLong(String param) {
        try {
            return Long.parseLong(param);
        } catch ( NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + param);
        }
    }

    private Map<String, List<String>> collectParams(URI uri) throws MagnetError {
        Map<String, List<String>> paramsMap = new HashMap<>();

        // iota://address/, so getSchemeSpecificPart removes iota:
        String[] params = uri.getSchemeSpecificPart()
                .substring(Constants.ADDRESS_LENGTH_WITH_CHECKSUM + 4)
                .split("&");
        for (String param : params) {
            try {
                String[] parts = param.split("=");
                String name = parts[0];
                String value = parts[1];
                List<String> values = paramsMap.get(name);
                if (values == null) {
                    values = new ArrayList<>();
                    paramsMap.put(name, values);
                }
                values.add(value);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new MagnetError("Magnet field param is missing a value");
            }
        }

        return paramsMap;
    }
    
    private String getParam(String condition, Map<String, List<String>> paramsMap, String defaultValue) {
        List<String> map = paramsMap.getOrDefault(condition, Collections.emptyList());
        if (map.size() > 1) {
            throw new IllegalArgumentException("Only one value is allowed for: " + condition);
        } else if (map.size() == 0) {
            return defaultValue;
        }
        
        return map.get(0);
    }

    @Override
    public String build(ConditionalDepositAddress conditions) {
        String address = conditions.getDepositAddress().getHash();
        String magnetChecksum = magnetChecksum(address,
                conditions.getRequest().getTimeOut().getTime(),
                conditions.getRequest().isMultiUse(),
                conditions.getRequest().getExpectedAmount());
        
        return String.format(magnetUrl, 
                address + magnetChecksum,
                conditions.getRequest().getTimeOut().getTime(),
                conditions.getRequest().isMultiUse(),
                conditions.getRequest().getExpectedAmount());
    }

    //Package private for testing
    String magnetChecksum(String address, long timeout, boolean multiUse, long amount) {
        //Get checksum trits for address
        int[] addressTrits = calculateChecksum(Converter.trits(address));
        
        //Get trits non-bool for fields
        int[] timeoutTrits = Converter.trits(timeout);
        int[] amountTrits = Converter.trits(amount);
        
        //trit input for checksum of magnet
        int[] totalTrits = new int[Constants.HASH_LENGTH_TRITS];

        //timeout (27) + multi_use (1) + amount (81) = 109
        int addressRest = Constants.HASH_LENGTH_TRITS - 109;
        //Copy part of address checksum into magnet checksum trits
        System.arraycopy(addressTrits, 0, totalTrits, 0, addressRest);
        
        //Add fields to trits input
        System.arraycopy(timeoutTrits, 0, totalTrits, addressRest, timeoutTrits.length);
        totalTrits[addressRest + 27] = multiUse ? 1 : 0;
        System.arraycopy(amountTrits, 0, totalTrits, addressRest + 27 + 1, amountTrits.length);

        //Make checksum trits
        int[] checksumTrits = calculateChecksum(totalTrits);
        String checksum = Converter.trytes(checksumTrits);
        
        //Return only the "checksum", last 9 trytes
        return checksum.substring(72, 81);
    }
    
    /**
     * Takes input trits and makes hash length output trits
     * @param trits
     * @return
     */
    private int[] calculateChecksum(int[] trits) {
        curl.reset();
        curl.absorb(trits);
        int[] checksumTrits = new int[JCurl.HASH_LENGTH];
        curl.squeeze(checksumTrits);
        return checksumTrits;
    }
}
