package org.iota.jota.account.errors;

public abstract class AccountError extends RuntimeException {

    public AccountError(Exception e) {
        super(e);
    }

}
