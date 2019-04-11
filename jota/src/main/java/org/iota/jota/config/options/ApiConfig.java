package org.iota.jota.config.options;

import java.util.List;

import org.iota.jota.config.Config;
import org.iota.jota.connection.Connection;

public interface ApiConfig extends Config {

    //TODO
    // getLocalPoW -> null?
    
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
