package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventReceivedDeposit extends EventAbstractBundle {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EventReceivedDeposit.class);
    
    private Address receiver;

    public EventReceivedDeposit(Bundle bundle, Address address) {
        super(AccountEventType.ReceivedDeposit, bundle);
        receiver = address;
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
        LOGGER.error("Deposit received event fired but could not find amount!\\n Please check " + receiver.getAddress());
        return -1;
    }
}
