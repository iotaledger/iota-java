package jota.pow;

/**
 * (c) 2016 Come-from-Beyond
 * <p>
 * JCurl belongs to the sponge function family.
 */
public class JCurl implements ICurl {

    /**
     * The hash length.
     */
    public static final int HASH_LENGTH = 243;
    private static final int STATE_LENGTH = 3 * HASH_LENGTH;

    private static final int NUMBER_OF_ROUNDS = 27;
    private static final int[] TRUTH_TABLE = {1, 0, -1, 1, -1, 0, -1, 1, 0};

    private int[] state = new int[STATE_LENGTH];

    /**
     * Absorbs the specified trits.
     *
     * @param trits  The trits.
     * @param offset The offset to start from.
     * @param length The length.
     * @return The ICurl instance (used for method chaining).
     */
    public JCurl absorb(final int[] trits, int offset, int length) {

        do {
            System.arraycopy(trits, offset, state, 0, length < HASH_LENGTH ? length : HASH_LENGTH);
            transform();
            offset += HASH_LENGTH;
        } while ((length -= HASH_LENGTH) > 0);

        return this;
    }

    /**
     * Absorbs the specified trits.
     *
     * @param trits The trits.
     * @return The ICurl instance (used for method chaining).
     */
    public JCurl absorb(final int[] trits) {
        return absorb(trits, 0, trits.length);
    }

    /**
     * Transforms this instance.
     *
     * @return The ICurl instance (used for method chaining).
     */
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

    /**
     * Resets this state.
     *
     * @return The ICurl instance (used for method chaining).
     */
    public JCurl reset() {
        for (int stateIndex = 0; stateIndex < STATE_LENGTH; stateIndex++) {
            state[stateIndex] = 0;
        }
        return this;
    }

    /**
     * Squeezes the specified trits.
     *
     * @param trits  The trits.
     * @param offset The offset to start from.
     * @param length The length.
     * @return The squeezes trits.
     */
    public int[] squeeze(final int[] trits, int offset, int length) {

        do {
            System.arraycopy(state, 0, trits, offset, length < HASH_LENGTH ? length : HASH_LENGTH);
            transform();
            offset += HASH_LENGTH;
        } while ((length -= HASH_LENGTH) > 0);

        return state;
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
     * Gets the states.
     *
     * @return The state.
     */
    public int[] getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state The states.
     */
    public void setState(int[] state) {
        this.state = state;
    }

    /**
     * Clones this instance.
     *
     * @return A new instance.
     */
    @Override
    public ICurl clone() {
        return new JCurl();
    }
}
