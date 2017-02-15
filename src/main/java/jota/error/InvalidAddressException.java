package jota.error;

/**
 * This exception occurs when an invalid address is provided.
 *
 * @author Adrian
 */
public class InvalidAddressException extends BaseException {

    /**
     * Initializes a new instance of the InvalidAddressException.
     */
    public InvalidAddressException() {
        super("Invalid Address!");
    }
}
