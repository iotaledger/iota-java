package org.iota.jota;

import org.iota.jota.account.errors.AccountError;

public class SendException extends AccountError {

    public SendException(Exception e) {
        super(e);
    }

}
