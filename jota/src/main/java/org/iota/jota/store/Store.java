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
     * @throws Exception
     */
    void save() throws Exception;

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
     * @return
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
