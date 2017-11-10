package jota.utils;

import jota.error.ArgumentException;
import jota.model.Bundle;
import jota.model.Input;
import jota.model.Transaction;
import jota.pow.ICurl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static jota.utils.Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR;

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
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public static String newAddress(String seed, int security, int index, boolean checksum, ICurl curl) throws ArgumentException {

        if (security < 1) {
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

    public static List<String> signInputsAndReturn(final String seed,
                                                   final List<Input> inputs,
                                                   final Bundle bundle,
                                                   final List<String> signatureFragments, ICurl curl) throws ArgumentException {

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
                for (Input input : inputs) {
                    if (input.getAddress().equals(thisAddress)) {
                        keyIndex = input.getKeyIndex();
                        keySecurity = input.getSecurity();
                    }
                }

                String bundleHash = bundle.getTransactions().get(i).getBundle();

                // Get corresponding private key of address
                int[] key = new Signing(curl).key(Converter.trits(seed), keyIndex, keySecurity);

                //  First 6561 trits for the firstFragment
                int[] firstFragment = Arrays.copyOfRange(key, 0, 6561);

                //  Get the normalized bundle hash
                int[] normalizedBundleHash = bundle.normalizedBundle(bundleHash);

                //  First bundle fragment uses 27 trytes
                int[] firstBundleFragment = Arrays.copyOfRange(normalizedBundleHash, 0, 27);

                //  Calculate the new signatureFragment with the first bundle fragment
                int[] firstSignedFragment = new Signing(curl).signatureFragment(firstBundleFragment, firstFragment);

                //  Convert signature to trytes and assign the new signatureFragment
                bundle.getTransactions().get(i).setSignatureFragments(Converter.trytes(firstSignedFragment));

                // if user chooses higher than 27-tryte security
                // for each security level, add an additional signature
                for (int j = 1; j < keySecurity; j++) {

                    //  Because the signature is > 2187 trytes, we need to
                    //  find the second transaction to add the remainder of the signature
                    //  Same address as well as value = 0 (as we already spent the input)
                    if (bundle.getTransactions().get(i + j).getAddress().equals(thisAddress) && bundle.getTransactions().get(i + j).getValue() == 0) {
                        // Use the second 6562 trits
                        int[] secondFragment = Arrays.copyOfRange(key, 6561 * j, 6561 * (j + 1));

                        // The second 27 to 54 trytes of the bundle hash
                        int[] secondBundleFragment = Arrays.copyOfRange(normalizedBundleHash, 27 * j, 27 * (j + 1));

                        //  Calculate the new signature
                        int[] secondSignedFragment = new Signing(curl).signatureFragment(secondBundleFragment, secondFragment);

                        //  Convert signature to trytes and assign it again to this bundle entry
                        bundle.getTransactions().get(i+j).setSignatureFragments(Converter.trytes(secondSignedFragment));
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

