package jota.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Signing {

    static int[] key(int [] seed, int index, int length) {

        final int[] subseed = seed;

        for (int i = 0; i < index; i++) {
            for (int j = 0; j < 243; j++) {
                if (++subseed[j] > 1) {
                    subseed[j] = -1;
                } else {
                    break;
                }
            }
        }

        Curl curl = new Curl();
        //curl.absorb(subseed, state);
        //curl.squeeze(subseed, state);
        curl.absorb(subseed, 0, subseed.length);

        List<Integer> key = new ArrayList<>();
        int [] buffer = new int[subseed.length];
        int offset = 0;

        while (length-- > 0) {

            for (int i = 0; i < 27; i++) {

                curl.squeeze(buffer, 0, buffer.length);
                for (int j = 0; j < 243; j++) {
                    key.add(buffer[j]);
                }
            }
        }
        return to(key);
    }

    private static int[] to(List<Integer> key) {
        int a [] = new int[key.size()]; int i = 0;
        for (Integer v : key) {
            a[i++] = v;
        }
        return a;
    }

    public static int [] digests(int [] key) {
        final Curl curl = new Curl();

        int[] digests = new int[key.length];
        int[] buffer = new int[key.length];

        for (int i = 0; i < Math.floor(key.length / 6561); i++) {
            int [] keyFragment = Arrays.copyOfRange(key, i * 6561, (i + 1) * 6561);

            for (int j = 0; j < 27; j++) {

                buffer = Arrays.copyOfRange(keyFragment, j * 243, (j + 1) * 243);
                for (int k = 0; k < 26; k++) {

                    curl.absorb(buffer, 0, buffer.length);
                    curl.squeeze(buffer, 0, buffer.length);
                }
                for (int k = 0; k < 243; k++) {

                    keyFragment[j * 243 + k] = buffer[k];
                }
            }

            curl.absorb(keyFragment, 0, keyFragment.length);
            curl.squeeze(buffer, 0, buffer.length);

            for (int j = 0; j < 243; j++) {
                digests[i * 243 + j] = buffer[j];
            }
        }
        return digests;
    }

    public static int [] address(int [] digests) {
        final Curl curl = new Curl();
        int [] address = new int[digests.length];
        curl.absorb(digests, 0, digests.length);
        curl.squeeze(address, 0, address.length);
        return address;
    }
}
