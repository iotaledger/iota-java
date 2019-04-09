package org.iota.jota.account.event.events;

import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;
import org.iota.jota.types.Address;

public class EventNewInput extends EventImpl {
    
    private DepositRequest request;
    private Address address;

    public EventNewInput(Address address, DepositRequest request) {
        super(AccountEventType.DepositAddress);
        this.address = address;
        this.request = request;
    }
    
    public DepositRequest getRequest() {
        return request;
    }
    
    public Address getAddress() {
        return address;
    }
}
