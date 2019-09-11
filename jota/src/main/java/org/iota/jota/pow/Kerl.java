package org.iota.jota.pow;

import java.util.LinkedHashSet;
import java.util.Set;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.iota.jota.utils.Pair;

public class Kerl extends JCurl {

    private static final int HASH_LENGTH = 243;
    private static final int BIT_HASH_LENGTH = 384;
    private static final int BYTE_HASH_LENGTH = BIT_HASH_LENGTH / 8;

    private static final int RADIX = 3;
    private final Keccak.Digest384 keccak;
    private byte[] byte_state;
    private int[] trit_state;

    private static final int[] HALF_3 = new int[]{
            0xa5ce8964, 0x9f007669, 0x1484504f, 0x3ade00d9, 0x0c24486e, 0x50979d57, 0x79a4c702, 0x48bbae36, 0xa9f6808b, 0xaa06a805, 0xa87fabdf, 0x5e69ebef};
    private static int BYTE_LENGTH = 48;
    private static int INT_LENGTH = BYTE_LENGTH / 4;

    Kerl() {
        super(SpongeFactory.Mode.CURL_P81);
        this.keccak = new Keccak.Digest384();
        this.byte_state = new byte[BYTE_HASH_LENGTH];
        this.trit_state = new int[HASH_LENGTH];
    }

    private static final long toUnsignedLong(int i) {
        return i & 0xFFFFFFFFL;
    }

    private static int toUnsignedInt(byte x) {
        return x & 0xff;
    }

    private static int sum(int[] toSum) {
        int sum = 0;
        for (int aToSum : toSum) {
            sum += aToSum;
        }
        return sum;
    }

    public static byte[] convertTritsToBytes(final int[] trits) {
        if (trits.length != Kerl.HASH_LENGTH) {
            throw new RuntimeException("Input trits length must be " + Kerl.HASH_LENGTH + "in length");
        }
        int[] base = new int[INT_LENGTH];

        Set<Integer> setUniqueNumbers = new LinkedHashSet<>();
        for (int x : trits) {
            setUniqueNumbers.add(x);
        }
        if (setUniqueNumbers.size() == 1 && setUniqueNumbers.contains(-1)) {
            base = HALF_3.clone();
            bigintNot(base);
            bigintAdd(base, 1);
        } else {
            int size = INT_LENGTH;
            for (int i = Kerl.HASH_LENGTH - 1; i-- > 0; ) {
                { // Multiply by radix
                    int sz = size;
                    int carry = 0;

                    for (int j = 0; j < sz; j++) {
                        // full_mul
                        long v = Kerl.toUnsignedLong(base[j]) * (Kerl.toUnsignedLong(RADIX)) + Kerl.toUnsignedLong(carry);
                        carry = (int) ((v >> Integer.SIZE) & 0xFFFFFFFF);
                        base[j] = (int) (v & 0xFFFFFFFF);
                    }

                    if (carry > 0) {
                        base[sz] = carry;
                        size += 1;
                    }
                }
                final int in = trits[i] + 1;
                { // Add
                    int sz = bigintAdd(base, in);
                    if (sz > size) {
                        size = sz;
                    }
                }
            }

            if (sum(base) != 0) {
                if (bigintCmp(HALF_3, base) <= 0) {
                    // base is >= HALF_3.
                    // just do base - HALF_3
                    base = bigintSub(base, HALF_3);
                } else {
                    // we don't have a wrapping sub.
                    // so we need to be clever.
                    base = bigintSub(HALF_3, base);
                    bigintNot(base);
                    bigintAdd(base, 1);
                }
            }

        }

        byte[] out = new byte[BYTE_LENGTH];

        for (int i = 0; i < INT_LENGTH; i++) {
            out[i * 4 + 0] = (byte) ((base[INT_LENGTH - 1 - i] & 0xFF000000) >> 24);
            out[i * 4 + 1] = (byte) ((base[INT_LENGTH - 1 - i] & 0x00FF0000) >> 16);
            out[i * 4 + 2] = (byte) ((base[INT_LENGTH - 1 - i] & 0x0000FF00) >> 8);
            out[i * 4 + 3] = (byte) ((base[INT_LENGTH - 1 - i] & 0x000000FF) >> 0);
        }
        return out;
    }

