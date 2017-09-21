package jota.error;

/**
 * This exception occurs when invalid trytes is provided.
 *
 * @author Adrian
 */
public class InvalidTrytesException extends BaseException {

    /**
     * Initializes a new instance of the InvalidTrytesException.
     */
    public InvalidTrytesException() {
        super("Invalid input trytes");
    }
}