package org.iota.jota.config.options;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.clock.Clock;
import org.iota.jota.account.seedprovider.SeedProvider;

public interface AccountBuilderSettings {

    IotaAPI getApi();
    
    SeedProvider getSeed();
    
    Clock getTime();
}
