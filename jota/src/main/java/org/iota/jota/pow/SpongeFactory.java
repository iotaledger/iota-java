package org.iota.jota.pow;

import java.util.Objects;

/**
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