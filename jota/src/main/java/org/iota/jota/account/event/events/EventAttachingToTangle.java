package org.iota.jota.account.event.events;


import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;

public class EventAttachingToTangle extends EventImpl {

    private String[] trytes;

    public EventAttachingToTangle(String[] trytes) {
        super(AccountEventType.AttachingToTangle);
        
        this.trytes = trytes;
    }
    
    public String[] getTrytes() {
        return trytes;
    }
}
