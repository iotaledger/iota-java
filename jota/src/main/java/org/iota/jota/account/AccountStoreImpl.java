package org.iota.jota.account;

import java.util.Map;

import org.iota.jota.account.deposits.StoredDepositRequest;
import org.iota.jota.store.Store;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trits;
import org.iota.jota.types.Trytes;
import org.iota.jota.utils.Converter;

public class AccountStoreImpl implements AccountStore {

    private Store store;

    public AccountStoreImpl(Store store) {
        if (!store.canWrite()) {
            throw new IllegalArgumentException("Accounts requires a writeable store");
        }
        
        this.store = store;
    }

    @Override
    public AccountState loadAccount(String id) {
        AccountState state = store.get(id);
        
        if (state == null) {
            state = new AccountState();
            store.set(id, state);
            save();
        }
        
        return state;
    }
    
    @Override
    public void saveAccount(String id, AccountState state) {
        store.set(id, state);
        save();
    }

    @Override
    public void RemoveAccount(String id) {
        store.set(id, null);
    }

    @Override
    public int ReadIndex(String id) {
        AccountState state = loadAccount(id);
        return state.getKeyIndex();
    }

    @Override
    public void writeIndex(String id, int index) {
        AccountState state = loadAccount(id);
        state.setKeyIndex(index);
        
        save();
    }

    @Override
    public void addDepositRequest(String id, int index, StoredDepositRequest request) {
        AccountState state = loadAccount(id);
        state.addDepositRequest(index, request);
        
        save();
    }

    @Override
    public void removeDepositRequest(String id, int index) {
        AccountState state = loadAccount(id);
        state.removeDepositRequest(index);
        
        save();
    }
    
    @Override
    public Map<Integer, StoredDepositRequest> getDepositRequests(String id) {
        AccountState state = loadAccount(id);
        return state.getDepositRequests();
    }

    @Override
    public void addPendingTransfer(String id, Hash tailTx, Trytes[] bundleTrytes, int... indices) {
        AccountState state = loadAccount(id);
        
        Trits[] trits = new Trits[bundleTrytes.length];
        for (int i=0; i<bundleTrytes.length; i++) {
            Trytes t = bundleTrytes[i];
            Converter.trits(t.toString());
        }
        
        PendingTransfer transfer = new PendingTransfer(trits);
        transfer.addTail(tailTx);
        
        state.addPendingTransfers(tailTx.getHash(), transfer);
        
        save();
    }

    @Override
    public void removePendingTransfer(String id, Hash tailHash) {
        AccountState state = loadAccount(id);
        state.removePendingTransfer(tailHash.toString());
        
        save();
    }

    @Override
    public void addTailHash(String id, Hash tailHash, Hash newTailTxHash) {
        AccountState state = loadAccount(id);
        PendingTransfer transfer = state.getPendingTransfer(tailHash.toString());
        if (transfer != null) {
            transfer.addTail(newTailTxHash);
        }
        
        save();
    }

    @Override
    public Map<String, PendingTransfer> getPendingTransfers(String id) {
        AccountState state = loadAccount(id);
        return state.getPendingTransfers();
    }
    
    private void save() {
        try {
            store.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
