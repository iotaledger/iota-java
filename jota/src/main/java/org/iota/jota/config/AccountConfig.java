package org.iota.jota.config;

import org.iota.jota.account.AccountStore;

public interface AccountConfig extends Config {
    
    /**
     * 
     * @return
     */
    int getMwm();
    
    /**
     * 
     * @return
     */
    int getDepth();
    
    /**
     * 
     * @return
     */
    int getSecurityLevel();
    
    /**
     * The storage method we use for storing indexes and unsend transactions
     * @return
     */
    AccountStore getStore();
}
