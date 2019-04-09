package org.iota.jota.config.types;

import java.util.List;

import org.iota.jota.account.AccountStore;
import org.iota.jota.account.store.AccountFileStore;
import org.iota.jota.config.IotaClientConfig;
import org.iota.jota.connection.Connection;
import org.iota.jota.store.EnvironmentStore;

public class EnvConfig extends IotaClientConfig {

    private static final String ENV_PROT = "IOTA_NODE_PROTOCOL";
    private static final String ENV_HOST = "IOTA_NODE_HOST";
    private static final String ENV_PORT = "IOTA_NODE_PORT";
    private static final String ENV_TIMEOUT = "ENV_TIMEOUT";
    
    private static final String ENV_STORE = "IOTA_STORE_LOCATION";
    
    private static final String ENV_MWM = "IOTA_ACCOUNT_MWM";
    private static final String ENV_DEPTH = "IOTA_ACCOUNT_DEPTH";
    private static final String ENV_SECURITY = "IOTA_ACCOUNT_SECURITY";
    
    public EnvConfig() throws Exception {
        super(new EnvironmentStore());
    }
    
    @Deprecated
    public int getLegacyPort() {
        return intOrNull(ENV_PORT);
    }
    
    @Deprecated
    public String getLegacyProtocol() {
        return stringOrNull(ENV_PROT);
    }
    
    @Deprecated
    public String getLegacyHost() {
        return stringOrNull(ENV_HOST);
    }

    @Override
    public AccountStore getStore() {
        return new AccountFileStore(stringOrNull(ENV_STORE));
    }
    
    @Override
    public int getMwm() {
        return intOrNull(ENV_MWM);
    }

    @Override
    public int getDepth() {
        return intOrNull(ENV_DEPTH);
    }

    @Override
    public int getSecurityLevel() {
        return intOrNull(ENV_SECURITY);
    }

    @Override
    public int getConnectionTimeout() {
        return intOrNull(ENV_TIMEOUT);
    }

    @Override
    public List<Connection> getNodes() {
        return null;
    }
}
