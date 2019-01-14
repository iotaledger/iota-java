package org.iota.jota.account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.iota.jota.types.Hash;
import org.iota.jota.types.Trits;

public class PendingTransfer {
    
    /**
     * 
     */
    Trits[] bundle;

    /**
     * 
     */
    List<Hash> tailHashes;
    
    public PendingTransfer(Trits[] bundle) {
        this.bundle = bundle;
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
    public Stream<Hash> getHashes(){
        return tailHashes.stream();
    }
    
    @Override
    public PendingTransfer clone() throws CloneNotSupportedException {
        return (PendingTransfer) super.clone();
    }
}
