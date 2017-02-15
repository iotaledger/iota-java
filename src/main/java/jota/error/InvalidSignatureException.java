package jota.error;

/**
 * This exception occurs when an invalid signature is encountered.
 *
 * @author Adrian
 */
public class InvalidSignatureException extends BaseException {

    /**
     * Initializes a new instance of the InvalidSignatureException.
     */
    public InvalidSignatureException() {
        super("Invalid Signatures!");
    }
}
