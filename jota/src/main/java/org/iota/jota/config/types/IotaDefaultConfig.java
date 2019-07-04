package org.iota.jota.config.types;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.iota.jota.account.AccountStore;
import org.iota.jota.account.store.AccountFileStore;
import org.iota.jota.config.IotaClientConfig;
import org.iota.jota.connection.Connection;
import org.iota.jota.connection.HttpConnector;
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
        public static final AccountStore STORE = new AccountFileStore();
        
        public static final int CONNECTION_TIMEOUT = 500;
        public static final int SECURITY_LEVEL = Constants.MAX_SECURITY_LEVEL;
        public static final int DEPTH = 3;
        public static final int MWM = 14;
        
        public static final int LEGACY_PORT = 14265;
        public static final String LEGACY_PROTOCOL = "http";
        public static final String LEGACY_HOST = "localhost";

        public static final String DATABASE_NAME = "iota_account";
        public static final String TABLE_NAME = "accounts";
    }

    @Override
    public List<Connection> getNodes() {
        List<Connection> list =  new ArrayList<>();
        try {
            list.add(new HttpConnector(getLegacyProtocol(), getLegacyHost(), getLegacyPort(), getConnectionTimeout()));
        } catch (MalformedURLException e) {
            // Default should have unit tests to prevent  this
        }
        return list;
    }
}
