package jota.error;

/**
 * Created by Adrian on 09.12.2016.
 */
public class ArgumentException extends BaseException {

    private static final long serialVersionUID = -7850044681919575720L;

    public ArgumentException() {
        super("Wrong arguments passed to function");
    }
}
