package jota.error;

/**
 * @author Adrian
 */
public class ArgumentException extends BaseException {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -7850044681919575720L;

    public ArgumentException() {
        super("Wrong arguments passed to function");
    }

    public ArgumentException(String msg) {
        super(msg);
    }
}
