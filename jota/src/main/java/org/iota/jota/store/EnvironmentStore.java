package org.iota.jota.store;

import java.io.Serializable;

public class EnvironmentStore implements Store {

    @Override
    public void load() {
        //Do nothing :D
    }

    @Override
    public void save() throws Exception {
        throw new Exception("Environment store does not allow saving");
    }

    @Override
    public Serializable get(String key) {
        return System.getenv(key);
    }

    @Override
    public Serializable get(String key, Serializable def) {
        String value = System.getenv(key);;
        return value != null ? value : def;
    }

    @Override
    public Serializable set(String key, Serializable value) {
        throw new IllegalArgumentException("Environment store does not allow setting values");
        //throw new NotAllowedException("Environment store does not allow setting values");
    }

    @Override
    public boolean canWrite() {
        return false;
    }
    
    @Override
    public String toString() {
        return "Environment variables";
    }
}
