package jota.utils;

import jota.dto.response.GetBundleResponse;
import jota.model.Bundle;
import jota.model.Input;
import jota.model.Transaction;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Client Side computation service
 *
 * @author davassi
 */
public class IotaAPIUtils {

    private static final Logger log = LoggerFactory.getLogger(IotaAPIUtils.class);

    /**
     * Generates a new address
     *
     * @param seed
     * @param index
     * @param checksum
     * @return an String with address
     */
    public static String newAddress(String seed, int index, boolean checksum) {

        final int[] key = Signing.key(Converter.trits(seed), index, 2);
        final int[] digests = Signing.digests(key);
        final int[] addressTrits = Signing.address(digests);

        String address = Converter.trytes(addressTrits);

        if (checksum) {
            address = Checksum.addChecksum(address);
        }
        return address;
    }

    public static GetBundleResponse getBundle(final String transaction) {
        throw new NotImplementedException("Not yet implemented");
    }

    public static String transactionTrytes(Transaction trx) {
        int[] valueTrits = Converter.trits(trx.getValue());
        while (valueTrits.length < 81) {
            valueTrits[valueTrits.length] = 0;
        }

        int[] timestampTrits = Converter.trits(trx.getTimestamp());
        while (timestampTrits.length < 27) {
            timestampTrits[timestampTrits.length] = 0;
        }

        int[] currentIndexTrits = Converter.trits(trx.getTimestamp());
        while (currentIndexTrits.length < 27) {
            currentIndexTrits[currentIndexTrits.length] = 0;
        }

        int[] lastIndexTrits = Converter.trits(trx.getCurrentIndex());
        while (lastIndexTrits.length < 27) {
            lastIndexTrits[lastIndexTrits.length] = 0;
        }

        return trx.getSignatureMessageChunk()
                + trx.getAddress()
                + Converter.trytes(valueTrits)
                + trx.getTag()
                + Converter.trytes(timestampTrits)
                + Converter.trytes(currentIndexTrits)
                + Converter.trytes(lastIndexTrits)
                + trx.getBundle()
                + trx.getTrunkTransaction()
                + trx.getBranchTransaction()
                + trx.getNonce();
    }

    public static List<String> signInputsAndReturn(String seed, List<Input> inputs, Bundle bundle,
                                                   List<String> signatureFragments) {
        bundle.finalize();
        bundle.addTrytes(signatureFragments);

        //  SIGNING OF INPUTS
        //
        //  Here we do the actual signing of the inputs
        //  Iterate over all bundle transactions, find the inputs
        //  Get the corresponding private key and calculate the signatureFragment
        for (int i = 0; i < bundle.getTransactions().size(); i++) {
            if (Long.parseLong(bundle.getTransactions().get(i).getValue()) < 0) {
                String thisAddress = bundle.getTransactions().get(i).getAddress();

                // Get the corresponding keyIndex of the address
                int keyIndex = 0;
                for (int k = 0; k < inputs.size(); k++) {
                    if (inputs.get(k).getAddress().equals(thisAddress)) {
                        keyIndex = inputs.get(k).getKeyIndex();
                        break;
                    }
                }

                String bundleHash = bundle.getTransactions().get(i).getBundle();

                // Get corresponding private key of address
                int[] key = Signing.key(Converter.trits(seed), keyIndex, 2);

                //  First 6561 trits for the firstFragment
                int[] firstFragment = Arrays.copyOfRange(key, 0, 6561);

                //  Get the normalized bundle hash
                int[] normalizedBundleHash = bundle.normalizedBundle(bundleHash);

                //  First bundle fragment uses 27 trytes
                int[] firstBundleFragment = Arrays.copyOfRange(normalizedBundleHash, 0, 27);

                //  Calculate the new signatureFragment with the first bundle fragment
                int[] firstSignedFragment = Signing.signatureFragment(firstBundleFragment, firstFragment);

                //  Convert signature to trytes and assign the new signatureFragment
                bundle.getTransactions().get(i).setSignatureMessageChunk(Converter.trytes(firstSignedFragment));

                //  Because the signature is > 2187 trytes, we need to
                //  find the second transaction to add the remainder of the signature
                for (int j = 0; j < bundle.getTransactions().size(); j++) {
                    //  Same address as well as value = 0 (as we already spent the input)
                    if (bundle.getTransactions().get(j).getAddress() == thisAddress && Long.parseLong(bundle.getTransactions().get(j).getValue()) == 0) {
                        // Use the second 6562 trits
                        int[] secondFragment = Arrays.copyOfRange(key, 6561, 6561 * 2);

                        // The second 27 to 54 trytes of the bundle hash
                        int[] secondBundleFragment = Arrays.copyOfRange(normalizedBundleHash, 27, 27 * 2);

                        //  Calculate the new signature
                        int[] secondSignedFragment = Signing.signatureFragment(secondBundleFragment, secondFragment);

                        //  Convert signature to trytes and assign it again to this bundle entry
                        bundle.getTransactions().get(j).setSignatureMessageChunk(Converter.trytes(secondSignedFragment));
                    }
                }
            }
        }

        List<String> bundleTrytes = new ArrayList<>();

        // Convert all bundle entries into trytes
        for (Transaction tx : bundle.getTransactions()) {
            bundleTrytes.add(IotaAPIUtils.transactionTrytes(tx));
        }
        Collections.reverse(bundleTrytes);
        return bundleTrytes;
    }
}
