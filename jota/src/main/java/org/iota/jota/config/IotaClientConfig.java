package org.iota.jota.config;

import java.io.Serializable;
import java.util.List;

import org.iota.jota.connection.Connection;
import org.iota.jota.store.Store;

public abstract class IotaClientConfig implements IotaConfig {
    
    protected Store store;
    
    /**
     * Used for empty construction in {@link IotaDefaultConfig}
     */
    protected IotaClientConfig() {
        
    }

    public IotaClientConfig(Store store) throws Exception {
        this.store = store;
        this.store.load();
    }

    @Override
    public List<Connection> getNodes() {
        return null;
    }
    
    @Override
    public boolean hasNodes() {
        List<Connection> nodes = getNodes();
        return nodes != null && nodes.size() > 0;
    }

    protected String stringOrNull(String key) {
        Serializable ret = store.get(key);
        return ret != null ? ret .toString() : null;
    }
    
    /**
     * 
     * @param key
     * @return The parsed int value or <code>0</code>
     */
    protected int intOrNull(String key) {
        Serializable ret = store.get(key);
        try {
            return Integer.parseInt(ret.toString());
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return store.toString();
    }
}
