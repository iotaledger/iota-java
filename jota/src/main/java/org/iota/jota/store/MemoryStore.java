package org.iota.jota.store;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryStore implements Store {

    private static final Logger log = LoggerFactory.getLogger(MemoryStore.class);
    
    private Map<String, Serializable> store;
    
    public MemoryStore() {
        
    }
    
    public MemoryStore(Map<String, Serializable> store) {
        // Copy to ensure writeability
        this.store = new ConcurrentHashMap<String, Serializable>(store);
    }

    @Override
    public Serializable get(String key) {
        return get(key, null);
    }

    @Override
    public <T extends Serializable> T get(String key, T def) {
        try {
            @SuppressWarnings("unchecked")
            T prop = (T) store.get(key);
            return prop != null ? prop : def;
        } catch (ClassCastException e) {
            return def;
        }
    }
    
    @Override
    public <T extends Serializable> T set(String key, T value) {
        try {
            Serializable oldValue = store.put(key, value);
            @SuppressWarnings("unchecked")
            T old = (T) oldValue;
            return old;
        } catch (ClassCastException e) {
            log.warn("Attempted to store a different type of value for a key." );
            log.warn("Explanation: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public void load() {
        if (this.store == null) {
            store = new ConcurrentHashMap<>();
        }
    }

    @Override
    public void save(boolean closeResources) {
        
    }
    
    @Override
    public Map<String, Serializable> getAll() {
        return store;
    }

    @Override
    public boolean canWrite() {
        return true;
    }

    @Override
    public Serializable delete(String key) {
        return store.remove(key);
    }
}
