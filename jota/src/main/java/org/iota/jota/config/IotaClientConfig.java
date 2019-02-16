package org.iota.jota.config;

import java.io.Serializable;
import java.util.List;

import org.iota.jota.connection.Connection;
import org.iota.jota.store.Store;

/**
 * 
 * IotaClientConfig is a config which implements all required config variables using a provided storage method.
 * It contains utility methods for getting fields from the storage by key.
 * 
 * We have this because each Config type( {@link EnvConfig}, {@link FileConfig}) have their own naming conventions
 * but use the same reading system through their storage object
 *
 */
public abstract class IotaClientConfig implements IotaConfig {
    
    protected Store store;
    
    /**
     * Used for empty construction in {@link IotaDefaultConfig}
     */
    protected IotaClientConfig() {
        
    }

    /**
     * Creates a new IotaClientConfig using the provided storage method.
     * The store gets loaded as well.
     * 
     * @param store the store to use in this config
     * @throws Exception if the store failed to load
     */
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

    /**
     * Reads the value from the store using the provided key
     * 
     * @param key the store field we are reading
     * @return The value or <code>null</code>
     */
    protected String stringOrNull(String key) {
        Serializable ret = store.get(key);
        return ret != null ? ret .toString() : null;
    }
    
    /**
     * Reads the value from the store using the provided key
     * 
     * @param key the store field we are reading
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
