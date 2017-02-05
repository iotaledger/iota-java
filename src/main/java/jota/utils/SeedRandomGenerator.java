package jota.utils;

import java.security.SecureRandom;

/**
 * Created by pinpong on 13.12.16.
 */
public class SeedRandomGenerator {


    /**
     * generate a new seed
     *
     * @return random generated seed
     **/
    public static String generateNewSeed() {
        char[] chars = Constants.TRYTE_ALPHABET.toCharArray();
        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < Constants.SEED_LENGTH_MAX; i++) {
            char c = chars[random.nextInt(chars.length)];
            builder.append(c);
        }
        return builder.toString();
    }
}
