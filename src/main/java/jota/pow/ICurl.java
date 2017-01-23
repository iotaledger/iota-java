package jota.pow;

/**
 * Created by Adrian on 07.01.2017.
 */
public interface ICurl {
    JCurl absorb(final int[] trits, int offset, int length);

    JCurl absorb(final int[] trits);

    int[] squeeze(final int[] trits, int offset, int length);

    int[] squeeze(final int[] trits);

    JCurl transform();

    JCurl reset();

    int[] getState();

    void setState(int[] state);
}
