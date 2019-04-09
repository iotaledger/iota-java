package org.iota.jota.account.clock;

import java.util.Date;

public interface Clock {
    
    /**
     * Returns the time on this current clock
     * @return
     */
    Date time();
}
