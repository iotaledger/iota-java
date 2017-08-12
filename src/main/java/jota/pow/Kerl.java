package jota.pow;

import org.bouncycastle.jcajce.provider.digest.Keccak;

import java.math.BigInteger;
import java.util.Arrays;

public class Kerl extends JCurl {

    private static final int HASH_LENGTH = 243;
    private static final int BIT_HASH_LENGTH = 384;
    private static final int BYTE_HASH_LENGTH = BIT_HASH_LENGTH / 8;

    private static final int RADIX = 3;
    private static final int MAX_TRIT_VALUE = (RADIX - 1) / 2, MIN_TRIT_VALUE = -MAX_TRIT_VALUE;
    private final Keccak.Digest384 keccak;
    private byte[] byte_state;
    private int[] trit_state;

    public Kerl() {
        super();
        this.keccak = new Keccak.Digest384();
        this.byte_state = new byte[BYTE_HASH_LENGTH];
        this.trit_state = new int[HASH_LENGTH];
    }

    public static BigInteger convertTritsToBigint(final int[] trits, final int offset, final int size) {

        BigInteger value = BigInteger.ZERO;

        for (int i = size; i-- > 0; ) {

            value = value.multiply(BigInteger.valueOf(RADIX)).add(BigInteger.valueOf(trits[offset + i]));
        }

        return value;
    }

    private static BigInteger convertBytesToBigInt(final byte[] bytes, final int offset, final int size) {

        return new BigInteger(Arrays.copyOfRange(bytes, offset, offset + size));
    }

    private static int[] convertBigintToTrits(final BigInteger value, int size) {

        int[] destination = new int[size];
        BigInteger absoluteValue = value.compareTo(BigInteger.ZERO) < 0 ? value.negate() : value;
        for (int i = 0; i < size; i++) {

            BigInteger[] divRemainder = absoluteValue.divideAndRemainder(BigInteger.valueOf(RADIX));
            int remainder = divRemainder[1].intValue();
            absoluteValue = divRemainder[0];

            if (remainder > MAX_TRIT_VALUE) {

                remainder = MIN_TRIT_VALUE;
                absoluteValue = absoluteValue.add(BigInteger.ONE);
            }
            destination[i] = remainder;
        }

        if (value.compareTo(BigInteger.ZERO) < 0) {

            for (int i = 0; i < size; i++) {

                destination[i] = -destination[i];
            }
        }

        return destination;
    }

    private static byte[] convertBigintToBytes(final BigInteger value, int size) {

        final byte[] result = new byte[BYTE_HASH_LENGTH];

        final byte[] bytes = value.toByteArray();
        int i = 0;
        while (i + bytes.length < BYTE_HASH_LENGTH) {

            result[i++] = (byte) (bytes[0] < 0 ? -1 : 0);
        }
        for (int j = bytes.length; j-- > 0; ) {

            result[i++] = bytes[bytes.length - 1 - j];
        }

        return result;
    }

    @Override
    public Kerl reset() {

        this.keccak.reset();

        return this;
    }

    @Override
    public Kerl absorb(final int[] trits, int offset, int length) {

        if (length % 243 != 0) throw new RuntimeException("Illegal length: " + length);

        do {

            //copy trits[offset:offset+length]
            System.arraycopy(trits, offset, trit_state, 0, HASH_LENGTH);

            //convert to bits
            trit_state[HASH_LENGTH - 1] = 0;
            byte[] bytes = convertBigintToBytes(convertTritsToBigint(trit_state, 0, HASH_LENGTH), BYTE_HASH_LENGTH);

            //run keccak
            keccak.update(bytes);
            offset += HASH_LENGTH;

        } while ((length -= HASH_LENGTH) > 0);

        return this;
    }

    @Override
    public int[] squeeze(final int[] trits, int offset, int length) {

        if (length % 243 != 0) throw new RuntimeException("Illegal length: " + length);

        do {

            byte_state = this.keccak.digest();
            //convert to trits
            trit_state = convertBigintToTrits(convertBytesToBigInt(byte_state, 0, BYTE_HASH_LENGTH), HASH_LENGTH);

            //copy with offset
            trit_state[HASH_LENGTH - 1] = 0;
            System.arraycopy(trit_state, 0, trits, offset, HASH_LENGTH);

            //calculate hash again
            for (int i = byte_state.length; i-- > 0; ) {

                byte_state[i] = (byte) (byte_state[i] ^ 0xFF);
            }
            keccak.update(byte_state);
            offset += HASH_LENGTH;

        } while ((length -= HASH_LENGTH) > 0);

        return trits;
    }

    /**
     * Squeezes the specified trits.
     *
     * @param trits The trits.
     * @return The squeezes trits.
     */
    public int[] squeeze(final int[] trits) {
        return squeeze(trits, 0, trits.length);
    }

    /**
     * Clones this instance.
     *
     * @return A new instance.
     */
    @Override
    public Kerl clone() {
        return new Kerl();
    }
}