package org.iota.jota.account.errors;

public abstract class AccountError extends Exception {

    public AccountError(Exception e) {
        super(e);
    }

}
