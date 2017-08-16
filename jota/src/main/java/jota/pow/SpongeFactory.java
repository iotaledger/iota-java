package jota.pow;

/**
 * Created by paul on 7/27/17.
 */
public abstract class SpongeFactory {
    public static ICurl create(Mode mode) {
        switch (mode) {
            case CURLP81:
                return new JCurl(mode);
            case CURLP27:
                return new JCurl(mode);
            case KERL:
                return new Kerl();
            default:
                return null;
        }
    }

    public enum Mode {
        CURLP81,
        CURLP27,
        KERL,
        //BCURLT
    }
}