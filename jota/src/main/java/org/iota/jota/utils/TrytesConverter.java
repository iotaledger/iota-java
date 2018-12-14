package org.iota.jota.utils;

import org.iota.jota.error.ArgumentException;

/**
 * This class allows to convert between ASCII and tryte encoded strings.
 *
 * @author pinpong
 */
public class TrytesConverter {

    /**
     * Conversion of ASCII encoded bytes to trytes.
     * Input is a string (can be stringified JSON object), return value is Trytes
     *
     * How the conversion works:
     * 2 Trytes === 1 Byte
     * There are a total of 27 different tryte values: 9ABCDEFGHIJKLMNOPQRSTUVWXYZ
     *
     * 1. We get the decimal value of an individual ASCII character
     * 2. From the decimal value, we then derive the two tryte values by basically calculating the tryte equivalent (e.g. 100 === 19 + 3 * 27)
     * a. The first tryte value is the decimal value modulo 27 (27 trytes)
     * b. The second value is the remainder (decimal value - first value), divided by 27
     * 3. The two values returned from Step 2. are then input as indices into the available values list ('9ABCDEFGHIJKLMNOPQRSTUVWXYZ') to get the correct tryte value
     *
     *
     * EXAMPLE
     *
     * Lets say we want to convert the ASCII character "Z".
     * 1. 'Z' has a decimal value of 90.
     * 2. 90 can be represented as 9 + 3 * 27. To make it simpler:
     * a. First value: 90 modulo 27 is 9. This is now our first value
     * b. Second value: (90 - 9) / 27 is 3. This is our second value.
     * 3. Our two values are now 9 and 3. To get the tryte value now we simply insert it as indices into '9ABCDEFGHIJKLMNOPQRSTUVWXYZ'
     * a. The first tryte value is '9ABCDEFGHIJKLMNOPQRSTUVWXYZ'[9] === "I"
     * b. The second tryte value is '9ABCDEFGHIJKLMNOPQRSTUVWXYZ'[3] === "C"
     * Our tryte pair is "IC"
     *
     * @param inputString The input String.
     * @return The ASCII char "Z" is represented as "IC" in trytes.
     */
    public static String asciiToTrytes(String inputString) {

        StringBuilder trytes = new StringBuilder();

        for (int i = 0; i < inputString.length(); i++) {

            char asciiValue = inputString.charAt(i);

            // If not recognizable ASCII character, replace with space
            if (asciiValue > 255) {
                asciiValue = 32;
            }

            int firstValue = asciiValue % 27;
            int secondValue = (asciiValue - firstValue) / 27;

            String trytesValue = String.valueOf(Constants.TRYTE_ALPHABET.charAt(firstValue) + String.valueOf(Constants.TRYTE_ALPHABET.charAt(secondValue)));

            trytes.append(trytesValue);
        }

        return trytes.toString();
    }

    /**
     * Converts Trytes of even length to an ASCII string.
     * Reverse operation from the asciiToTrytes 
     * 2 Trytes == 1 Byte
     * @param inputTrytes the trytes we want to convert
     * @return an ASCII string or null when the inputTrytes are uneven
     * @throws ArgumentException When the trytes in the string are an odd number
     */
    public static String trytesToAscii(String inputTrytes) throws ArgumentException {

        // If input length is odd, return null
        if (inputTrytes.length() % 2 != 0) {
            throw new ArgumentException("Odd amount of trytes supplied");
        }

        StringBuilder string = new StringBuilder();

        for (int i = 0; i < inputTrytes.length(); i += 2) {
            // get a trytes pair

            int firstValue = Constants.TRYTE_ALPHABET.indexOf(inputTrytes.charAt(i));
            int secondValue = Constants.TRYTE_ALPHABET.indexOf(inputTrytes.charAt(i + 1));

            int decimalValue = firstValue + secondValue * 27;

            String character = Character.toString((char) decimalValue);
            string.append(character);
        }

        return string.toString();
    }
}