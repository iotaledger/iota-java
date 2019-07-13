package org.iota.jota.error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class BaseException extends RuntimeException {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 5617085097507773343L;

    protected Collection<String> messages;

    public BaseException(String msg) {
        super(msg);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(msg);
    }

    public BaseException(String msg, Exception cause) {
        super(msg, cause);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(msg);
    }

    public BaseException(Collection<String> messages) {
        this.messages = messages;
    }

    public BaseException(Collection<String> messages, Exception cause) {
        super(cause);
        this.messages = messages;
    }

    @Override
    public String getMessage() {
        if (messages != null && messages.size() == 1){
            return messages.toArray()[0].toString();
        }
        return Arrays.toString(messages.toArray());
    }
}
