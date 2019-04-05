package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;
import org.iota.jota.model.Bundle;

public class EventAbstractBundle extends EventImpl {
    
    private Bundle bundle;

    public EventAbstractBundle(AccountEventType type, Bundle bundle) {
        super(type);
        
        this.bundle = bundle;
    }
    
    public Bundle getBundle() {
        return bundle;
    }
}
