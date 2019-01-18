package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;
import org.iota.jota.model.Bundle;

public class EventTransferConfirmed extends EventImpl {

    private Bundle bundle;
    
    public EventTransferConfirmed(Bundle bundle) {
        super(AccountEventType.TransferConfirmed);
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

}
