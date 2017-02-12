package jota.error;

/**
 * @author Adrian
 */
public class NoNodeInfoException extends BaseException {

    public NoNodeInfoException() {
        super("Node info could not be retrieved");
    }
}