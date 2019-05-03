package org.iota.jota.account.errors;

import org.iota.jota.error.BaseException;

/**
 * This exception occurs as part of a `promoteTransaction` call if the transaction is not promotable.
 */
public class NotPromotableException extends BaseException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 8271238442325973496L;

    public NotPromotableException(String info) {
        super("Transaction is not promotable: " + info);
    }
}
