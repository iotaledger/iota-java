package org.iota.jota.account.errors;

import org.iota.jota.account.errors.AccountError;

public class SendException extends AccountError {

    /**
     * 
     */
    private static final long serialVersionUID = -5311863987778424442L;

    public SendException(Exception e) {
        super(e);
    }

}
