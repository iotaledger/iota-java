package jota.pow;

/**
 * This interface abstracts the curl hashing algorithm
 * @author Adrian
 */
public interface ICurl {

    /**
     * Absorbs the specified trits.
     *
     * @param trits  The trits.
     * @param offset The offset to start from.
     * @param length The length.
     * @return
     */
    ICurl absorb(final int[] trits, int offset, int length);

    /**
     * Absorbs the specified trits.
     *
     * @param trits The trits.
     * @return
     */
    ICurl absorb(final int[] trits);

    /**
     * Squeezes the specified trits.
     * @param trits The trits.
     * @param offset The offset to start from.
     * @param length The length.
     * @return
     */
    int[] squeeze(final int[] trits, int offset, int length);

    /**
     * Squeezes the specified trits.
     * @param trits The trits.
     * @return
     */
    int[] squeeze(final int[] trits);

    /**
     * Transforms this instance.
     * @return
     */
    ICurl transform();

    /**
     * Resets this state.
     * @return
     */
    ICurl reset();

    /**
     * Gets or sets the state.
     * @return
     */
    int[] getState();

    /**
     * Sets or sets the state.
     * @param state
     */
    void setState(int[] state);

    /**
     * Clones this instance.
     * @return
     */
    ICurl clone();
}
