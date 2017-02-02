package jota.error;

/**
 * Created by Adrian on 27.12.2016.
 */
public class InvalidBundleException extends BaseException {

    public InvalidBundleException() {
        super("Invalid Bundle");
    }

    public InvalidBundleException(String msg) {
        super(msg);
    }
}
