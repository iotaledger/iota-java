package org.iota.jota.account;

import java.util.Map;

import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.model.Bundle;
import org.iota.jota.store.Store;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trytes;

public class AccountStoreImpl implements AccountStore {

    private Store store;

    public AccountStoreImpl(Store store) {
        if (!store.canWrite()) {
            throw new IllegalArgumentException("Accoutns requires a writeable store");
        }
        
        this.store = store;
    }

    @Override
    public AccountState LoadAccount(String id) {
        return store.get(id, new AccountState());
    }

    @Override
    public void RemoveAccount(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public int ReadIndex(String id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeIndex(String id, int index) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addDepositRequest(String id, int index, DepositRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeDepositRequest(String id, int index) {
        // TODO Auto-generated method stub

    }
    
    @Override
    public Map<Integer, DepositRequest> getDepositRequests(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addPendingTransfer(String id, Hash tailTx, Trytes[] bundleTrytes, int... indices) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removePendingTransfer(String id, Hash tailHash) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addTailHash(String id, Hash tailHash, Hash newTailTxHash) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Bundle> getPendingTransfers(String id) {
        // TODO Auto-generated method stub
        return null;
    }
}
