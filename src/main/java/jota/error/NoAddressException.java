package jota.error;

/**
 * Created by Adrian on 22.01.2017.
 */
public class NoAddressException extends BaseException {

    public NoAddressException() {
        super("Not address found for the provided seed");
    }
}