package jota.error;

/**
 * This exception occurs as part of a `promoteTransaction` call if the transaction is not promotable.
 */
public class NotPromotableException extends BaseException{
    public NotPromotableException(String info) {
        super("Transaction is not promotable: " + info);
    }
}
