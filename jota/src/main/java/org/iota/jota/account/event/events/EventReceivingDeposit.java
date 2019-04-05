package org.iota.jota.account.event.events;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.model.Bundle;

public class EventReceivingDeposit extends EventAbstractBundle {

    public EventReceivingDeposit(Bundle bundle) {
        super(AccountEventType.ReceivingDeposit, bundle);
    }

}
