package org.iota.jota.store;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryStore implements Store {
    
    private Map<String, Serializable> store;
    
    public MemoryStore() {
        
    }
    
    public MemoryStore(Map<String, Serializable> store) {
        this.store = store;
    }

    @Override
    public <T extends Serializable> T get(String key) {
        return get(key, null);
    }

    @Override
    public <T extends Serializable> T get(String key, T def) {
        Serializable prop = store.get(key);
        return prop != null ? (T) prop : def;
    }
    
    @Override
    public <T extends Serializable> T set(String key, T value) {
        T old = (T) store.put(key, value);
        return old;
    }
    
    @Override
    public void load() {
        store = new ConcurrentHashMap<>();
    }

    @Override
    public void save() {
        
    }
    
    public Map<String, Serializable> getStore() {
        return store;
    }

    @Override
    public boolean canWrite() {
        return true;
    }

}
