package org.iota.jota.store;

import java.io.Serializable;

public interface Store {
    
    void load() throws Exception;
    
    void save() throws Exception;

    Serializable get(String key);
    
    Serializable get(String key, Serializable def);
    
    Serializable set(String key, Serializable value);
    
    boolean canWrite();
}
