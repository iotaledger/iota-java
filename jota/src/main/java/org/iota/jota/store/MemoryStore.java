package org.iota.jota.store;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryStore implements Store {
    
    private Map<String, Serializable> store;
    
    public MemoryStore() {
        store = new ConcurrentHashMap<String, Serializable>();
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void save() throws Exception {
        // TODO Auto-generated method stub
        
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
