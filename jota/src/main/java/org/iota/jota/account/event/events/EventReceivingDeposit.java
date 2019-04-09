package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventReceivingDeposit extends EventAbstractBundle {
    
    private static final Logger log = LoggerFactory.getLogger(EventReceivingDeposit.class);
    
    private Address receiver;

    public EventReceivingDeposit(Bundle bundle, Address address) {
        super(AccountEventType.ReceivingDeposit, bundle);
        this.receiver = address;
    }
    
    public Address getReceiver() {
        return receiver;
    }
    
    public Address getAddress(){
        return receiver;
    }
    
    public long getAmount() {
        for (Transaction t : getBundle().getTransactions()) {
            if (t.getAddress().equals(receiver.getAddress().toString())) {
                return t.getValue();
            }
        }
        
        // This should NEVER happen
        log.error("Deposit received event fired but could not find amount!\\n Please check " + receiver.getAddress());
        return -1;
    }
}
