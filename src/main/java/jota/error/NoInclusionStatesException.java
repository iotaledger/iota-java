package jota.error;

/**
 * This exception occurs when it not possible to get a inclusion state.
 *
 * @author Adrian
 */
public class NoInclusionStatesException extends BaseException {

    /**
     * Initializes a new instance of the NoInclusionStatesException.
     */
    public NoInclusionStatesException() {
        super("No inclusion states for the provided seed");
    }
}
