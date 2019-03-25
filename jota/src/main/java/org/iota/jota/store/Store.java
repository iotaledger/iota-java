package org.iota.jota.store;

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
    String get(String key);
    
    /**
     * Gets a value from the store
     * 
     * @param key The field to look for
     * @param def The object to return when the key doesn't have a field
     * @return the requested value, or the default when the key didn't exist
     */
    String get(String key, String def);
    
    /**
     * Sets a value in the store
     * 
     * @param key The field to set the value
     * @param value The value to set
     * @return The old value, or <code>null</code> if there was none
     */
    String set(String key, String value);
    
    /**
     * Returns all the values in this store
     * 
     * @return Map of values
     */
    Map<String, String> getAll();
    
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
    String delete(String key);
}
