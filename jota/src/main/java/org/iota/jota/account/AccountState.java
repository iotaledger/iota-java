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

    private long keyIndex;
    
    private Map<Long, DepositRequest> depositRequests;
    
    private Map<String, PendingTransfer> pendingTransfers;
    
    public AccountState() {
        pendingTransfers = new HashMap<>();
        depositRequests = new HashMap<>();
    }
    
    /**
     * 
     * @param index
     * @param request
     */
    public void addDepositRequest(long index, DepositRequest request){
        depositRequests.put(index, request);
    }
    
    /**
     * 
     * @param hash
     * @param request
     */
    public void addPendingTransfers(String hash, PendingTransfer request){
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
    
    public void setKeyIndex(long keyIndex) {
        this.keyIndex = keyIndex;
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
        
        if (obj == null || !obj.getClass().equals(AccountState.class)) {
            return false;
        }
        
        AccountState as = (AccountState) obj;
        return as.keyIndex == keyIndex
                && Objects.equals(depositRequests, as.depositRequests)
                && Objects.equals(pendingTransfers, as.pendingTransfers);
    }
}
