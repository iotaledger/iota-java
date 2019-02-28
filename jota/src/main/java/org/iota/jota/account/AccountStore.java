package org.iota.jota.account;

import java.util.Map;

import org.iota.jota.account.deposits.StoredDepositRequest;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trytes;

public interface AccountStore {

    AccountState loadAccount(String id);
    
    void saveAccount(String id, AccountState state);
    
    void removeAccount(String id);
    
    int readIndex(String id);
    
    void writeIndex(String id, int index);
    
    void addDepositRequest(String id, int index, StoredDepositRequest request);
    
    void removeDepositRequest(String id, int index);
    
    Map<Integer, StoredDepositRequest> getDepositRequests(String id);
    
    /**
     * Adds transaction as pending
     * Automatically adds a tail to this pending as well.
     * 
     * @param id
     * @param tailTx
     * @param bundleTrytes
     * @param indices indexes to remove for this transfer, currently unused since we remove when we ask for inputs
     */
    void addPendingTransfer(String id, Hash tailTx, Trytes[] bundleTrytes, int... indices);
    
    void removePendingTransfer(String id, Hash tailHash );
    
    void addTailHash(String id, Hash tailHash, Hash newTailTxHash);
    
    Map<String, PendingTransfer> getPendingTransfers(String id);
}
