package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventReceivedDeposit extends EventAbstractBundle {
    
    private static final Logger log = LoggerFactory.getLogger(EventReceivedDeposit.class);
    
    private Address receiver;

    public EventReceivedDeposit(Bundle bundle, Address address) {
        super(AccountEventType.RECEIVED_DEPOSIT, bundle);
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
            if (t.getAddress().equals(receiver.getAddressHash().toString())) {
                return t.getValue();
            }
        }
        
        // This should NEVER happen
        log.error("Deposit received event fired but could not find amount!\\n Please check " + receiver.getAddressHash());
        return -1;
    }
}
