package jota.pow;

/**
 * Created by Adrian on 07.01.2017.
 */
public interface ICurl {
    public JCurl absorbb(final int[] trits, int offset, int length);
    public JCurl absorbb(final int[] trits);
    public int[] squeezee(final int[] trits, int offset, int length);
    public int[] squeezee(final int[] trits);
    public JCurl transform();
    public JCurl reset();
    public int[] getState();
    public void setState(int[] state);
    }
