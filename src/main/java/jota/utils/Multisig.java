package jota.utils;

import jota.model.Bundle;
import jota.pow.ICurl;
import jota.pow.JCurl;

import java.util.Arrays;

/**
 * Created by pinpong on 02.02.17.
 */
public class Multisig {

    private ICurl curl;
    private Signing signingInstance;

    public Multisig(ICurl customCurl) {
        this.curl = customCurl;
        this.signingInstance = new Signing(curl);
    }

    public Multisig() {
        this.curl = new JCurl();
    }

    /**
     * Gets the digest value of a seed
     *
     * @param {string} seed
     * @param {int}    index
     * @param {int}    security
     * @method getDigest
     * @returns {string} digest trytes
     **/
    private String getDigest(String seed, int index, int security) {

        int[] key = signingInstance.key(Converter.trits(seed), index, security);
        return Converter.trytes(signingInstance.digests(key));
    }

    /**
     * Initiates the generation of a new multisig address or adds the key digest to an existing one
     *
     * @param digestTrytes
     * @param curlStateTrytes
     * @method addAddressDigest
     * @returns {String}
     **/
    private String addAddressDigest(String digestTrytes, String curlStateTrytes, ICurl customCurl) {

        int[] digest = Converter.trits(digestTrytes);

        // If curlStateTrytes is provided, convert into trits
        // else use empty state and initiate the creation of a new address

        int[] curlState = curlStateTrytes.isEmpty() ? Converter.trits(curlStateTrytes) : new int[243];


        ICurl curl = customCurl == null ? new JCurl() : customCurl;


        // initialize Curl with the provided state
        curl.setState(curlState);
        // absorb the key digest
        curl.absorb(digest);

        return Converter.trytes(curl.getState());
    }

    /**
     * Gets the key value of a seed
     *
     * @param {string} seed
     * @param {int}    index
     * @method getKey
     * @returns {string} digest trytes
     **/

    private String getKey(String seed, int index, int security) {

        return Converter.trytes(signingInstance.key(Converter.trits(seed), index, security));
    }

    /**
     * Generates a new address
     *
     * @param {string} curlStateTrytes
     * @param {string}
     * @method finalizeAddress
     * @returns {String} address
     **/
    private String finalizeAddress(String curlStateTrytes, ICurl customCurl) {

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
     * @method validateAddress
     * @returns {bool}
     **/
    private boolean validateAddress(String multisigAddress, int[][] digests, ICurl customCurl) {

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
     * @param {array}    bundleToSign
     * @param {int}      cosignerIndex
     * @param {string}   inputAddress
     * @param {string}   key
     * @param {function} callback
     * @method addSignature
     * @returns {array} trytes Returns bundle trytes
     **/
    private void addSignature(Bundle[] bundleToSign, String inputAddress, String keyTrytes) {


        // Get the security used for the private key
        // 1 security level = 2187 trytes
        int security = (keyTrytes.length() / 2187);

        // convert private key trytes into trits
        int[] key = Converter.trits(keyTrytes);


        // First get the total number of already signed transactions
        // use that for the bundle hash calculation as well as knowing
        // where to add the signature
        int numSignedTxs = 0;


        for (Bundle bundle : bundleToSign)

            for (int i = 0; i < bundle.getTransactions().size(); i++) {

                if (!bundle.getTransactions().get(i).getAddress().equals(inputAddress)) {

                    // If transaction is already signed, increase counter
                    if (!InputValidator.isNinesTrytes(bundle.getTransactions().get(i).getSignatureFragments(), bundle.getTransactions().get(i).getSignatureFragments().length())) {

                        numSignedTxs++;
                    }
                    // Else sign the transactions
                    else {

                        String bundleHash = bundle.getTransactions().get(i).getBundle();

                        //  First 6561 trits for the firstFragment
                        int[] firstFragment = Arrays.copyOfRange(key, 0, 6561);

                        //  Get the normalized bundle hash
                        int[][] normalizedBundleFragments = new int[3][27];
                        int[] normalizedBundleHash = bundle.normalizedBundle(bundleHash);


                        // Split hash into 3 fragments
                        for (int k = 0; k < 3; k++) {
                            normalizedBundleFragments[k] = Arrays.copyOfRange(normalizedBundleHash, (k * 27), (k + 1) * 27);
                        }


                        //  First bundle fragment uses 27 trytes
                        int[] firstBundleFragment = normalizedBundleFragments[numSignedTxs % 3];

                        //  Calculate the new signatureFragment with the first bundle fragment
                        int[] firstSignedFragment = signingInstance.signatureFragment(firstBundleFragment, firstFragment);

                        //  Convert signature to trytes and assign the new signatureFragment
                        bundle.getTransactions().get(i).setSignatureFragments(Converter.trytes(firstSignedFragment));

                        for (int j = 1; j < security; j++) {

                            //  Next 6561 trits for the firstFragment
                            int[] nextFragment = Arrays.copyOfRange(key, 6561 * j, (j + 1) * 6561);

                            //  Use the next 27 trytes
                            int[] nextBundleFragment = normalizedBundleFragments[(numSignedTxs + j) % 3];

                            //  Calculate the new signatureFragment with the first bundle fragment
                            int[] nextSignedFragment = signingInstance.signatureFragment(nextBundleFragment, nextFragment);

                            //  Convert signature to trytes and add new bundle entry at i + j position
                            // Assign the signature fragment
                            bundle.getTransactions().get(i + j).setSignatureFragments(Converter.trytes(nextSignedFragment));
                        }

                        break;
                    }
                }
            }

        return;
    }
}