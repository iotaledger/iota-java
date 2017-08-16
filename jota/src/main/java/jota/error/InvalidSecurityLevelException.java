package jota.error;

/**
 * This exception occurs when an invalid security level is provided.
 *
 * @author pinpong
 */
public class InvalidSecurityLevelException extends BaseException {

    /**
     * Initializes a new instance of the InvalidSecurityLevelException.
     */
    public InvalidSecurityLevelException() {
        super("Invalid security level");
    }
}
