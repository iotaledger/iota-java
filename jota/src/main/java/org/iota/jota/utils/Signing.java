package org.iota.jota.utils;

import static org.iota.jota.pow.JCurl.HASH_LENGTH;
import static org.iota.jota.utils.Constants.INVALID_INDEX_INPUT_ERROR;
import static org.iota.jota.utils.Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR;
import static org.iota.jota.utils.Constants.INVALID_SEED_INPUT_ERROR;
import static org.iota.jota.utils.Constants.KEY_LENGTH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.SpongeFactory;


public class Signing {
    

    private ICurl curl;

    
    public Signing() {
        this(Optional.empty());
    }
    
    public Signing(ICurl curl) {
        this(curl != null ? Optional.of(curl) : Optional.empty());
    }
     
    /**
     *
     * @param curl
     */
    public Signing(Optional<ICurl> curl) {
        this.curl = curl == null || !curl.isPresent() ? SpongeFactory.create(SpongeFactory.Mode.KERL) : curl.get();
    }
    
    /**
     * Returns the sub-seed trits given a seed and an index
     * @param inSeed the seed
     * @param index the index
     * @return the sub-seed
     */
    public int[] subseed(int[] inSeed, int index) throws ArgumentException {
        if (index < 0) {
            throw new ArgumentException(INVALID_INDEX_INPUT_ERROR);
        }
        
        int[] seed = inSeed.clone();

        /*
         * 
         * index 0 = [0,0,0,1,0,-1,0,1,1,0,-1,1,-1,0]
         * index 1 = [1,0,0,1,0,-1,0,1,1,0,-1,1,-1,0]
         * index 2 = [-1,1,0,1,0,-1,0,1,1,0,-1,1,-1,0]
         * index 3 = [0,1,0,1,0,-1,0,1,1,0,-1,1,-1,0]
         */
        
        // Derive subseed.
        for (int i = 0; i < index; i++) {
            for (int j = 0; j < seed.length; j++) {
                if (++seed[j] > 1) {
                    seed[j] = -1;
                } else {
                    break;
                }
            }
        }
        return seed;
    }
    
    /**
     * Generates the key which is needed as a part of address generation.
     * Used in {@link #digests(int[])}
     * Calculates security based on <code>inSeed length / key length</code>
     * 
     * @param inSeed    Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param index     Key index for the address
     * @return The key
     * @throws ArgumentException is thrown when the specified security level is not valid
     * @throws ArgumentException is thrown when inSeed length is not dividable by 3
     * @throws ArgumentException is thrown when index is below 1
     */
    public int[] key(int[] inSeed, int index) throws ArgumentException {
        return key(inSeed, index, (int) Math.floor(inSeed.length / KEY_LENGTH));
    }

    /**
     * Generates the key which is needed as a part of address generation.
     * Used in {@link #digests(int[])}
     * 
     * @param inSeed    Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param index     Key index for the address
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @return The key
     * @throws ArgumentException is thrown when the specified security level is not valid
     * @throws ArgumentException is thrown when inSeed length is not dividable by 3
     * @throws ArgumentException is thrown when index is below 1
     */
    public int[] key(int[] inSeed, int index, int security) throws ArgumentException {
        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }
        
        if (inSeed.length % 3 != 0) {
            throw new ArgumentException(INVALID_SEED_INPUT_ERROR);
        }

        int[] seed = subseed(inSeed, index);
        
        ICurl curl = this.getICurlObject(SpongeFactory.Mode.KERL);
        curl.reset();
        curl.absorb(seed, 0, seed.length);
        // seed[0..HASH_LENGTH] contains subseed
        curl.squeeze(seed, 0, seed.length);
        curl.reset();
        // absorb subseed
        curl.absorb(seed, 0, seed.length);

        final int[] key = new int[security * HASH_LENGTH * 27];
        final int[] buffer = new int[seed.length];
        int offset = 0;

