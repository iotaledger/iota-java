package jota.error;

/**
 * Created by Adrian on 22.01.2017.
 */
public class NoTransactionExcpection extends BaseException {

    public NoTransactionExcpection() {
        super("Not transactions for the provided seed");
    }
}
