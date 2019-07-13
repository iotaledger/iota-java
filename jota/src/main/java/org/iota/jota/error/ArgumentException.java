package org.iota.jota.error;

/**
 * This exception occurs when an invalid argument is provided.
 *
 */
public class ArgumentException extends BaseException {
    
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -7850044681919575720L;

    /**
     * Initializes a new instance of the ArgumentException.
     */
    public ArgumentException() {
        super("Wrong arguments passed to function");
    }

    /**
     * Initializes a new instance of the ArgumentException.
     */
    public ArgumentException(String msg) {
        super(msg);
    }

    public ArgumentException(String msg, Exception e) {
        super(msg, e);
    }
}
