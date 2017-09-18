package jota.utils;

import jota.model.Bundle;
import jota.model.Transaction;
import jota.pow.ICurl;
import jota.pow.SpongeFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Signing {

    /**
     * Ask @come-from-beyond
     */
    private ICurl curl;

    /**
     * public Signing() {
     * this(null);
     * }
     * <p>
     * /**
     *
     * @param curl
     */
    public Signing(ICurl curl) {
        this.curl = curl == null ? SpongeFactory.create(SpongeFactory.Mode.KERL) : curl;
    }

    /**
     * @param seed
     * @param index
     * @param length
     * @return
     */
    public int[] key(int[] seed, int index, int length) {

        for (int i = 0; i < index; i++) {
            for (int j = 0; j < 243; j++) {
                if (++seed[j] > 1) {
                    seed[j] = -1;
                } else {
                    break;
                }
            }
        }

        curl.reset();
        curl.absorb(seed, 0, seed.length);
        curl.squeeze(seed, 0, seed.length);
        curl.reset();
        curl.absorb(seed, 0, seed.length);

        final int[] key = new int[length * seed.length * 27];
        int offset = 0;

        while (length-- > 0) {
            for (int i = 0; i < 27; i++) {
                curl.squeeze(key, offset, seed.length);
                offset += seed.length;
            }
        }
        return key;
    }

    public int[] signatureFragment(int[] normalizedBundleFragment, int[] keyFragment) {

        for (int i = 0; i < 27; i++) {
            for (int j = 0; j < 13 - normalizedBundleFragment[i]; j++) {
                curl.reset()
                        .absorb(keyFragment, i * 243, 243)
                        .squeeze(keyFragment, i * 243, 243);
            }
        }

        return keyFragment;
    }

    public int[] address(int[] digests) {
        int[] address = new int[243];
        curl.reset()
                .absorb(digests)
                .squeeze(address);
        return address;
    }

    public int[] digests(int[] key) {

        int[] digests = new int[(int) Math.floor(key.length / 6561) * 243];

        for (int i = 0; i < Math.floor(key.length / 6561); i++) {
            int[] keyFragment = Arrays.copyOfRange(key, i * 6561, (i + 1) * 6561);

            for (int j = 0; j < 27; j++) {
                for (int k = 0; k < 26; k++) {
                    curl.reset()
                            .absorb(keyFragment, j * 243, 243)
                            .squeeze(keyFragment, j * 243, 243);
                }
            }

            curl.reset();
            curl.absorb(keyFragment, 0, keyFragment.length);
            curl.squeeze(digests, i * 243, 243);
        }
        return digests;
    }

    public int[] digest(int[] normalizedBundleFragment, int[] signatureFragment) {
        curl.reset();
        ICurl jCurl = SpongeFactory.create(SpongeFactory.Mode.KERL);
        int[] buffer = new int[243];

        for (int i = 0; i < 27; i++) {
            buffer = Arrays.copyOfRange(signatureFragment, i * 243, (i + 1) * 243);

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
        int[] digests = new int[signatureFragments.length * 243];

        for (int i = 0; i < signatureFragments.length; i++) {

            int[] digestBuffer = digest(normalizedBundleFragments[i % 3], Converter.trits(signatureFragments[i]));

            System.arraycopy(digestBuffer, 0, digests, i * 243, 243);
        }
        String address = Converter.trytes(address(digests));

        return (expectedAddress.equals(address));
    }
}

