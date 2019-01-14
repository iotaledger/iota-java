package org.iota.jota.config;

import org.iota.jota.account.AccountStore;
import org.iota.jota.account.AccountStoreImpl;
import org.iota.jota.store.EnvironmentStore;
import org.iota.jota.store.IotaFileStore;

public class EnvConfig extends IotaClientConfig {

    private static final String CONFIG_PARAM = "CONFIG";
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
    
    public String getConfigName() {
        return stringOrNull(CONFIG_PARAM);
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
        return new AccountStoreImpl(new IotaFileStore(stringOrNull(ENV_STORE)));
    }
    
    @Override
    public int getMwm() {
        return intOrNull(ENV_MWM);
    }

    @Override
    public int getDept() {
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
}