    public static int[] convertBytesToTrits(byte[] bytes) {
        int[] base = new int[INT_LENGTH];
        int[] out = new int[243];
        out[Kerl.HASH_LENGTH - 1] = 0;

        if (bytes.length != BYTE_LENGTH) {
            throw new RuntimeException("Input base must be " + BYTE_LENGTH + " in length");
        }

        for (int i = 0; i < INT_LENGTH; i++) {
            base[INT_LENGTH - 1 - i] = Kerl.toUnsignedInt(bytes[i * 4]) << 24;
            base[INT_LENGTH - 1 - i] |= Kerl.toUnsignedInt(bytes[i * 4 + 1]) << 16;
            base[INT_LENGTH - 1 - i] |= Kerl.toUnsignedInt(bytes[i * 4 + 2]) << 8;
            base[INT_LENGTH - 1 - i] |= Kerl.toUnsignedInt(bytes[i * 4 + 3]);
        }

        if (bigintCmp(base, HALF_3) == 0) {
            int val = 0;
            if (base[0] > 0) {
                val = -1;
            } else if (base[0] < 0) {
                val = 1;
            }
            for (int i = 0; i < Kerl.HASH_LENGTH - 1; i++) {
                out[i] = val;
            }

        } else {
            boolean flipTrits = false;
            // See if we have a positive or negative two's complement number.
            if (Kerl.toUnsignedLong(base[INT_LENGTH - 1]) >> 31 != 0) {
                // negative value.
                bigintNot(base);
                if (bigintCmp(base, HALF_3) > 0) {
                    base = bigintSub(base, HALF_3);
                    flipTrits = true;
                } else {
                    bigintAdd(base, 1);
                    base = bigintSub(HALF_3, base);
                }
            } else {
                // positive. we need to shift right by HALF_3
                base = bigintAdd(HALF_3, base);
            }

            int size = INT_LENGTH;

            int remainder = 0;
            for (int i = 0; i < Kerl.HASH_LENGTH - 1; i++) {
                { //div_rem
                    remainder = 0;

                    for (int j = size - 1; j >= 0; j--) {
                        long lhs = (Kerl.toUnsignedLong(remainder) << 32) | Kerl.toUnsignedLong(base[j]);
                        long rhs = Kerl.toUnsignedLong(RADIX);

                        int q = (int) (lhs / rhs);
                        int r = (int) (lhs % rhs);
                        base[j] = q;
                        remainder = r;
                    }
                }
                out[i] = remainder - 1;
            }

            if (flipTrits) {
                for (int i = 0; i < out.length; i++) {
                    out[i] = -out[i];
                }
            }
        }

        return out;
    }

    private static void bigintNot(int[] base) {
        for (int i = 0; i < base.length; i++) {
            base[i] = ~base[i];
        }
    }

    private static int bigintAdd(int[] base, final int rh) {
        Pair<Integer, Boolean> res = fullAdd(base[0], rh, false);
        base[0] = res.getLow();

        int j = 1;
        while (res.getHi()) {
            res = fullAdd(base[j], 0, true);
            base[j] = res.getLow();
            j += 1;
        }

        return j;
    }

    private static int[] bigintAdd(final int[] lh, final int[] rh) {
        int[] out = new int[INT_LENGTH];
        boolean carry = false;
        Pair<Integer, Boolean> ret;
        for (int i = 0; i < INT_LENGTH; i++) {
            ret = fullAdd(lh[i], rh[i], carry);
            out[i] = ret.getLow();
            carry = ret.getHi();
        }

        if (carry) {
            throw new RuntimeException("Exceeded max value.");
        }

        return out;
    }

    private static int bigintCmp(final int[] lh, final int[] rh) {
        for (int i = INT_LENGTH - 1; i >= 0; i--) {
            int ret = Long.compare(Kerl.toUnsignedLong(lh[i]), Kerl.toUnsignedLong(rh[i]));
            if (ret != 0) {
                return ret;
            }
        }
        return 0;
    }

    private static int[] bigintSub(final int[] lh, final int[] rh) {
        int[] out = new int[INT_LENGTH];
        boolean noborrow = true;
        Pair<Integer, Boolean> ret;
        for (int i = 0; i < INT_LENGTH; i++) {
            ret = fullAdd(lh[i], ~rh[i], noborrow);
            out[i] = ret.getLow();
            noborrow = ret.getHi();
        }

        if (!noborrow) {
            throw new RuntimeException("noborrow");
        }

        return out;
    }

    private static Pair<Integer, Boolean> fullAdd(final int ia, final int ib, final boolean carry) {
        long a = Kerl.toUnsignedLong(ia);
        long b = Kerl.toUnsignedLong(ib);

        long v = a + b;
        long l = v >> 32;
        long r = v & 0xFFFFFFFF;
        boolean carry1 = l != 0;

        if (carry) {
            v = r + 1;
        }
        l = (v >> 32) & 0xFFFFFFFF;
        r = v & 0xFFFFFFFF;
        boolean carry2 = l != 0;

        return new Pair<>((int) r, carry1 || carry2);
    }

    @Override
    public Kerl reset() {

        this.keccak.reset();

        return this;
    }

    @Override
    public Kerl absorb(final int[] trits, int offset, int length) {

        if (length % 243 != 0) {
            throw new RuntimeException("Illegal length: " + length);
        }

        do {

            //copy trits[offset:offset+length]
            System.arraycopy(trits, offset, trit_state, 0, HASH_LENGTH);

            //convert to bits
            trit_state[HASH_LENGTH - 1] = 0;
            byte[] bytes = convertTritsToBytes(trit_state);

            //run keccak
            keccak.update(bytes);
            offset += HASH_LENGTH;

        } while ((length -= HASH_LENGTH) > 0);

        return this;
    }

    @Override
    public int[] squeeze(final int[] trits, int offset, int length) {

        if (length % 243 != 0) {
            throw new RuntimeException("Illegal length: " + length);
        }

        do {

            byte_state = this.keccak.digest();
            //convert to trits
            trit_state = convertBytesToTrits(byte_state);

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