package org.iota.jota.account.event.events;


import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;

public class EventDoingProofOfWork extends EventImpl {

    private String[] trytes;

    public EventDoingProofOfWork(String[] trytes) {
        super(AccountEventType.DOING_PROOF_OF_WORK);
        
        this.trytes = trytes;
    }
    
    public String[] getTrytes() {
        return trytes;
    }
}
