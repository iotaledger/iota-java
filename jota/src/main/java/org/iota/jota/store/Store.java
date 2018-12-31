package org.iota.jota.store;

import java.io.Serializable;

public interface Store {
    
    void load() throws Exception;
    
    void save() throws Exception;

    <T extends Serializable> T get(String key);
    
    <T extends Serializable> T get(String key, T def);
    
    <T extends Serializable> T set(String key, T value);
    
    boolean canWrite();
}
