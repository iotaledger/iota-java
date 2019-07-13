package org.iota.jota.error;

/**
 * This exception occurs when an API method was disabled in the node
 *
 */
public class AccessLimitedException extends BaseException {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -7850044681919575720L;

    /**
     * Initializes a new instance of the AccessLimitedException.
     */
    public AccessLimitedException(String msg) {
        super(msg);
    }

    public AccessLimitedException(String msg, Exception cause) {
        super(msg, cause);
    }
}
