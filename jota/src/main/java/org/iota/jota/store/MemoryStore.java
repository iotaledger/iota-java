package org.iota.jota.store;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryStore implements Store {
    
    private Map<String, Serializable> store;
    
    public MemoryStore() {
        store = new ConcurrentHashMap<String, Serializable>();
    }
    
    public MemoryStore(Map<String, Serializable> store) {
        this.store = store;
    }

    @Override
    public Serializable get(String key) {
        return get(key, null);
    }

    @Override
    public Serializable get(String key, Serializable def) {
        Serializable ret = store.get(key);
        return ret != null ? ret : def;
        
    }
    
    @Override
    public void load() throws Exception {
        
    }

    @Override
    public void save() throws Exception {
        
    }

    @Override
    public Serializable set(String key, Serializable value) {
        return store.put(key, value);
    }

    @Override
    public boolean canWrite() {
        return true;
    }

}
