package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;

public class EventReceivedDeposit extends EventImpl {

    public EventReceivedDeposit() {
        super(AccountEventType.ReceivedDeposit);
    }

}
