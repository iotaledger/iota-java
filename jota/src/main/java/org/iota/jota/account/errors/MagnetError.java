package org.iota.jota.account.errors;

public class MagnetError extends AccountError {

    /**
     * 
     */
    private static final long serialVersionUID = 2841363707066447666L;

    public MagnetError(String message) {
        super(message);
    }

    public MagnetError(Exception e) {
        super(e);
    }
}
