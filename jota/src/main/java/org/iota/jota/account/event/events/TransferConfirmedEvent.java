package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;

public class TransferConfirmedEvent extends EventImpl {

    public TransferConfirmedEvent() {
        super(AccountEventType.TransferConfirmed);
    }

}
