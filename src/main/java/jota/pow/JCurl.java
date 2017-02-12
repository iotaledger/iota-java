package jota.pow;

/**
 * (c) 2016 Come-from-Beyond
 *
 * JCurl belongs to the sponge function family.
 */
public class JCurl implements ICurl {

    public static final int HASH_LENGTH = 243;
    private static final int STATE_LENGTH = 3 * HASH_LENGTH;

    private static final int NUMBER_OF_ROUNDS = 27;
    private static final int[] TRUTH_TABLE = {1, 0, -1, 1, -1, 0, -1, 1, 0};

    private int[] state = new int[STATE_LENGTH];

    public JCurl absorb(final int[] trits, int offset, int length) {

        do {
            System.arraycopy(trits, offset, state, 0, length < HASH_LENGTH ? length : HASH_LENGTH);
            transform();
            offset += HASH_LENGTH;
        } while ((length -= HASH_LENGTH) > 0);

        return this;
    }


    public JCurl absorb(final int[] trits) {
        return absorb(trits, 0, trits.length);
    }

    public JCurl transform() {

        final int[] scratchpad = new int[STATE_LENGTH];
        int scratchpadIndex = 0;
        for (int round = 0; round < NUMBER_OF_ROUNDS; round++) {
            System.arraycopy(state, 0, scratchpad, 0, STATE_LENGTH);
            for (int stateIndex = 0; stateIndex < STATE_LENGTH; stateIndex++) {
                state[stateIndex] = TRUTH_TABLE[scratchpad[scratchpadIndex] + scratchpad[scratchpadIndex += (scratchpadIndex < 365 ? 364 : -365)] * 3 + 4];
            }
        }
        return this;
    }

    public JCurl reset() {
        for (int stateIndex = 0; stateIndex < STATE_LENGTH; stateIndex++) {
            state[stateIndex] = 0;
        }
        return this;
    }

    public int[] squeeze(final int[] trits, int offset, int length) {

        do {
            System.arraycopy(state, 0, trits, offset, length < HASH_LENGTH ? length : HASH_LENGTH);
            transform();
            offset += HASH_LENGTH;
        } while ((length -= HASH_LENGTH) > 0);

        return state;
    }

    public int[] squeeze(final int[] trits) {
        return squeeze(trits, 0, trits.length);
    }

    public int[] getState() {
        return state;
    }

    public void setState(int[] state) {
        this.state = state;
    }

    @Override
    public ICurl clone() {
        return new JCurl();
    }
}
