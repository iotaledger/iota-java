package org.iota.jota.account.deposits.methods;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iota.jota.account.deposits.ConditionalDepositAddress;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.types.Hash;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;

public class MagnetMethod implements DepositMethod<String> {
    
    private static final String SCHEME = "iota";
    
    private static final String CONDITION_EXPIRES = "t";
    private static final String CONDITION_MULTI_USE = "m";
    private static final String CONDITION_AMOUNT = "am";
    
    private static final String magnetUrl = SCHEME + "://%s/?" 
            + CONDITION_EXPIRES + "=%d&"
            + CONDITION_MULTI_USE + "=%b&"
            + CONDITION_AMOUNT + "=%d";

    public MagnetMethod() {
        
    }

    @Override
    public ConditionalDepositAddress parse(String method) {
        try {
            return parse(new URI(method));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ConditionalDepositAddress parse(URI uri) {
        if (!SCHEME.equals(uri.getScheme())) {
            throw new IllegalArgumentException("Invalid scheme: " + uri.getScheme());
        }

        String address = uri.toString().substring(SCHEME.length() + 3, SCHEME.length() + 3 + Constants.ADDRESS_LENGTH_WITH_CHECKSUM);
        if (!InputValidator.checkAddress(address)) {
            throw new IllegalArgumentException("Invalid Address: " + address);
        }
        
        Map<String, List<String>> paramsMap = collectParams(uri);

        long timeOut = parseLong(getParam(CONDITION_EXPIRES, paramsMap));
        boolean multiUse = Boolean.getBoolean(getParam(CONDITION_MULTI_USE, paramsMap));
        long expectedAmount = parseLong(getParam(CONDITION_AMOUNT, paramsMap));
        
        DepositRequest request = new DepositRequest(new Date(timeOut), multiUse, expectedAmount);
        ConditionalDepositAddress conditions = new ConditionalDepositAddress(request, new Hash(address));
        
        return conditions;
    }

    private long parseLong(String param) {
        try {
            return Long.parseLong(param);
        } catch ( NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + param);
        }
    }

    private Map<String, List<String>> collectParams(URI uri) {
        Map<String, List<String>> paramsMap = new HashMap<>();

        // iota://address/, so getSchemeSpecificPart removes iota:
        String[] params = uri.getSchemeSpecificPart()
                .substring(Constants.ADDRESS_LENGTH_WITH_CHECKSUM + 4)
                .split("&");
        for (String param : params) {
            String[] parts = param.split("=");
            String name = parts[0];
            String value = parts[1];
            List<String> values = paramsMap.get(name);
            if (values == null) {
                values = new ArrayList<>();
                paramsMap.put(name, values);
            }
            values.add(value);
        }

        return paramsMap;
    }
    
    private String getParam(String condition, Map<String, List<String>> paramsMap) {
        List<String> map = paramsMap.getOrDefault(condition, Collections.emptyList());
        if (map.size() != 1) {
            throw new IllegalArgumentException("Only one value is allowed for: " + condition);
        }
        
        return map.get(0);
    }

    @Override
    public String build(ConditionalDepositAddress conditions) {
        return String.format(magnetUrl, 
                conditions.getDepositAddress().getWithChecksum(),
                conditions.getRequest().getTimeOut().getTime(),
                conditions.getRequest().isMultiUse(),
                conditions.getRequest().getExpectedAmount());
    }

}
