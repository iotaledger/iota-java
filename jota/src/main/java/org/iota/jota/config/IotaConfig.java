package org.iota.jota.config;

import java.util.List;

import org.iota.jota.connection.Connection;
import org.iota.jota.store.IotaStore;

public interface IotaConfig {

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

    /**
     * The storage method we use for storing indexes and unsend transactions
     * @return
     */
    IotaStore getStore();
}
