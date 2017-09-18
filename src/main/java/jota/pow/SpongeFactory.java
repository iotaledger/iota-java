package jota.pow;

/**
 * Created by paul on 7/27/17.
 */
public abstract class SpongeFactory {
    public static ICurl create(Mode mode) {
        switch (mode) {
            case CURL:
                return new JCurl();
            case KERL:
                return new Kerl();
            case BCURLT:
                return new JCurl(true);
            default:
                return null;
        }
    }

    public enum Mode {
        CURL,
        KERL,
        BCURLT
    }
}