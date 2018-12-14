package org.iota.jota.config;

import org.iota.jota.store.IotaFileStore;
import org.iota.jota.store.IotaStore;

public class IotaDefaultConfig extends IotaClientConfig {

    @Override
    public int getLegacyPort() {
        return 14265;
    }

    @Override
    public String getLegacyProtocol() {
        return "http";
    }

    @Override
    public String getLegacyHost() {
        return "localhost";
    }

    @Override
    public IotaStore getStore() {
        return new IotaFileStore();
    }

}
