package org.iota.jota.account.event.impl;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.Event;

public abstract class EventImpl implements Event {

    AccountEventType type;
    
    public EventImpl(AccountEventType type) {
        this.type = type;
    }
    
}
