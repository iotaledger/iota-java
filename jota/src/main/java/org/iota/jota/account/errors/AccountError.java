package org.iota.jota.account.errors;

public class AccountError extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3461570403194261127L;

    public AccountError(String message) {
        super(message);
    }

    public AccountError(Exception e) {
        super(e);
    }

}
