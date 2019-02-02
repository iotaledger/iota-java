package org.iota.jota.account.clock;

import java.util.Date;

public class SystemClock implements Clock {

    @Override
    public Date time() {
        return new Date();
    }

}
