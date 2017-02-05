package jota.error;

/**
 * Created by Adrian on 22.01.2017.
 */
public class NoNodeInfoException extends BaseException {

    public NoNodeInfoException() {
        super("Node info could not be retrieved");
    }
}