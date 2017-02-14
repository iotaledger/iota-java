package jota.error;

/**
 * @author Adrian
 */
public class NotEnoughBalanceException extends BaseException {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -3807270816402226476L;

    public NotEnoughBalanceException() {
        super("Not enough balance");
    }
}
