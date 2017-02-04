package jota.error;

/**
 * Created by Adrian on 04.02.2017.
 */
public class InvalidTrytesException extends BaseException {

    public InvalidTrytesException() {
        super("Invalid input trytes");
    }
}
