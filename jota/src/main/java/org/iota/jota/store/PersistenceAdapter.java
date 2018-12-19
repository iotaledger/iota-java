package org.iota.jota.store;

import java.util.List;

import org.iota.jota.types.Trits;

public interface PersistenceAdapter {
    
    String BUNDLE = "bundle";
    
    //PersistenceAdapter open(String id);
    
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
    List<Trits> getPendingBundles();
    
    /**
     * 
     * @param bundles
     */
    void setPendingBundles(List<Trits> bundles);
}
