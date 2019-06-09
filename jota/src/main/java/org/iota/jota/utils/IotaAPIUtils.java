package org.iota.jota.utils;

import org.iota.jota.builder.AddressRequest;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Input;
import org.iota.jota.model.Transaction;
import org.iota.jota.pow.ICurl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.iota.jota.utils.Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR;

/**
 * Client Side computation service.
 *
 */
public class IotaAPIUtils {

    /**
     * Generates a new address
     *
     * @param seed     The tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security The security level of private key / seed.
     * @param index    The index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @param checksum The adds 9-tryte address checksum
     * @param curl     The curl instance.
     * @return An String with address.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public static String newAddress(String seed, int security, int index, boolean checksum, ICurl curl) throws ArgumentException {

        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

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

    /**
     * Generates a new address
     *
     * @param addressRequest {@link AddressRequest}
     * @param index    The index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @param curl     The curl instance.
     * @return An String with address.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public static String newAddress(AddressRequest addressRequest, int index, ICurl curl) throws ArgumentException {
        return newAddress(addressRequest.getSeed(), addressRequest.getSecurityLevel(), index,
                addressRequest.isChecksum(), curl);
    }

    /**
     * Finalizes and signs the bundle transactions.
     * Bundle and inputs are assumed correct.
     * 
     * @param seed The tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param inputs
     * @param bundle The bundle
     * @param signatureFragments
     * @param curl The curl instance.
     * @return list of transaction trytes in the bundle
     * @throws ArgumentException When the seed is invalid
     */
    public static List<String> signInputsAndReturn(String seed, List<Input> inputs, Bundle bundle,
                                                                List<String> signatureFragments, 
                                                                ICurl curl) throws ArgumentException {

        if ((!InputValidator.isValidSeed(seed))) {
            throw new IllegalStateException(Constants.INVALID_SEED_INPUT_ERROR);
        }
        
        if (!InputValidator.areValidInputsList(inputs)) {
            throw new ArgumentException(Constants.INVALID_INPUT_ERROR);
        }
        bundle.finalize(curl);
        bundle.addTrytes(signatureFragments);

        //  SIGNING OF INPUTS
        //
        //  Here we do the actual signing of the inputs
        //  Iterate over all bundle transactions, find the inputs
        //  Get the corresponding private key and calculate the signatureFragment
        for (int i = bundle.getTransactions().size()-1; i >= 0; i--) {
            if (bundle.getTransactions().get(i).getValue() < 0) {
                String thisAddress = bundle.getTransactions().get(i).getAddress();

                // Get the corresponding keyIndex of the address
                int keyIndex = 0;
                int keySecurity = 0;
                for (Input input : inputs) {
                    if (input.getAddress().subSequence(0,  81).equals(thisAddress.subSequence(0,  81))) {
                        keyIndex = input.getKeyIndex();
                        keySecurity = input.getSecurity();
                    }
                }

                String bundleHash = bundle.getTransactions().get(i).getBundle();

                // Get corresponding private key of address
                int[] key = new Signing(curl).key(Converter.trits(seed), keyIndex, keySecurity);


                //  Get the normalized bundle hash
                int[] normalizedBundleHash = bundle.normalizedBundle(bundleHash);

                // for each security level, add signature
                for (int j = 0; j < keySecurity; j++) {
                	
                	int hashPart = j % 3;

                    //  Add parts of signature for bundles with same address
                    if (bundle.getTransactions().get(i + j).getAddress().equals(thisAddress)) {
                        // Use 6562 trits starting from j*6561
                        int[] keyFragment = Arrays.copyOfRange(key, 6561 * j, 6561 * (j + 1));

                        // The current part of the bundle hash
                        int[] bundleFragment = Arrays.copyOfRange(normalizedBundleHash, 27 * hashPart, 27 * (hashPart + 1));

                        //  Calculate the new signature
                        int[] signedFragment = new Signing(curl).signatureFragment(bundleFragment, keyFragment);

                        //  Convert signature to trytes and assign it again to this bundle entry
                        bundle.getTransactions().get(i+j).setSignatureFragments(Converter.trytes(signedFragment));
                    } else {
						throw new ArgumentException("Inconsistent security-level and transactions");
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

