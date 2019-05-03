package org.iota.jota.error;

/**
 * This exception occurs when an API method threw an unexpected error on the node
 *
 */
public class ConnectorException extends BaseException {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -7850044681919575720L;

    private final int errorCode;

    /**
     * Initializes a new instance of the ConnectorException.
     */
    public ConnectorException(String msg, int errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public ConnectorException(String msg, Exception cause, int errorCode) {
        super(msg, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
