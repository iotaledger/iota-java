package jota.error;

/**
 * Created by Adrian on 09.12.2016.
 */
public class NotEnoughBalanceException extends BaseException {
    public NotEnoughBalanceException() {
        super("not enough balance dude");
    }
}
