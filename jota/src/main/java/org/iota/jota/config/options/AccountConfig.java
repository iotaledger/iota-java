package org.iota.jota.config.options;

import org.iota.jota.account.AccountStore;
import org.iota.jota.config.Config;

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
