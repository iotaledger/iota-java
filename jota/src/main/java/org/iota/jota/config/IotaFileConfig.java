package org.iota.jota.config;

import java.io.File;
import java.util.Optional;

import org.iota.jota.store.FlatFileStore;
import org.iota.jota.store.IotaFileStore;
import org.iota.jota.store.IotaStore;

public class IotaFileConfig extends IotaClientConfig {

    private static final String DEFAULT_CONFIG_NAME = ".." + File.separator + "node_config.properties";
    
    private static final String CONFIG_PROT = "iota.node.protocol";
    private static final String CONFIG_HOST = "iota.node.host";
    private static final String CONFIG_PORT = "iota.node.port";
    
    private static final String CONFIG_STORE = "storage.url";
    
    public IotaFileConfig() throws Exception {
        super(new FlatFileStore(DEFAULT_CONFIG_NAME));
    }

    public IotaFileConfig(String url) throws Exception {
        super(new FlatFileStore(url));
    }
    
    public IotaFileConfig(Optional<String> url) throws Exception {
        super(new FlatFileStore(url.isPresent() ? url.get() : DEFAULT_CONFIG_NAME));
    }

    @Override
    public int getLegacyPort() {
        return intOrNull(CONFIG_PORT);
    }

    @Override
    public String getLegacyProtocol() {
        return stringOrNull(CONFIG_PROT);
    }

    @Override
    public String getLegacyHost() {
        return stringOrNull(CONFIG_HOST);
    }

    @Override
    public IotaStore getStore() {
        return new IotaFileStore(stringOrNull(CONFIG_STORE));
    }
}
