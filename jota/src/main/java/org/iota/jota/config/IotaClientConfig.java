package org.iota.jota.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iota.jota.config.types.EnvConfig;
import org.iota.jota.config.types.FileConfig;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.connection.Connection;
import org.iota.jota.connection.ConnectionFactory;
import org.iota.jota.store.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    private static final Logger log = LoggerFactory.getLogger(IotaClientConfig.class);
    
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

    /**
     * Loads nodes from a properties file based on a key.
     * key gets a number starting at 1 appended and checks for the existing data.
     * Once no key + number data is found, we return the loaded data.
     * 
     * @param key
     * @return
     */
    public List<Connection> loadNodes(String key) {
        int start = 1;
        List<Connection> connections = new LinkedList<>();
        
        Map<String, String> options;
        do {
            options = new HashMap<>();
            String optionKey = key + start;
            for (Entry<String, Serializable> entry : store.getAll().entrySet()) {
                if (entry.getKey().startsWith(optionKey)) {
                    options.put(
                        // Remove the key segment, cannot do based on . since the value might have a .
                        entry.getKey().substring(key.length() + 1 + ((int)(Math.log10(start)+1))), 
                        entry.getValue().toString());
                }
            }
            
            if (options.size() > 0) {
                try {
                    Connection c = ConnectionFactory.createConnection(options, getConnectionTimeout());
                    if (c != null) {
                        connections.add(c);
                    }
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }
            start++;
        } while (options.size() > 0);
        
        return connections;
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
