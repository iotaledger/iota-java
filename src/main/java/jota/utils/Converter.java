package jota.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides a set of utility methods to are used to convert between different formats.
 */
public class Converter {

    public static final int HIGH_INTEGER_BITS = 0xFFFFFFFF;
    public static final long HIGH_LONG_BITS = 0xFFFFFFFFFFFFFFFFL;
    /**
     * The radix
     */
    private static final int RADIX = 3;
    /**
     * The maximum trit value
     */
    private static final int MAX_TRIT_VALUE = (RADIX - 1) / 2, MIN_TRIT_VALUE = -MAX_TRIT_VALUE;
    /**
     * The number of trits in a byte
     */
    private static final int NUMBER_OF_TRITS_IN_A_BYTE = 5;
    /**
     * The number of trits in a tryte
     */
    private static final int NUMBER_OF_TRITS_IN_A_TRYTE = 3;
    private static final int[][] BYTE_TO_TRITS_MAPPINGS = new int[243][];
    private static final int[][] TRYTE_TO_TRITS_MAPPINGS = new int[27][];

    static {

        final int[] trits = new int[NUMBER_OF_TRITS_IN_A_BYTE];

        for (int i = 0; i < 243; i++) {
            BYTE_TO_TRITS_MAPPINGS[i] = Arrays.copyOf(trits, NUMBER_OF_TRITS_IN_A_BYTE);
            increment(trits, NUMBER_OF_TRITS_IN_A_BYTE);
        }

        for (int i = 0; i < 27; i++) {
            TRYTE_TO_TRITS_MAPPINGS[i] = Arrays.copyOf(trits, NUMBER_OF_TRITS_IN_A_TRYTE);
            increment(trits, NUMBER_OF_TRITS_IN_A_TRYTE);
        }
    }

    /**
     * Converts the specified trits array to bytes.
     *
     * @param trits  The trits.
     * @param offset The offset to start from.
     * @param size   The size.
     * @return The bytes.
     */
    public static byte[] bytes(final int[] trits, final int offset, final int size) {

        final byte[] bytes = new byte[(size + NUMBER_OF_TRITS_IN_A_BYTE - 1) / NUMBER_OF_TRITS_IN_A_BYTE];
        for (int i = 0; i < bytes.length; i++) {

            int value = 0;
            for (int j = (size - i * NUMBER_OF_TRITS_IN_A_BYTE) < 5 ? (size - i * NUMBER_OF_TRITS_IN_A_BYTE) : NUMBER_OF_TRITS_IN_A_BYTE; j-- > 0; ) {
                value = value * RADIX + trits[offset + i * NUMBER_OF_TRITS_IN_A_BYTE + j];
            }
            bytes[i] = (byte) value;
        }

        return bytes;
    }

    public static byte[] bytes(final int[] trits) {
        return bytes(trits, 0, trits.length);
    }

    /**
     * Gets the trits from the specified bytes and stores it into the provided trits array.
     *
     * @param bytes The bytes.
     * @param trits The trits.
     */
    public static void getTrits(final byte[] bytes, final int[] trits) {

        int offset = 0;
        for (int i = 0; i < bytes.length && offset < trits.length; i++) {
            System.arraycopy(BYTE_TO_TRITS_MAPPINGS[bytes[i] < 0 ? (bytes[i] + BYTE_TO_TRITS_MAPPINGS.length) : bytes[i]], 0, trits, offset, trits.length - offset < NUMBER_OF_TRITS_IN_A_BYTE ? (trits.length - offset) : NUMBER_OF_TRITS_IN_A_BYTE);
            offset += NUMBER_OF_TRITS_IN_A_BYTE;
        }
        while (offset < trits.length) {
            trits[offset++] = 0;
        }
    }

