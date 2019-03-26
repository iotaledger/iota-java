package org.iota.jota.store;

import java.io.Serializable;
import java.util.Map;

public abstract class IotaClientStore implements Store {

    protected Store store;
    
    public IotaClientStore(Store store) {
        this.store = store;
    }
    
    public boolean canWrite() {
        return store.canWrite();
    }
    
    @Override
    public String toString() {
        return store.toString();
    }

    @Override
    public void load() throws Exception {
        store.load();
    }

    @Override
    public void save(boolean closeResources) throws Exception {
        store.save(closeResources);
    }

    @Override
    public Serializable get(String key) {
        return store.get(key);
    }

    @Override
    public <T extends Serializable> T get(String key, T def) {
        return store.get(key, def);
    }

    @Override
    public <T extends Serializable> T set(String key, T value) {
        return store.set(key, value);
    }
    
    @Override
    public Map<String, Serializable> getAll() {
        return store.getAll();
    }
    
    @Override
    public Serializable delete(String key) {
        return store.delete(key);
    }
}
