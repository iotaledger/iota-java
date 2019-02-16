package org.iota.jota.config;

import org.iota.jota.account.AccountStore;
import org.iota.jota.account.AccountStoreImpl;
import org.iota.jota.store.IotaFileStore;
import org.iota.jota.utils.Constants;

public class IotaDefaultConfig extends IotaClientConfig {

    @Override
    public int getLegacyPort() {
        return Defaults.LEGACY_PORT;
    }

    @Override
    public String getLegacyProtocol() {
        return Defaults.LEGACY_PROTOCOL;
    }

    @Override
    public String getLegacyHost() {
        return Defaults.LEGACY_HOST;
    }

    @Override
    public AccountStore getStore() {
        return Defaults.STORE; 
    }

    @Override
    public int getMwm() {
        return Defaults.MWM;
    }

    @Override
    public int getDepth() {
        return Defaults.DEPTH;
    }

    @Override
    public int getSecurityLevel() {
        return Defaults.SECURITY_LEVEL;
    }

    @Override
    public int getConnectionTimeout() {
        return Defaults.CONNECTION_TIMEOUT;
    }
    
    public static class Defaults {
        public static final AccountStore STORE = new AccountStoreImpl(new IotaFileStore());
        
        public static final int CONNECTION_TIMEOUT = 500;
        public static final int SECURITY_LEVEL = Constants.MIN_SECURITY_LEVEL;
        public static final int DEPTH = 3;
        public static final int MWM = 14;
        
        public static final int LEGACY_PORT = 14265;
        public static final String LEGACY_PROTOCOL = "http";
        public static final String LEGACY_HOST = "localhost";
    }
}
