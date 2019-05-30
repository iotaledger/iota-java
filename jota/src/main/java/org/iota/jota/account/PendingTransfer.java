package org.iota.jota.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iota.jota.types.Hash;
import org.iota.jota.types.Trits;

/**
 * Pojo for original bundle trits and its tails created during broadcasting/promotion/reattachment
 */
public class PendingTransfer implements Serializable {

    private static final long serialVersionUID = 5549430894683695596L;

    /**
     * 
     */
    List<Trits> bundleTrits;

    /**
     * 
     */
    List<Hash> tailHashes;
    
    /**
     * Used in json de/construction
     */
    @SuppressWarnings("unused")
    private PendingTransfer() {
        
    }
    
    public PendingTransfer(List<Trits> bundle) {
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
    
    public List<Hash> getTailHashes() {
        return tailHashes;
    }
    
    public List<Trits> getBundleTrits() {
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
        result = prime * result + ((bundleTrits == null) ? 0 : bundleTrits.hashCode());
        result = prime * result + ((tailHashes == null) ? 0 : tailHashes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PendingTransfer other = (PendingTransfer) obj;
        if (bundleTrits == null) {
            if (other.bundleTrits != null) {
                return false;
            }
        } else if (!bundleTrits.equals(other.bundleTrits)) {
            return false;
        }
        if (tailHashes == null) {
            if (other.tailHashes != null) {
                return false;
            }
        } else if (!tailHashes.equals(other.tailHashes)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PendingTransfer [bundleTrits=" + bundleTrits + ", tailHashes=" + tailHashes + "]";
    }
    
    
}
