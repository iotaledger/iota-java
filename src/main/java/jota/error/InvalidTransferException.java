package jota.error;

/**
 * This exception occurs when an invalid transfer is provided.
 *
 * @author pinpong
 */
public class InvalidTransferException extends BaseException {

    /**
     * Initializes a new instance of the InvalidTransferException.
     */
    public InvalidTransferException() {
        super("Invalid Transfer!");
    }
}
