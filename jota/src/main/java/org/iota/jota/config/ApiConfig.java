package org.iota.jota.config;

import java.util.List;

import org.iota.jota.connection.Connection;

public interface ApiConfig extends Config{

    /**
     * 
     * @return
     */
    List<Connection> getNodes();

    boolean hasNodes();
    
    @Deprecated
    public int getLegacyPort();
    
    @Deprecated
    public String getLegacyProtocol();
    
    @Deprecated
    public String getLegacyHost();
}
