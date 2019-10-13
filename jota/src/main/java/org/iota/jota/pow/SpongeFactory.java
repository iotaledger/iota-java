package org.iota.jota.pow;

import java.util.Objects;

/**
 * Factory for creating an instance of a hashing function implementation,
 * based on the passed-in {@link SpongeFactory.Mode}.
 *
 * Currently supported mode (hashing functions):
 * <ol>
 *     <li>Curl P81</li>
 *     <li>Curl P27</li>
 *     <li>Kerl</li>
 * </ol>
 *
 * @see <a href="https://github.com/iotaledger/iota.curl.java">IOTA CURL.</a>
 * @see <a href="https://github.com/iotaledger/kerl">IOTA Kerl.</a>
 *
 * Created by paul on 7/27/17.
 */
public class SpongeFactory {

    private SpongeFactory() {
        throw new AssertionError("Do not instantiate!");
    }

    public static ICurl create(Mode mode) {
        Objects.requireNonNull(mode, "Mode must not be null");
        switch (mode) {
            case CURL_P81:
            case CURL_P27:
                return new JCurl(mode);
            case KERL:
                return new Kerl();
            default:
                throw new IllegalArgumentException(mode + " is currently not supported.");
        }
    }

    public enum Mode {
        CURL_P81,
        CURL_P27,
        KERL
    }
}
