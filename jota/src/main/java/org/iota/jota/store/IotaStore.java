package org.iota.jota.store;

import java.util.List;

import org.iota.jota.model.Bundle;

public interface IotaStore {
    
    String BUNDLE = "bundle";
    
    /**
     * 
     * @param seed
     * @return
     */
    int getCurrentIndex(String seed);
    
    /**
     * 
     * @param seed
     */
    void increaseIndex(String seed);
    
    /**
     * 
     * @param seed
     * @return
     */
    int getIndexAndIncrease(String seed);
    
    /**
     * 
     * @return
     */
    List<Bundle> getPendingBundles();
    
    /**
     * 
     * @param bundles
     */
    void setPendingBundles(List<Bundle> bundles);
}
