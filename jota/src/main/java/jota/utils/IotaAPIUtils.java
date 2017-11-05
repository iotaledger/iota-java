package jota.utils;

import jota.error.InvalidAddressException;
import jota.model.Bundle;
import jota.model.Input;
import jota.model.Transaction;
import jota.pow.ICurl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Client Side computation service.
 *
 * @author davassi
 */
public class IotaAPIUtils {

    /**
     * Generates a new address
     *
     * @param seed     The tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security The secuirty level of private key / seed.
     * @param index    The index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @param checksum The adds 9-tryte address checksum
     * @param curl     The curl instance.
     * @return An String with address.
     * @throws InvalidAddressException is thrown when the specified address is not an valid address.
     */
    public static String newAddress(String seed, int security, int index, boolean checksum, ICurl curl) throws InvalidAddressException {
        Signing signing = new Signing(curl);
        final int[] key = signing.key(Converter.trits(seed), index, security);
        final int[] digests = signing.digests(key);
        final int[] addressTrits = signing.address(digests);

        String address = Converter.trytes(addressTrits);

        if (checksum) {
            address = Checksum.addChecksum(address);
        }
        return address;
    }

    public static List<String> signInputsAndReturn(final String oseed,
                                                   final List<Input> inputs,
                                                   final Bundle bundle,
                                                   final List<String> signatureFragments, ICurl curl) {

        bundle.finalize(curl);
        bundle.addTrytes(signatureFragments);

        //  SIGNING OF INPUTS
        //
        //  Here we do the actual signing of the inputs
        //  Iterate over all bundle transactions, find the inputs
        //  Get the corresponding private key and calculate the signatureFragment
        for (int i = 0; i < bundle.getTransactions().size(); i++) {
            if (bundle.getTransactions().get(i).getValue() < 0) {
                String thisAddress = bundle.getTransactions().get(i).getAddress();

                // Get the corresponding keyIndex of the address
                int keyIndex = 0;
                int keySecurity = 0;
                String signatureSeed = oseed;
                for (Input input : inputs) {
                    if (input.getAddress().equals(thisAddress)) {
                        keyIndex = input.getKeyIndex();
                        keySecurity = input.getSecurity();
                        if (StringUtils.isNotBlank(input.getSeed()))
                        	signatureSeed=input.getSeed();
                    }
                }

                String bundleHash = bundle.getTransactions().get(i).getBundle();

                 // Get corresponding private key of address
                int[] key = new Signing(curl).key(Converter.trits(signatureSeed), keyIndex, keySecurity);
 
                //  Get the normalized bundle hash
                int[] normalizedBundleHash = bundle.normalizedBundle(bundleHash);

                // for each security level, add a signature
                for (int j = 0; j < keySecurity; j++) {
                	
                	int hi = j % 3;

                    if (bundle.getTransactions().get(i + j).getAddress().equals(thisAddress)) {
                        // Use current 6562 trits
                        int[] keyFragment = Arrays.copyOfRange(key, 6561 * j, 6561 * (j + 1));

                        // The current trytes of the bundle hash
                        int[] bundleFragment = Arrays.copyOfRange(normalizedBundleHash, 27 * hi, 27 * (hi + 1));

                        //  Calculate the new signature
                        int[] signedFragment = new Signing(curl).signatureFragment(bundleFragment, keyFragment);

                        //  Convert signature to trytes and assign it again to this bundle entry
                        bundle.getTransactions().get(i+j).setSignatureFragments(Converter.trytes(signedFragment));
                    }
                }
            }
        }

        List<String> bundleTrytes = new ArrayList<>();

        // Convert all bundle entries into trytes
        for (Transaction tx : bundle.getTransactions()) {
            bundleTrytes.add(tx.toTrytes());
        }
        Collections.reverse(bundleTrytes);
        return bundleTrytes;
    }
}

