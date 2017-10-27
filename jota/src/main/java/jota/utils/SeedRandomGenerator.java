package jota.utils;

import java.security.SecureRandom;

/**
 * This class allows to create a secure random generated seed.
 *
 * @author pinpong
 */
public class SeedRandomGenerator {


    /**
     * Generate a new seed.
     *
     * @return Random generated seed.
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
