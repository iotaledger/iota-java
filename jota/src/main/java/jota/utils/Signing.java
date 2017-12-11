package jota.utils;

import jota.error.ArgumentException;
import jota.model.Bundle;
import jota.model.Transaction;
import jota.pow.ICurl;
import jota.pow.SpongeFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jota.pow.JCurl.HASH_LENGTH;
import static jota.utils.Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR;


public class Signing {
    public final static int KEY_LENGTH = 6561;

    private ICurl curl;

    /**
     * public Signing() {
     * this(null);
     * }
     *
     * /**
     *
     * @param curl
     */
    public Signing(ICurl curl) {
        this.curl = curl == null ? SpongeFactory.create(SpongeFactory.Mode.KERL) : curl;
    }

    /**
     * @param inSeed
     * @param index
     * @param security
     * @return
     * @throws ArgumentException is thrown when the specified security level is not valid.
     */
    public int[] key(final int[] inSeed, final int index, int security) throws ArgumentException {
        if (security < 1) {
            throw new ArgumentException(INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        int[] seed = inSeed.clone();

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

    public int[] address(int[] digests) {
        int[] address = new int[HASH_LENGTH];
        ICurl curl = this.getICurlObject(SpongeFactory.Mode.KERL);
        curl.reset()
                .absorb(digests)
                .squeeze(address);
        return address;
    }

    public int[] digests(int[] key) {
        int security = (int) Math.floor(key.length / KEY_LENGTH);

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
}

