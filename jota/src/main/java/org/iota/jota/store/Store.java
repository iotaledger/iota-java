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
     * Gets a value from the store
     * 
     * @param key The field to look for
     * @param def The object to return when the key doesn't have a field
     * @return the requested value, or the default when the key didn't exist
     */
    <T extends Serializable> T get(String key, T def);
    
    /**
     * Sets a value in the store
     * 
     * @param key The field to set the value
     * @param value The value to set
     * @return The old value, or <code>null</code> if there was none
     */
    <T extends Serializable> T set(String key, T value);
    
    /**
     * Returns all the values in this store
     * 
     * @return Map of values
     */
    Map<String, Serializable> getAll();
    
    /**
     * Some stores like environment variables can't write.
     * Those stores can't be used in account storage.
     * 
     * @return
     */
    boolean canWrite();
    
    /**
     * Delete a field form the store
     * 
     * @param key the key we want to delete
     * @return The deleted value, or <code>null</code> if there was none
     */
    Serializable delete(String key);
}
