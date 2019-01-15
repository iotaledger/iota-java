package org.iota.jota.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.iota.jota.types.Hash;
import org.iota.jota.types.Trits;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public Stream<Hash> getTailHashes(){
        return tailHashes.stream();
    }
    
    public Trits[] getBundleTrits() {
        return bundleTrits;
    }
    
    @Override
    public PendingTransfer clone() throws CloneNotSupportedException {
        return (PendingTransfer) super.clone();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(PendingTransfer.class)) {
            return false;
        }
        
        PendingTransfer pt = (PendingTransfer) obj;
        return pt.bundleTrits == bundleTrits
                && pt.tailHashes.equals(tailHashes);
    }
}
