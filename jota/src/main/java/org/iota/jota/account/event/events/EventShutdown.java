package org.iota.jota.account.event.events;

import java.util.Date;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.account.event.impl.EventImpl;

public class EventShutdown extends EventImpl {

    private Date time;

    public EventShutdown(Date now) {
        super(AccountEventType.Shutdown);
        this.time = now;
    }

    public Date getTime() {
        return time;
    }
}
