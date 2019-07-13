package org.iota.jota.builder;

import java.util.List;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.clock.Clock;
import org.iota.jota.account.plugins.Plugin;
import org.iota.jota.account.seedprovider.SeedProvider;

public interface AccountBuilderSettings {

    IotaAPI getApi();
    
    SeedProvider getSeed();
    
    Clock getTime();
    
    List<Plugin> getPlugins();
}
