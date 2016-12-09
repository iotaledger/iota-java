package jota.error;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Created by Adrian on 09.12.2016.
 */
public class ArgumentException extends BaseException {
    public ArgumentException() {
        super("wrong arguments passed to function");
    }
}
