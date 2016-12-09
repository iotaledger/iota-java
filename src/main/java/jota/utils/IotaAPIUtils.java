package jota.utils;

import jota.dto.response.GetBundleResponse;
import jota.model.Bundle;
import jota.model.Input;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /*
    public static  List<String> signInputsAndReturn(String seed, List<Input> inputs, Bundle bundle,
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
                var firstFragment = key.Take(6561);

                //  Get the normalized bundle hash
                String normalizedBundleHash = bundle.normalizedBundle(bundleHash);
                    /*
                    //  First bundle fragment uses 27 trytes
                    var firstBundleFragment = normalizedBundleHash(27);

                    //  Calculate the new signatureFragment with the first bundle fragment
                    var firstSignedFragment = Signing.signatureFragment(firstBundleFragment, firstFragment);

                    //  Convert signature to trytes and assign the new signatureFragment
                    bundle.Transactions[i].signatureMessageFragment = Converter.trytes(firstSignedFragment);

                    //  Because the signature is > 2187 trytes, we need to
                    //  find the second transaction to add the remainder of the signature
                    for (var j = 0; j < bundle.Transactions.Count; j++)
                    {
                        //  Same address as well as value = 0 (as we already spent the input)
                        if (bundle.Transactions[j].Address == thisAddress && bundle.Transactions[j].Value == 0)
                        {
                            // Use the second 6562 trits
                            var secondFragment = key.Skip(6561).Take(6561);

                            // The second 27 to 54 trytes of the bundle hash
                            var secondBundleFragment = normalizedBundleHash.slice(27, 27*2);

                            //  Calculate the new signature
                            var secondSignedFragment = Signing.signatureFragment(secondBundleFragment, secondFragment);

                            //  Convert signature to trytes and assign it again to this bundle entry
                            bundle.Transactions[j].signatureMessageFragment = Converter.trytes(secondSignedFragment);
                        }
                    }*/
    //   }
    // }
}

