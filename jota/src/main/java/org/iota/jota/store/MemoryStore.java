package org.iota.jota.store;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryStore implements Store {
    
    private Map<String, String> store;
    
    public MemoryStore() {
        
    }
    
    public MemoryStore(Map<String, String> store) {
        this.store = store;
    }

    @Override
    public String get(String key) {
        return get(key, null);
    }

    @Override
    public String get(String key, String def) {
        String prop = store.get(key);
        return prop != null ? prop : def;
    }
    
    @Override
    public String set(String key, String value) {
        String old = store.put(key, value);
        return old;
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
    public Map<String, String> getAll() {
        return store;
    }

    @Override
    public boolean canWrite() {
        return true;
    }

    @Override
    public String delete(String key) {
        return store.remove(key);
    }
}
