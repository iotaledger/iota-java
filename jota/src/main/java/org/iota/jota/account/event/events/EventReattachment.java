package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;
import org.iota.jota.model.Bundle;

public class EventReattachment extends EventImpl {

    private Bundle originalBundle;
    private Bundle newBundle;

    public EventReattachment(Bundle originalBundle, Bundle newBundle) {
        super(AccountEventType.Reattachment);
        this.originalBundle = originalBundle;
        this.newBundle = newBundle;
    }

    public Bundle getOriginalBundle() {
        return originalBundle;
    }
    
    public Bundle getNewBundle() {
        return newBundle;
    }
}
