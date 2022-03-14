package org.iota.jota.account;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.iota.jota.account.deposits.StoredDepositAddress;

public class AccountState implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8261579952650062417L;

    /**
     * Current key index
     */
    private int keyIndex;
    
    /**
     * Map of key indexes and their corresponding stored deposit
     */
    private Map<Integer, StoredDepositAddress> depositRequests;
    
    /**
     * 
     */
    private Map<String, PendingTransfer> pendingTransfers;
    
    public AccountState() {
        pendingTransfers = new HashMap<>();
        depositRequests = new HashMap<>();
    }
    
    
    
    public AccountState(int keyIndex, Map<Integer, StoredDepositAddress> depositRequests,
            Map<String, PendingTransfer> pendingTransfers) {
        this.keyIndex = keyIndex;
        this.depositRequests = depositRequests;
        this.pendingTransfers = pendingTransfers;
    }



    /**
     * 
     * @param index keyIndex
     * @param request request to store
     */
    public void addDepositRequest(int index, StoredDepositAddress request){
        depositRequests.put(index, request);
    }
    
    public void removeDepositRequest(int index) {
        depositRequests.remove(index);
    }
    
    /**
     * 
     * @param hash
     * @param request
     */
    public void addPendingTransfers(String hash, PendingTransfer request){
        pendingTransfers.put(hash, request);
    }
    
    public void removePendingTransfer(String hash) {
        pendingTransfers.remove(hash);
    }
    
    public boolean isNew() {
        return depositRequests.size() == 0 && pendingTransfers.size() == 0;
    }
    
    public Map<Integer, StoredDepositAddress> getDepositRequests() {
        return depositRequests;
    }
    
    public Map<String, PendingTransfer> getPendingTransfers() {
        return pendingTransfers;
    }
    
    public PendingTransfer getPendingTransfer(String hash) {
        return pendingTransfers.get(hash);
    }
    
    @Override
    public AccountState clone() throws CloneNotSupportedException {
        AccountState newState = new AccountState();
        newState.keyIndex = keyIndex;
        
        if (null != depositRequests) {
            newState.depositRequests = new HashMap<>();
            for (Map.Entry<Integer, StoredDepositAddress> entry : depositRequests.entrySet()) {
                newState.depositRequests.put(entry.getKey(), entry.getValue().clone());
            }
        }
        
        if (null != pendingTransfers) {
            newState.pendingTransfers = new HashMap<>();
            for (Map.Entry<String, PendingTransfer> entry : pendingTransfers.entrySet()) {
                newState.pendingTransfers.put(entry.getKey(), entry.getValue().clone());
            }
        }
        
        return newState;
    }
    
    public void setKeyIndex(int keyIndex) {
        this.keyIndex = keyIndex;
    }
    
    public int getKeyIndex() {
        return this.keyIndex;
    }
    
    @Override
    public String toString() {
        return "AccountState [keyIndex=" + keyIndex + ", depositRequests=" + depositRequests + ", pendingTransfers="
                + pendingTransfers + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(AccountState.class)) {
            return false;
        }
        
        AccountState as = (AccountState) obj;
        return as.keyIndex == keyIndex
                && Objects.equals(depositRequests, as.depositRequests)
                && Objects.equals(pendingTransfers, as.pendingTransfers);
    }

    //TODO: SonarLint-Rule: "equals(Object obj)" and "hashCode()" should be overridden in pairs. Therefore also overrie "hashCode()".
}
