package jota.error;

/**
 * This exception occurs when a transfer fails because their is not enough balance to perform the transfer.
 *
 * @author Adrian
 */
public class NotEnoughBalanceException extends BaseException {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -3807270816402226476L;

    /**
     * Initializes a new instance of the NotEnoughBalanceException.
     */
    public NotEnoughBalanceException() {
        super("Not enough balance");
    }
}
