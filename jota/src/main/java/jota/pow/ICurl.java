package jota.pow;

/**
 * This interface abstracts the curl hashing algorithm.
 *
 * @author Adrian
 */
public interface ICurl {

    /**
     * Absorbs the specified trits.
     *
     * @param trits  The trits.
     * @param offset The offset to start from.
     * @param length The length.
     * @return The ICurl instance (used for method chaining).
     */
    ICurl absorb(final int[] trits, int offset, int length);

    /**
     * Absorbs the specified trits.
     *
     * @param trits The trits.
     * @return The ICurl instance (used for method chaining).
     */
    ICurl absorb(final int[] trits);

    /**
     * Squeezes the specified trits.
     *
     * @param trits  The trits.
     * @param offset The offset to start from.
     * @param length The length.
     * @return The squeezed trits.
     */
    int[] squeeze(final int[] trits, int offset, int length);

    /**
     * Squeezes the specified trits.
     *
     * @param trits The trits.
     * @return The squeezed trits.
     */
    int[] squeeze(final int[] trits);

    /**
     * Transforms this instance.
     *
     * @return The ICurl instance (used for method chaining).
     */
    ICurl transform();

    /**
     * Resets this state.
     *
     * @return The ICurl instance (used for method chaining).
     */
    ICurl reset();

    /**
     * Gets or sets the state.
     *
     * @return The stae.
     */
    int[] getState();

    /**
     * Sets or sets the state.
     *
     * @param state The state.
     */
    void setState(int[] state);

    /**
     * Clones this instance.
     *
     * @return A new instance.
     */
    ICurl clone();
}
