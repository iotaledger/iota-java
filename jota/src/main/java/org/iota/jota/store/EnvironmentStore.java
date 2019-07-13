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
    public <T extends Serializable> T  get(String key, T def) {
        try {
            @SuppressWarnings("unchecked")
            T value = (T) System.getenv(key);
            return value != null ? value : def;
        } catch (ClassCastException e) {
            return def;
        }
    }

    @Override
    public <T extends Serializable> T  set(String key, T value) {
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map<String, Serializable> getAll() {
        //TODO: Make nicer, But String is already Serializable
        return (Map)System.getenv();
    }

    @Override
    public String delete(String key) {
        throw new IllegalArgumentException("Environment store does not allow deleting");
    }
}
