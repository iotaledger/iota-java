package org.iota.jota.config;

import org.iota.jota.store.EnvironmentStore;
import org.iota.jota.store.IotaFileStore;
import org.iota.jota.store.IotaStore;

public class IotaEnvConfig extends IotaClientConfig {

    private static final String CONFIG_PARAM = "CONFIG";
    private static final String ENV_PROT = "IOTA_NODE_PROTOCOL";
    private static final String ENV_HOST = "IOTA_NODE_HOST";
    private static final String ENV_PORT = "IOTA_NODE_PORT";
    private static final String ENV_STORE = "IOTA_STORE_LOCATION";
    
    public IotaEnvConfig() throws Exception {
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
    public IotaStore getStore() {
        return new IotaFileStore(stringOrNull(ENV_STORE));
    }
}
