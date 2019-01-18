package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;

public class EventPromotion extends EventImpl {

    public EventPromotion() {
        super(AccountEventType.Promotion);
    }

}
