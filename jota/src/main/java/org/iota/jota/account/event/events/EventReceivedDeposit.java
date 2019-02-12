package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventReceivedDeposit extends EventImpl {
    
    Logger log = LoggerFactory.getLogger(EventReceivedDeposit.class);
    
    Address receiver;
    
    Bundle bundle;

    public EventReceivedDeposit() {
        super(AccountEventType.ReceivedDeposit);
    }

    public Address getReceiver() {
        return receiver;
    }
    
    public Address getAddress(){
        return receiver;
    }
    
    public Bundle getBundle() {
        return bundle;
    }
    
    public long getAmount() {
        for (Transaction t : bundle.getTransactions()) {
            if (t.getAddress().equals(receiver.getAddress().toString())) {
                return t.getValue();
            }
        }
        
        // This should NEVER happen
        log.error("Deposit received event fired but could not find amount!\\n Please check " + receiver.getAddress());
        return -1;
    }
}
