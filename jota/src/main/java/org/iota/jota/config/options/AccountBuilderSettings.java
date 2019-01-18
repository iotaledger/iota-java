package org.iota.jota.config.options;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.clock.Clock;

public interface AccountBuilderSettings {

    IotaAPI getApi();
    
    String getSeed();
    
    Clock getTime();
}
