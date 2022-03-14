package org.iota.jota.account.event.events;


import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;

public class EventAttachingToTangle extends EventImpl {

    private String[] trytes;

    public EventAttachingToTangle(String[] trytes) {
        super(AccountEventType.ATTACHING_TO_TANGLE);
        
        this.trytes = trytes;
    }
    
    public String[] getTrytes() {
        return trytes;
    }
}
