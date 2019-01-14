package org.iota.jota.config;

import org.iota.jota.account.AccountStore;
import org.iota.jota.account.AccountStoreImpl;
import org.iota.jota.store.IotaFileStore;
import org.iota.jota.utils.Constants;

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
    public AccountStore getStore() {
        return new AccountStoreImpl(new IotaFileStore());
    }

    @Override
    public int getMwm() {
        return 14;
    }

    @Override
    public int getDept() {
        return 3;
    }

    @Override
    public int getSecurityLevel() {
        return Constants.MIN_SECURITY_LEVEL;
    }

    @Override
    public int getConnectionTimeout() {
        return 500;
    }
}
