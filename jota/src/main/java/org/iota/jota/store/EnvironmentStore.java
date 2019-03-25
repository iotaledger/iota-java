package org.iota.jota.store;

import java.io.Serializable;
import java.util.Map;

public class EnvironmentStore implements Store {

    @Override
    public void load() {
        //Do nothing :D
    }

    @Override
    public void save(boolean closeResources) throws Exception {
        throw new Exception("Environment store does not allow saving");
    }

    @Override
    public String get(String key) {
        return System.getenv(key);
    }

    @Override
    public String get(String key, String def) {
        String value = System.getenv(key);
        return value != null ? value : def;
    }

    @Override
    public String set(String key, String value) {
        throw new IllegalArgumentException("Environment store does not allow setting values");
    }

    @Override
    public boolean canWrite() {
        return false;
    }
    
    @Override
    public String toString() {
        return "Environment variables";
    }

    @Override
    public Map<String, String> getAll() {
        //TODO: Make nicer
        return (Map<String, String>)((Map)System.getenv());
    }

    @Override
    public String delete(String key) {
        throw new IllegalArgumentException("Environment store does not allow deleting");
    }
}
