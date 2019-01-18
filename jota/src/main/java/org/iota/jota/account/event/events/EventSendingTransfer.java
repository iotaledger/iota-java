package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;
import org.iota.jota.model.Bundle;

public class EventSendingTransfer extends EventImpl {

    private Bundle bundle;

    public EventSendingTransfer(Bundle bundle) {
        super(AccountEventType.SendingTransfer);
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

}
