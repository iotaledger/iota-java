package jota.utils;

import jota.model.Transaction;
import jota.pow.Curl;

import java.util.Arrays;

public class Converter {

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

    public static int[] trits(final String trytes) {

        final int[] trits = new int[trytes.length() * NUMBER_OF_TRITS_IN_A_TRYTE];
        for (int i = 0; i < trytes.length(); i++) {
            System.arraycopy(TRYTE_TO_TRITS_MAPPINGS[Constants.TRYTE_ALPHABET.indexOf(trytes.charAt(i))], 0, trits, i * NUMBER_OF_TRITS_IN_A_TRYTE, NUMBER_OF_TRITS_IN_A_TRYTE);
        }

        return trits;
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
            destination[offset + i] = remainder;
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

    public static void increment(final int[] trits, final int size) {

        for (int i = 0; i < size; i++) {
            if (++trits[i] > Converter.MAX_TRIT_VALUE) {
                trits[i] = Converter.MIN_TRIT_VALUE;
            } else {
                break;
            }
        }
    }

    public static Transaction transactionObject(String trytes) {
        if (trytes == null) return null;

        // validity check
        for (int i = 2279; i < 2295; i++) {
            if (trytes.charAt(i) != '9') {
                return null;
            }
        }
        int[] transactionTrits = Converter.trits(trytes);
        int[] hash = new int[90];

        Curl curl = new Curl();

        // generate the correct transaction hash
        curl.reset();
        curl.absorb(transactionTrits, 0, transactionTrits.length);
        curl.squeeze(hash, 0, hash.length);

        Transaction trx = new Transaction();

        trx.setHash(Converter.trytes(hash));
        trx.setSignatureFragments(trytes.substring(0, 2187));
        trx.setAddress(trytes.substring(2187, 2268));
        trx.setValue("" + Converter.value(Arrays.copyOfRange(transactionTrits, 6804, 6837)));
        trx.setTag(trytes.substring(2295, 2322));
        trx.setTimestamp("" + Converter.value(Arrays.copyOfRange(transactionTrits, 6966, 6993)));
        trx.setCurrentIndex("" + Converter.value(Arrays.copyOfRange(transactionTrits, 6993, 7020)));
        trx.setLastIndex("" + Converter.value(Arrays.copyOfRange(transactionTrits, 7020, 7047)));
        trx.setBundle(trytes.substring(2349, 2430));
        trx.setTrunkTransaction(trytes.substring(2430, 2511));
        trx.setBranchTransaction(trytes.substring(2511, 2592));
        trx.setNonce(trytes.substring(2592, 2673));

        return trx;
    }
}