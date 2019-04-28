package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;

public class EventAccountError extends EventImpl {

    private Exception exception;
    private boolean shouldLog;

    public EventAccountError(Exception exception) {
        super(AccountEventType.Error);
        this.exception = exception;
        this.shouldLog = true;
    }
    
    public Exception getException() {
        return exception;
    }
    
    public String getMessage() {
        return exception.getMessage();
    }

    public Throwable getCause() {
        return exception.getCause();
    }
    
    public boolean shouldLog(){
        return shouldLog;
    }
    
    public void setLogging(boolean shouldLog) {
        this.shouldLog = shouldLog;
    }
}
