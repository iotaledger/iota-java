package org.iota.jota.store;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * 
 *
 */
public interface Store {
    
    /**
     * Loads this store into cache
     * 
     * @throws Exception
     */
    void load() throws Exception;
    
    /**
     * Saves this store onto its storage method.
     * 
     * @param closeResources if we want to clean up (final safe before exit)
     * @throws Exception
     */
    void save(boolean closeResources) throws Exception;

    /**
     * 
     * @param key
     * @return
     */
    Serializable get(String key);
    
    /**
     * 
     * @param key
     * @param def
     * @return
     */
    <T extends Serializable> T get(String key, T def);
    
    /**
     * 
     * @param key
     * @param value
     * @return The old value, or <code>null</code> if there was none
     */
    <T extends Serializable> T set(String key, T value);
    
    /**
     * Returns all the values in this store
     * 
     * @return
     */
    Map<String, Serializable> getAll();
    
    /**
     * 
     * 
     * @return
     */
    boolean canWrite();
}
