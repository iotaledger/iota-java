package jota.error;

/**
 * @author Adrian
 */
public class InvalidBundleException extends BaseException {

    public InvalidBundleException() {
        super("Invalid Bundle");
    }

    public InvalidBundleException(String msg) {
        super(msg);
    }
}
