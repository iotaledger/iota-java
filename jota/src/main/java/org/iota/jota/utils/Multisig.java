package org.iota.jota.utils;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.SpongeFactory;

import java.util.Arrays;

/**
 * @author pinpong
 */
public class Multisig {

    private ICurl curl;
    private Signing signingInstance;

    /**
     * Initializes a new instance of the Multisig class.
     */
    public Multisig(ICurl customCurl) {
        this.curl = customCurl;
        this.curl.reset();
        this.signingInstance = new Signing(curl.clone());
    }

    /**
     * Initializes a new instance of the Multisig class.
     */
    public Multisig() {
        this(SpongeFactory.create(SpongeFactory.Mode.KERL));
    }

    /**
     * @param seed     Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security Secuirty level of private key / seed.
     * @param index    Key index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @return trytes
     * @throws ArgumentException is thrown when the specified security level is not valid.
     **/
    public String getDigest(String seed, int security, int index) throws ArgumentException {
        int[] key = signingInstance.key(Converter.trits(seed, 243), index, security);
        return Converter.trytes(signingInstance.digests(key));
    }

    /**
     * Initiates the generation of a new multisig address or adds the key digest to an existing one
     *
     * @param digestTrytes
     * @param curlStateTrytes
     * @return trytes.
     **/
    public String addAddressDigest(String digestTrytes, String curlStateTrytes) {


        int[] digest = Converter.trits(digestTrytes, digestTrytes.length() * 3);

        // If curlStateTrytes is provided, convert into trits
        // else use empty state and initiate the creation of a new address

        int[] curlState = !curlStateTrytes.isEmpty() ? Converter.trits(curlStateTrytes,
                digestTrytes.length() * 3) : new int[digestTrytes.length() * 3];

        // initialize Curl with the provided state
        curl.setState(curlState);
        // absorb the key digest
        curl.absorb(digest);

        return Converter.trytes(curl.getState());
    }

    /**
     * Gets the key value of a seed
     *
     * @param seed  Tryte-encoded seed. It should be noted that this seed is not transferred
     * @param index Key index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @return trytes.
     * @throws ArgumentException is thrown when the specified security level is not valid.
     **/

    public String getKey(String seed, int index, int security) throws ArgumentException {

        return Converter.trytes(signingInstance.key(Converter.trits(seed, Constants.HASH_LENGTH_TRITS), index, security));
    }

    /**
     * Generates a new address
     *
     * @param curlStateTrytes
     * @return address
     **/
    public String finalizeAddress(String curlStateTrytes) {

        int[] curlState = Converter.trits(curlStateTrytes);

        // initialize Curl with the provided state
        curl.setState(curlState);

        int[] addressTrits = new int[243];
        curl.squeeze(addressTrits);

        // Convert trits into trytes and return the address
        return Converter.trytes(addressTrits);
    }

    /**
     * Validates  a generated multisig address
     *
     * @param multisigAddress
     * @param digests
     * @return <tt>true</tt> if the digests turn into multiSigAddress, otherwise <tt>false</tt>
     **/
    public boolean validateAddress(String multisigAddress, int[][] digests) {

        // initialize Curl with the provided state
        curl.reset();


        for (int[] keyDigest : digests) {
            curl.absorb(keyDigest);
        }

        int[] addressTrits = new int[243];
        curl.squeeze(addressTrits);

        // Convert trits into trytes and return the address
        return Converter.trytes(addressTrits).equals(multisigAddress);
    }

    /**
     * Adds the cosigner signatures to the corresponding bundle transaction
     *
     * @param bundleToSign
     * @param inputAddress
     * @param keyTrytes
     * @return Returns bundle trytes.
     **/
    public Bundle addSignature(Bundle bundleToSign, String inputAddress, String keyTrytes) {


        // Get the security used for the private key
        // 1 security level = 2187 trytes
        int security = (keyTrytes.length() / Constants.MESSAGE_LENGTH);

        // convert private key trytes into trits
        int[] key = Converter.trits(keyTrytes);


        // First get the total number of already signed transactions
        // use that for the bundle hash calculation as well as knowing
        // where to add the signature
        int numSignedTxs = 0;


        for (int i = 0; i < bundleToSign.getTransactions().size(); i++) {

            if (bundleToSign.getTransactions().get(i).getAddress().equals(inputAddress)) {

                // If transaction is already signed, increase counter
                if (!InputValidator.isNinesTrytes(bundleToSign.getTransactions().get(i).getSignatureFragments(),
                        bundleToSign.getTransactions().get(i).getSignatureFragments().length())) {

                    numSignedTxs++;
                }
                // Else sign the transactions
                else {

                    String bundleHash = bundleToSign.getTransactions().get(i).getBundle();

                    //  First 6561 trits for the firstFragment
                    int[] firstFragment = Arrays.copyOfRange(key, 0, 6561);

                    //  Get the normalized bundle hash
                    int[][] normalizedBundleFragments = new int[3][27];
                    int[] normalizedBundleHash = bundleToSign.normalizedBundle(bundleHash);


                    // Split hash into 3 fragments
                    for (int k = 0; k < 3; k++) {
                        normalizedBundleFragments[k] = Arrays.copyOfRange(normalizedBundleHash, (k * 27), (k + 1) * 27);
                    }


                    //  First bundle fragment uses 27 trytes
                    int[] firstBundleFragment = normalizedBundleFragments[numSignedTxs % 3];

                    //  Calculate the new signatureFragment with the first bundle fragment
                    int[] firstSignedFragment = signingInstance.signatureFragment(firstBundleFragment, firstFragment);

                    //  Convert signature to trytes and assign the new signatureFragment
                    bundleToSign.getTransactions().get(i).setSignatureFragments(Converter.trytes(firstSignedFragment));

                    for (int j = 1; j < security; j++) {

                        //  Next 6561 trits for the firstFragment
                        int[] nextFragment = Arrays.copyOfRange(key, 6561 * j, (j + 1) * 6561);

                        //  Use the next 27 trytes
                        int[] nextBundleFragment = normalizedBundleFragments[(numSignedTxs + j) % 3];

                        //  Calculate the new signatureFragment with the first bundle fragment
                        int[] nextSignedFragment = signingInstance.signatureFragment(nextBundleFragment, nextFragment);

                        //  Convert signature to trytes and add new bundle entry at i + j position
                        // Assign the signature fragment
                        bundleToSign.getTransactions().get(i + j).setSignatureFragments(Converter.trytes(nextSignedFragment));
                    }

                    break;
                }
            }
        }

        return bundleToSign;
    }
}