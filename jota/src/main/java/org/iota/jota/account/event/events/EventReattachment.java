package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;

public class EventReattachment extends EventImpl {

    public EventReattachment() {
        super(AccountEventType.Reattachment);
    }

}
