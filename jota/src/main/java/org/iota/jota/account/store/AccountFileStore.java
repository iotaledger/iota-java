package org.iota.jota.account.store;

import java.io.File;
import java.util.Date;
import java.util.Map;

import org.iota.jota.account.AccountState;
import org.iota.jota.account.ExportedAccountState;
import org.iota.jota.account.PendingTransfer;
import org.iota.jota.account.deposits.StoredDepositAddress;
import org.iota.jota.store.IotaFileStore;
import org.iota.jota.store.JsonFlatFileStore;
import org.iota.jota.store.Store;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trytes;

/**
 * 
 * Terribly inefficient storage method which reads/writes all to disk every time you modify.
 * Can be optimized by caching yml after modify, or only loading in the current account instead of all file data.
 *
 */
public class AccountFileStore extends AccountStoreImpl {

    private Store store;
    
    public AccountFileStore() {
        this.store = new IotaFileStore();
    }
    
    public AccountFileStore(File file) {
        this.store = new JsonFlatFileStore(file);
    }

    /**
     * Takes any store, but make sure the store allows saving of POJOs, and not just key/value Strings
     * @param fileStore Storage we use for reading/writing our account data
     */
    public AccountFileStore(Store fileStore) {
        this.store = fileStore;
    }

    public AccountFileStore(String file) {
        this.store = new IotaFileStore(file);
    }

    @Override
    public void load() throws Exception {
        this.store.load();
    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public void shutdown() {
        try {
            store.save(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public AccountState loadAccount(String id) {
        AccountState state = store.get(id, null);

        if (state == null) {
            saveAccount(id, state = new AccountState());
        }
        return state;
    }

    @Override
    public void saveAccount(String id, AccountState state) {
        store.set(id, state);
        save();
    }

    @Override
    public void removeAccount(String id) {
        store.delete(id);
        save();
    }

    @Override
    public int readIndex(String id) {
        return loadAccount(id).getKeyIndex();
    }

    @Override
    public void writeIndex(String id, int index) {
        loadAccount(id).setKeyIndex(index);
        save();
    }

    @Override
    public void addDepositAddress(String id, int index, StoredDepositAddress request) {
        loadAccount(id).addDepositRequest(index, request);
        save();
    }

    @Override
    public void removeDepositAddress(String id, int index) {
        loadAccount(id).removeDepositRequest(index);
        save();
    }

    @Override
    public Map<Integer, StoredDepositAddress> getDepositAddresses(String id) {
        return loadAccount(id).getDepositRequests();
    }

    @Override
    public void addPendingTransfer(String id, Hash tailTx, Trytes[] bundleTrytes, int... indices) {
        PendingTransfer pendingTransfer = new PendingTransfer(trytesToTrits(bundleTrytes));
        pendingTransfer.addTail(tailTx);
        
        loadAccount(id).addPendingTransfers(tailTx.getHash(), pendingTransfer);
        save();
    }

    @Override
    public void removePendingTransfer(String id, Hash tailHash) {
        loadAccount(id).removePendingTransfer(tailHash.getHash());
        save();
    }

    @Override
    public void addTailHash(String id, Hash tailHash, Hash newTailTxHash) {
        loadAccount(id).getPendingTransfer(tailHash.getHash()).addTail(newTailTxHash);
        save();
    }

    @Override
    public Map<String, PendingTransfer> getPendingTransfers(String id) {
        return loadAccount(id).getPendingTransfers();
    }

    @Override
    public void importAccount(ExportedAccountState state) {
        saveAccount(state.getId(), state.getState());
    }

    @Override
    public ExportedAccountState exportAccount(String id) {
        AccountState state = loadAccount(id);
        return new ExportedAccountState(new Date(), id, state);
    }
    
    private void save() {
        try {
            store.save(false);
        } catch (Exception e) {
            // TODO log account error
            e.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        return store.toString();
    }
}
