package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.model.Bundle;

public class EventReceivedMessage extends EventAbstractBundle {
    
    private String message = null;
    
    public EventReceivedMessage(Bundle bundle) {
        super(AccountEventType.ReceivedMessage, bundle);
    }

    public String getMessage() {
        if (message != null) {
            return message;
        }
        
        return message = getBundle().getMessage();
    }
}
