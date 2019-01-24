package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;
import org.iota.jota.model.Bundle;

public class EventPromotion extends EventImpl {

    private Bundle promotedBundle;

    public EventPromotion(Bundle promotedBundle) {
        super(AccountEventType.Promotion);
        
        this.promotedBundle = promotedBundle;
    }

    public Bundle getPromotedBundle() {
        return promotedBundle;
    }
}
