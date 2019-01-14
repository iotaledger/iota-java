package org.iota.jota.account;

import java.util.HashMap;
import java.util.Map;

import org.iota.jota.account.deposits.DepositRequest;

public class AccountState {

    long keyIndex;
    
    Map<Long, DepositRequest> depositRequests;
    
    Map<String, PendingTransfer> pendingTransfers;
    
    public AccountState() {
        
    }
    
    /**
     * 
     * @param index
     * @param request
     */
    public void addDepositRequest(long index, DepositRequest request){
        if (null == depositRequests) {
            depositRequests = new HashMap<>();
        }
        
        depositRequests.put(index, request);
    }
    
    /**
     * 
     * @param hash
     * @param request
     */
    public void addPendingTransfers(String hash, PendingTransfer request){
        if (null == pendingTransfers) {
            pendingTransfers = new HashMap<>();
        }
        
        pendingTransfers.put(hash, request);
    }
    
    boolean isNew() {
        return depositRequests.size() == 0 && pendingTransfers.size() == 0;
    }
    
    @Override
    public AccountState clone() throws CloneNotSupportedException {
        AccountState newState = new AccountState();
        newState.keyIndex = keyIndex;
        newState.depositRequests = new HashMap<>();
        for (Long key : depositRequests.keySet()) {
            newState.depositRequests.put(key, depositRequests.get(key).clone());
        }
        
        newState.pendingTransfers = new HashMap<>();
        for (String key : pendingTransfers.keySet()) {
            newState.pendingTransfers.put(key, pendingTransfers.get(key).clone());
        }
        
        return newState;
    }
}
