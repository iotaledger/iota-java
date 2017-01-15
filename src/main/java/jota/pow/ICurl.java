package jota.pow;

/**
 * Created by Adrian on 07.01.2017.
 */
public interface ICurl {
    JCurl absorbb(final int[] trits, int offset, int length);

    JCurl absorbb(final int[] trits);

    int[] squeezee(final int[] trits, int offset, int length);

    int[] squeezee(final int[] trits);

    JCurl transform();

    JCurl reset();

    int[] getState();

    void setState(int[] state);
}
