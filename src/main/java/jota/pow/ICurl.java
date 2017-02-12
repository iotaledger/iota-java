package jota.pow;

/**
 * @author Adrian
 */
public interface ICurl {
    ICurl absorb(final int[] trits, int offset, int length);

    ICurl absorb(final int[] trits);

    int[] squeeze(final int[] trits, int offset, int length);

    int[] squeeze(final int[] trits);

    ICurl transform();

    ICurl reset();

    int[] getState();

    void setState(int[] state);

    ICurl clone();
}