    public static int[] convertToIntArray(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i);
        }
        return ret;
    }

    /**
     * Converts the specified trinary encoded string into a trits array of the specified length.
     *
     * @param trytes The trytes.
     * @param length The length
     * @return A trits array.
     */
    public static int[] trits(final String trytes, int length) {
        int[] trits = trits(trytes);
        return Arrays.copyOf(trits, length);
    }

    /**
     * Converts the specified trinary encoded string into a trits array of the specified length.
     *
     * @param trytes The trytes.
     * @param length The length.
     * @return A trits array.
     */
    public static int[] trits(final long trytes, int length) {
        int[] trits = trits(trytes);
        return Arrays.copyOf(trits, length);
    }

    /**
     * Converts the specified trinary encoded trytes string to trits.
     *
     * @param trytes The trytes.
     * @return A trits array.
     */
    @Deprecated
    public static int[] tritsString(final String trytes) {
        return trits(trytes);
    }

    /**
     * Converts trytes into trits.
     *
     * @param trytes The trytes to be converted.
     * @return Array of trits.
     **/
    public static int[] trits(final long trytes) {
        final List<Integer> trits = new LinkedList<>();
        long absoluteValue = trytes < 0 ? -trytes : trytes;

        int position = 0;

        while (absoluteValue > 0) {

            int remainder = (int) (absoluteValue % RADIX);
            absoluteValue /= RADIX;

            if (remainder > MAX_TRIT_VALUE) {
                remainder = MIN_TRIT_VALUE;
                absoluteValue++;
            }

            trits.add(position++, remainder);
        }
        if (trytes < 0) {
            for (int i = 0; i < trits.size(); i++) {
                trits.set(i, -trits.get(i));
            }
        }
        return convertToIntArray(trits);
    }

    /**
     * Converts trytes into trits.
     *
     * @param trytes The trytes to be converted.
     * @return Array of trits.
     **/
    public static int[] trits(final String trytes) {
        int[] d = new int[3 * trytes.length()];
        for (int i = 0; i < trytes.length(); i++) {
            System.arraycopy(TRYTE_TO_TRITS_MAPPINGS[Constants.TRYTE_ALPHABET.indexOf(trytes.charAt(i))], 0, d, i * NUMBER_OF_TRITS_IN_A_TRYTE, NUMBER_OF_TRITS_IN_A_TRYTE);
        }
        return d;
    }

    /**
     * Copies the trits from the input string into the destination array
     *
     * @param input       The input String.
     * @param destination The destination array.
     * @return The destination.
     */
    public static int[] copyTrits(final String input, final int[] destination) {
        for (int i = 0; i < input.length(); i++) {
            int index = Constants.TRYTE_ALPHABET.indexOf(input.charAt(i));
            destination[i * 3] = TRYTE_TO_TRITS_MAPPINGS[index][0];
            destination[i * 3 + 1] = TRYTE_TO_TRITS_MAPPINGS[index][1];
            destination[i * 3 + 2] = TRYTE_TO_TRITS_MAPPINGS[index][2];
        }
        return destination;
    }

    /**
     * Converts trites to trytes.
     *
     * @param trits  Teh trits to be converted.
     * @param offset The offset to start from.
     * @param size   The size.
     * @return The trytes.
     **/
    public static String trytes(final int[] trits, final int offset, final int size) {

        StringBuilder trytes = new StringBuilder();
        for (int i = 0; i < (size + NUMBER_OF_TRITS_IN_A_TRYTE - 1) / NUMBER_OF_TRITS_IN_A_TRYTE; i++) {

            int j = trits[offset + i * 3] + trits[offset + i * 3 + 1] * 3 + trits[offset + i * 3 + 2] * 9;
            if (j < 0) {

                j += Constants.TRYTE_ALPHABET.length();
            }
            trytes.append(Constants.TRYTE_ALPHABET.charAt(j));
        }
        return trytes.toString();
    }

    public static String trytes(final int[] trits) {
        return trytes(trits, 0, trits.length);
    }

    /**
     * Converts the specified trits array to trytes in integer representation.
     *
     * @param trits  The trits.
     * @param offset The offset to start from.
     * @return The value.
     */
    public static int tryteValue(final int[] trits, final int offset) {
        return trits[offset] + trits[offset + 1] * 3 + trits[offset + 2] * 9;
    }

    /**
     * Converts the specified trits to its corresponding integer value.
     *
     * @param trits The trits.
     * @return The value.
     */
    public static int value(final int[] trits) {
        int value = 0;

        for (int i = trits.length; i-- > 0; ) {
            value = value * 3 + trits[i];
        }
        return value;
    }

    /**
     * Converts the specified trits to its corresponding integer value.
     *
     * @param trits The trits.
     * @return The value.
     */
    public static long longValue(final int[] trits) {
        long value = 0;

        for (int i = trits.length; i-- > 0; ) {
            value = value * 3 + trits[i];
        }
        return value;
    }

    /**
     * Increments the specified trits.
     *
     * @param trits The trits.
     * @param size  The size.
     */
    public static void increment(final int[] trits, final int size) {

        for (int i = 0; i < size; i++) {
            if (++trits[i] > Converter.MAX_TRIT_VALUE) {
                trits[i] = Converter.MIN_TRIT_VALUE;
            } else {
                break;
            }
        }
    }

}
