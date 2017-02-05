package jota.utils;

import jota.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Converter {

    private static final Logger log = LoggerFactory.getLogger(Converter.class);

    private static final int RADIX = 3;
    private static final int MAX_TRIT_VALUE = (RADIX - 1) / 2, MIN_TRIT_VALUE = -MAX_TRIT_VALUE;

    private static final int NUMBER_OF_TRITS_IN_A_BYTE = 5;
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

    public static int[] convertToIntArray(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i);
        }
        return ret;
    }

    public static int[] trits(final String trytes, int length) {
        int[] trits = trits(trytes);

        List<Integer> tritsList = new LinkedList<>();

        for(int i : trits)
            tritsList.add(i);

        while(tritsList.size() < length)
            tritsList.add(0);

        return convertToIntArray(tritsList);
    }
    public static int[] tritsString(final String trytes){
        int[] d = new int[3 * trytes.length()];
        for (int i = 0; i < trytes.length(); i++) {
            System.arraycopy(TRYTE_TO_TRITS_MAPPINGS[Constants.TRYTE_ALPHABET.indexOf(trytes.charAt(i))], 0, d, i * NUMBER_OF_TRITS_IN_A_TRYTE, NUMBER_OF_TRITS_IN_A_TRYTE);
        }
        return d;
    }

    public static int[] trits(final String trytes) {
        final List<Integer> trits = new LinkedList<>();
        if (InputValidator.isValue(trytes)) {

            long value = Long.parseLong(trytes);

            long absoluteValue = value < 0 ? -value : value;

            int position = 0;

            while (absoluteValue > 0) {

                int remainder = (int) (absoluteValue % RADIX);
                absoluteValue /= RADIX;

                if (remainder > MAX_TRIT_VALUE) {
                    remainder = MIN_TRIT_VALUE;
                    absoluteValue++;
                }

                trits.add(position++,remainder);
            }
            if (value < 0) {
                for (int i = 0; i < trits.size(); i++) {
                    trits.set(i,-trits.get(i));
                }
            }
        } else {
            int[] d = new int[3 * trytes.length()];
            for (int i = 0; i < trytes.length(); i++) {
                System.arraycopy(TRYTE_TO_TRITS_MAPPINGS[Constants.TRYTE_ALPHABET.indexOf(trytes.charAt(i))], 0, d, i * NUMBER_OF_TRITS_IN_A_TRYTE, NUMBER_OF_TRITS_IN_A_TRYTE);
            }
            return d;
        }
        return convertToIntArray(trits);
    }

    public static void copyTrits(final long value, final int[] destination, final int offset, final int size) {

        long absoluteValue = value < 0 ? -value : value;
        for (int i = 0; i < size; i++) {

            int remainder = (int) (absoluteValue % RADIX);
            absoluteValue /= RADIX;
            if (remainder > MAX_TRIT_VALUE) {

                remainder = MIN_TRIT_VALUE;
                absoluteValue++;
            }
            destination[offset +i ] = remainder;
        }

        if (value < 0) {

            for (int i = 0; i < size; i++) {
                destination[offset + i] = -destination[offset + i];
            }
        }
    }

    public static int[] copyTrits(final String input, final int[] destination) {
        for (int i = 0; i < input.length(); i++) {
            int index = Constants.TRYTE_ALPHABET.indexOf(input.charAt(i));
            destination[i * 3] = TRYTE_TO_TRITS_MAPPINGS[index][0];
            destination[i * 3 + 1] = TRYTE_TO_TRITS_MAPPINGS[index][1];
            destination[i * 3 + 2] = TRYTE_TO_TRITS_MAPPINGS[index][2];
        }
        return destination;
    }

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

    public static int tryteValue(final int[] trits, final int offset) {
        return trits[offset] + trits[offset + 1] * 3 + trits[offset + 2] * 9;
    }

    public static int value(final int[] trits) {
        int value = 0;

        for (int i = trits.length; i-- > 0; ) {
            value = value * 3 + trits[i];
        }
        return value;
    }

    public static long longValue(final int[] trits) {
        long value = 0;

        for (int i = trits.length; i-- > 0; ) {
            value = value * 3 + trits[i];
        }
        return value;
    }

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
