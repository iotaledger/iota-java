package org.iota.jota.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iota.jota.types.Hash;
import org.iota.jota.types.Trits;

public class PendingTransfer implements Serializable {

    private static final long serialVersionUID = 5549430894683695596L;

    /**
     * 
     */
    Trits[] bundleTrits;

    /**
     * 
     */
    List<Hash> tailHashes;
    
    public PendingTransfer() {
        
    }
    
    public PendingTransfer(Trits[] bundle) {
        this.bundleTrits = bundle;
    }
    
    /**
     * 
     * @param tailHash
     */
    public void addTail(Hash tailHash) {
        if (tailHashes == null) {
            tailHashes = new ArrayList<>();
        }
        
        tailHashes.add(tailHash);
    }
    
    /**
     * 
     * @return
     */
    public Hash[] getTailHashes(){
        return tailHashes.toArray(new Hash[tailHashes.size()]);
    }
    
    public Trits[] getBundleTrits() {
        return bundleTrits;
    }
    
    @Override
    public PendingTransfer clone() throws CloneNotSupportedException {
        return (PendingTransfer) super.clone();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(bundleTrits);
        result = prime * result + ((tailHashes == null) ? 0 : tailHashes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PendingTransfer other = (PendingTransfer) obj;
        if (!Arrays.equals(bundleTrits, other.bundleTrits))
            return false;
        if (tailHashes == null) {
            if (other.tailHashes != null)
                return false;
        } else if (!tailHashes.equals(other.tailHashes))
            return false;
        return true;
    }
}
