package jota.error;

/**
 * This exceptions occurs if an invalid bundle was found or provided.
 *
 * @author Adrian
 */
public class InvalidBundleException extends BaseException {

    /**
     * Initializes a new instance of the InvalidBundleException.
     */
    public InvalidBundleException() {
        super("Invalid Bundle");
    }

    /**
     * Initializes a new instance of the InvalidBundleException.
     */
    public InvalidBundleException(String msg) {
        super(msg);
    }
}
