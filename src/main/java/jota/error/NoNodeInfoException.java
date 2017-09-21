package jota.error;

/**
 * This exception occurs when its not possible to get node info.
 *
 * @author Adrian
 */
public class NoNodeInfoException extends BaseException {

    /**
     * Initializes a new instance of the NoNodeInfoException.
     */
    public NoNodeInfoException() {
        super("Node info could not be retrieved");
    }
}