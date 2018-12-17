package org.iota.jota.config.options;

import org.iota.jota.IotaAPI;

public interface AccountBuilderSettings {

    IotaAPI getApi();
    
    String getSeed();
}