        while (security-- > 0) {
            for (int i = 0; i < 27; i++) {
                curl.squeeze(buffer, 0, seed.length);
                System.arraycopy(buffer, 0, key, offset, HASH_LENGTH);

                offset += HASH_LENGTH;
            }
        }
        return key;
    }

    /**
     * Address generates the address trits from the given digests.
     * @param digests the digests
     * @return the address trits
     */
    public int[] address(int[] digests) {
        int[] address = new int[HASH_LENGTH];
        ICurl curl = this.getICurlObject(SpongeFactory.Mode.KERL);
        curl.reset()
                .absorb(digests)
                .squeeze(address);
        return address;
    }

    /**
     * Digests hashes each segment of each key fragment 26 times and returns them.
     * @param key the key trits
     * @return the digests
     * @throws ArgumentException if the security level is invalid
     */
    public int[] digests(int[] key) throws ArgumentException {
        int security = (int) Math.floor(key.length / KEY_LENGTH);
        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }
        
        int[] digests = new int[security * HASH_LENGTH];
        int[] keyFragment = new int[KEY_LENGTH];

        ICurl curl = this.getICurlObject(SpongeFactory.Mode.KERL);
        for (int i = 0; i < Math.floor(key.length / KEY_LENGTH); i++) {
            System.arraycopy(key, i * KEY_LENGTH, keyFragment, 0, KEY_LENGTH);

            for (int j = 0; j < 27; j++) {
                for (int k = 0; k < 26; k++) {
                    curl.reset()
                            .absorb(keyFragment, j * HASH_LENGTH, HASH_LENGTH)
                            .squeeze(keyFragment, j * HASH_LENGTH, HASH_LENGTH);
                }
            }

            curl.reset();
            curl.absorb(keyFragment, 0, keyFragment.length);
            curl.squeeze(digests, i * HASH_LENGTH, HASH_LENGTH);
        }
        return digests;
    }

    /**
     * 
     * @param normalizedBundleFragment
     * @param signatureFragment
     * @return The digest
     */
    public int[] digest(int[] normalizedBundleFragment, int[] signatureFragment) {
        curl.reset();
        ICurl jCurl = this.getICurlObject(SpongeFactory.Mode.KERL);
        int[] buffer = new int[HASH_LENGTH];

        for (int i = 0; i < 27; i++) {
            buffer = Arrays.copyOfRange(signatureFragment, i * HASH_LENGTH, (i + 1) * HASH_LENGTH);

            for (int j = normalizedBundleFragment[i] + 13; j-- > 0; ) {
                jCurl.reset();
                jCurl.absorb(buffer);
                jCurl.squeeze(buffer);
            }
            curl.absorb(buffer);
        }
        curl.squeeze(buffer);

        return buffer;
    }
    
    public int[] signatureFragment(int[] normalizedBundleFragment, int[] keyFragment) {
        int[] signatureFragment = keyFragment.clone();

        for (int i = 0; i < 27; i++) {

            for (int j = 0; j < 13 - normalizedBundleFragment[i]; j++) {
                curl.reset()
                        .absorb(signatureFragment, i * HASH_LENGTH, HASH_LENGTH)
                        .squeeze(signatureFragment, i * HASH_LENGTH, HASH_LENGTH);
            }
        }

        return signatureFragment;
    }

    public Boolean validateSignatures(Bundle signedBundle, String inputAddress) {
        String bundleHash = "";
        Transaction trx;
        List<String> signatureFragments = new ArrayList<>();

        for (int i = 0; i < signedBundle.getTransactions().size(); i++) {
            trx = signedBundle.getTransactions().get(i);

            if (trx.getAddress().equals(inputAddress)) {
                bundleHash = trx.getBundle();

                // if we reached remainder bundle
                String signatureFragment = trx.getSignatureFragments();
                if (InputValidator.isNinesTrytes(signatureFragment, signatureFragment.length())) {
                    break;
                }
                signatureFragments.add(signatureFragment);
            }
        }

        return validateSignatures(inputAddress, signatureFragments.toArray(new String[signatureFragments.size()]), bundleHash);
    }


    public Boolean validateSignatures(String expectedAddress, String[] signatureFragments, String bundleHash) {
        Bundle bundle = new Bundle();

        int[][] normalizedBundleFragments = new int[3][27];
        int[] normalizedBundleHash = bundle.normalizedBundle(bundleHash);

        // Split hash into 3 fragments
        for (int i = 0; i < 3; i++) {
            normalizedBundleFragments[i] = Arrays.copyOfRange(normalizedBundleHash, i * 27, (i + 1) * 27);
        }

        // Get digests
        int[] digests = new int[signatureFragments.length * HASH_LENGTH];

        for (int i = 0; i < signatureFragments.length; i++) {

            int[] digestBuffer = digest(normalizedBundleFragments[i % 3], Converter.trits(signatureFragments[i]));

            System.arraycopy(digestBuffer, 0, digests, i * HASH_LENGTH, HASH_LENGTH);
        }
        String address = Converter.trytes(address(digests));

        return (expectedAddress.equals(address));
    }
    
    private ICurl getICurlObject(SpongeFactory.Mode mode) {
    	return SpongeFactory.create(mode);
    }

    /**
     * Normalizes the given bundle hash, with resulting digits summing to zero.
     * It returns a slice with the tryte decimal representation without any 13/M values.
     * @param bundleHash the bundle hash
     * @return the normalized bundle hash in trits
     */
    public int[] normalizedBundle(String bundleHash) {
        int[] normalizedBundle = new int[81];

        for (int i = 0; i < 3; i++) {

            long sum = 0;
            for (int j = 0; j < 27; j++) {

                sum += (normalizedBundle[i * 27 + j] = Converter.value(Converter.trits("" + bundleHash.charAt(i * 27 + j))));
            }

            if (sum >= 0) {
                while (sum-- > 0) {
                    for (int j = 0; j < 27; j++) {
                        if (normalizedBundle[i * 27 + j] > -13) {
                            normalizedBundle[i * 27 + j]--;
                            break;
                        }
                    }
                }
            } else {

                while (sum++ < 0) {

                    for (int j = 0; j < 27; j++) {

                        if (normalizedBundle[i * 27 + j] < 13) {
                            normalizedBundle[i * 27 + j]++;
                            break;
                        }
                    }
                }
            }
        }

        return normalizedBundle;
    }
}

