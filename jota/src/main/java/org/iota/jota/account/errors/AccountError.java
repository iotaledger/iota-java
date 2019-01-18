package org.iota.jota.account.errors;

public abstract class AccountError extends RuntimeException {
    
    public AccountError(String message) {
        super(message);
    }

    public AccountError(Exception e) {
        super(e);
    }

}
