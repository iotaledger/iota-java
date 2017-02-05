package jota.error;

/**
 * Created by Adrian on 09.12.2016.
 */
public class NotEnoughBalanceException extends BaseException {
    
    private static final long serialVersionUID = -3807270816402226476L;

    public NotEnoughBalanceException() {
        super("Not enough balance");
    }
}
