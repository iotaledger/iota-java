package org.iota.jota.store;

import java.io.Serializable;
import java.util.List;

import org.iota.jota.types.Trits;

public class IotaClientStore implements PersistenceAdapter {

    protected Store store;
    
    public IotaClientStore(Store store) {
        this.store = store;
    }

    @Override
    public int getCurrentIndex(String seed) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void increaseIndex(String seed) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public int getIndexAndIncrease(String seed) {
        if (!canWrite()) return -1;
        
        Serializable index = store.get(seed);
        
        if (index == null) {
            store.set(seed, 1);
            return 0;
        } else if (!(index instanceof Integer)) {
            //Something went wrong
            return -1;
        }
        
        int cur = ((Integer)index).intValue();
        store.set(seed, cur+1);
        return cur;
    }
    
    @Override
    public void setPendingBundles(List<Trits> bundles) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Trits> getPendingBundles() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public boolean canWrite() {
        return store.canWrite();
    }
    
    @Override
    public String toString() {
        return store.toString();
    }
}