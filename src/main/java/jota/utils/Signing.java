package jota.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Signing {

    static int[] key(int[] seed, int index, int length) {

        for (int i = 0; i < index; i++) {
            for (int j = 0; j < 243; j++) {
                if (++seed[j] > 1) {
                    seed[j] = -1;
                } else {
                    break;
                }
            }
        }

        final Curl curl = new Curl();
        curl.reset();
        curl.absorb(seed, 0, seed.length);
        curl.squeeze(seed, 0, seed.length);
        curl.reset();
        curl.absorb(seed, 0, seed.length);

        final List<Integer> key = new ArrayList<>();
        int[] buffer = new int[seed.length];
        int offset = 0;

        while (length-- > 0) {

            for (int i = 0; i < 27; i++) {

                curl.squeeze(buffer, offset, buffer.length);
                for (int j = 0; j < 243; j++) {
                    key.add(buffer[j]);
                }
            }
        }
        return to(key);
    }

    private static int[] to(List<Integer> key) {
        int a[] = new int[key.size()];
        int i = 0;
        for (Integer v : key) {
            a[i++] = v;
        }
        return a;
    }

    public static int[] digests(int[] key) {
        final Curl curl = new Curl();

        int[] digests = new int[(int) Math.floor(key.length / 6561) * 243];
        int[] buffer = new int[243];

        for (int i = 0; i < Math.floor(key.length / 6561); i++) {
            int[] keyFragment = Arrays.copyOfRange(key, i * 6561, (i + 1) * 6561);

            for (int j = 0; j < 27; j++) {

                buffer = Arrays.copyOfRange(keyFragment, j * 243, (j + 1) * 243);
                for (int k = 0; k < 26; k++) {
                    curl.reset();
                    curl.absorb(buffer, 0, buffer.length);
                    curl.squeeze(buffer, 0, buffer.length);
                }
                System.arraycopy(buffer, 0, keyFragment, j * 243, 243);
            }

            curl.reset();
            curl.absorb(keyFragment, 0, keyFragment.length);
            curl.squeeze(buffer, 0, buffer.length);

            System.arraycopy(buffer, 0, digests, i * 243, 243);
        }
        return digests;
    }

    public static int[] address(int[] digests) {
        final Curl curl = new Curl();
        int[] address = new int[243];
        curl.reset();
        curl.absorb(digests, 0, digests.length);
        curl.squeeze(address, 0, address.length);
        return address;
    }
}
