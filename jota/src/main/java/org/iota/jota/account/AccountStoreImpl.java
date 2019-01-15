package org.iota.jota.account;

import java.util.Map;

import org.iota.jota.account.deposits.DepositRequest;
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
    public AccountState LoadAccount(String id) {
        AccountState state = store.get(id);
        
        if (state == null) {
            state = new AccountState();
            store.set(id, state);
            save();
        }
        
        return state;
    }

    @Override
    public void RemoveAccount(String id) {
        store.set(id, null);
    }

    @Override
    public long ReadIndex(String id) {
        AccountState state = LoadAccount(id);
        return state.getKeyIndex();
    }

    @Override
    public void writeIndex(String id, long index) {
        AccountState state = LoadAccount(id);
        state.setKeyIndex(index);
        
        save();
    }

    @Override
    public void addDepositRequest(String id, long index, DepositRequest request) {
        AccountState state = LoadAccount(id);
        state.addDepositRequest(index, request);
        
        save();
    }

    @Override
    public void removeDepositRequest(String id, long index) {
        AccountState state = LoadAccount(id);
        state.removeDepositRequest(index);
        
        save();
    }
    
    @Override
    public Map<Long, DepositRequest> getDepositRequests(String id) {
        AccountState state = LoadAccount(id);
        return state.getDepositRequests();
    }

    @Override
    public void addPendingTransfer(String id, Hash tailTx, Trytes[] bundleTrytes, int... indices) {
        AccountState state = LoadAccount(id);
        
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
        AccountState state = LoadAccount(id);
        state.removePendingTransfer(tailHash.toString());
        
        save();
    }

    @Override
    public void addTailHash(String id, Hash tailHash, Hash newTailTxHash) {
        AccountState state = LoadAccount(id);
        PendingTransfer transfer = state.getPendingTransfer(tailHash.toString());
        if (transfer != null) {
            transfer.addTail(newTailTxHash);
        }
        
        save();
    }

    @Override
    public Map<String, PendingTransfer> getPendingTransfers(String id) {
        AccountState state = LoadAccount(id);
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
