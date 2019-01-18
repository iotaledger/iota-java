package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;

public class EventReceievedMessage extends EventImpl {

    public EventReceievedMessage() {
        super(AccountEventType.ReceivedMessage);
    }

}
