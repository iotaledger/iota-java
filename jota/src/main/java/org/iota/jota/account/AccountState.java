package org.iota.jota.account;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.iota.jota.account.deposits.DepositRequest;

public class AccountState implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8261579952650062417L;

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
        
        if (null != depositRequests) {
            newState.depositRequests = new HashMap<>();
            for (Long key : depositRequests.keySet()) {
                newState.depositRequests.put(key, depositRequests.get(key).clone());
            }
        }
        
        if (null != pendingTransfers) {
            newState.pendingTransfers = new HashMap<>();
            for (String key : pendingTransfers.keySet()) {
                newState.pendingTransfers.put(key, pendingTransfers.get(key).clone());
            }
        }
        
        return newState;
    }
    
    @Override
    public String toString() {
        return "AccountState [keyIndex=" + keyIndex + ", depositRequests=" + depositRequests + ", pendingTransfers="
                + pendingTransfers + "]";
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("EQUALS check");
        System.out.println(obj);
        System.out.println(this);
        
        if (!obj.getClass().equals(AccountState.class)) {
            return false;
        }
        
        AccountState as = (AccountState) obj;
        return as.keyIndex == keyIndex;
    }
}
