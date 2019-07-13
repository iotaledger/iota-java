package org.iota.jota.error;

/**
 * This exception occurs when an API method threw an unexpected error on the node
 *
 */
public class InternalException extends BaseException {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -7850044681919575720L;

    /**
     * Initializes a new instance of the InternalException.
     */
    public InternalException(String msg) {
        super(msg);
    }

    public InternalException(String msg, Exception cause) {
        super(msg, cause);
    }
}
