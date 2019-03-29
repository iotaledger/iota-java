package org.iota.jota.config.types;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.iota.jota.account.AccountStore;
import org.iota.jota.account.store.AccountFileStore;
import org.iota.jota.config.IotaClientConfig;
import org.iota.jota.connection.Connection;
import org.iota.jota.store.FlatFileStore;
import org.iota.jota.store.PropertiesStore;

public class FileConfig extends IotaClientConfig {

    private static final String DEFAULT_CONFIG_NAME = ".." + File.separator + "node_config.properties";
    
    private static final String CONFIG_NODE_PRE = "iota.node";
    private static final String CONFIG_PROT = CONFIG_NODE_PRE + ".protocol";
    private static final String CONFIG_HOST = CONFIG_NODE_PRE + ".host";
    private static final String CONFIG_PORT = CONFIG_NODE_PRE + ".port";
    private static final String CONFIG_TIMEOUT = "connection.timeout";
    
    private static final String CONFIG_STORE = "accounts.storage.url";
    
    private static final String CONFIG_MWM = "accounts.mwm";
    private static final String CONFIG_DEPTH = "accounts.depth";
    private static final String CONFIG_SECURITY = "accounts.security";
    
    public FileConfig() throws Exception {
        super(new FlatFileStore(DEFAULT_CONFIG_NAME));
    }
    
    public FileConfig(FlatFileStore store) throws Exception {
        super(store);
    }

    public FileConfig(String url) throws Exception {
        super(new FlatFileStore(url));
    }
    
    public FileConfig(Optional<String> url) throws Exception {
        super(new FlatFileStore(url.isPresent() ? url.get() : DEFAULT_CONFIG_NAME));
    }

    // In legacy, every config is a properties.
    protected FileConfig(PropertiesStore store) throws Exception {
        super(store);
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
    public AccountStore getStore() {
        return new AccountFileStore(stringOrNull(CONFIG_STORE));
    }
    
    @Override
    public int getMwm() {
        return intOrNull(CONFIG_MWM);
    }

    @Override
    public int getDepth() {
        return intOrNull(CONFIG_DEPTH);
    }

    @Override
    public int getSecurityLevel() {
        return intOrNull(CONFIG_SECURITY);
    }

    @Override
    public int getConnectionTimeout() {
        return intOrNull(CONFIG_TIMEOUT);
    }

    @Override
    public List<Connection> getNodes() {
        return loadNodes(CONFIG_NODE_PRE);
    }
}
