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
    
    /**
     * Connection time out in seconds
     * @return
     */
    int getConnectionTimeout();
    
    @Deprecated
    public int getLegacyPort();
    
    @Deprecated
    public String getLegacyProtocol();
    
    @Deprecated
    public String getLegacyHost();
}
