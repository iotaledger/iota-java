package jota.utils;

import java.util.Random;

/**
 * Created by pinpong on 13.12.16.
 */
public class SeedRandomGenerator {

    public static String generateNewSeed() {
        char[] chars = Constants.TRYTE_ALPHABET.toCharArray();
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < Constants.SEED_LENGTH_MAX; i++) {
            char c = chars[random.nextInt(chars.length)];
            builder.append(c);
        }
        return builder.toString();
    }
}
