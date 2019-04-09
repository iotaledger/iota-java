package org.iota.jota.account.errors;

public class AccountLoadError extends AccountError {
    
    private static final long serialVersionUID = -7699935975313493322L;

    public AccountLoadError(String message) {
        super(message);
    }

    public AccountLoadError(Exception e) {
        super(e);
    }
}
