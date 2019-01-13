package org.iota.jota.account;

import java.util.Map;

import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.model.Bundle;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trytes;

public interface AccountStore {

    AccountState LoadAccount(String id);
    
    void RemoveAccount(String id);
    
    int ReadIndex(String id);
    
    void writeIndex(String id, int index);
    
    void addDepositRequest(String id, int index, DepositRequest request);
    
    void removeDepositRequest(String id, int index);
    
    Map<Integer, DepositRequest> getDepositRequests(String id);
    
    void addPendingTransfer(String id, Hash tailTx, Trytes[] bundleTrytes, int... indices);
    
    void removePendingTransfer(String id, Hash tailHash );
    
    void addTailHash(String id, Hash tailHash, Hash newTailTxHash);
    
    Map<String, Bundle> getPendingTransfers(String id);
}
