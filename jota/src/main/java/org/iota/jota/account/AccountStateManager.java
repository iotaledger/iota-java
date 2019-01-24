package org.iota.jota.account;

import java.util.List;
import java.util.Map;

import org.iota.jota.account.addressgenerator.AddressGeneratorServiceImpl;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.StoredDepositRequest;
import org.iota.jota.account.errors.AccountError;
import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.account.inputselector.InputSelectionStrategy;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.model.Input;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trytes;

public class AccountStateManager {

    private static volatile Object lock = new Object();
    
    private AccountStore store;
    private AccountState state;
    
    private AccountOptions options;
    
    private AddressGeneratorServiceImpl addressService;
    
    private InputSelectionStrategy inputSelector;

    private String accountId;
    private AccountBalanceCache cache;
    
    public AccountStateManager(AccountBalanceCache cache,
                               String accountId,
                               InputSelectionStrategy inputSelector, 
                               AccountState state, 
                               AddressGeneratorServiceImpl addressService, 
                               AccountOptions options, 
                               AccountStore store) {
        this.cache = cache;
        this.accountId = accountId;
        
        this.inputSelector = inputSelector;
        this.state = state;
        this.addressService = addressService;
        
        this.store = store;
        this.options = options;
        
        
    }

    public AccountState getAccountState() {
        return store.loadAccount(accountId);
    }

    public void save() {
        store.saveAccount(accountId, state);
    }
    
    public Hash nextZeroValueAddress() throws AddressGenerationError {
        synchronized (lock) {
            return new Hash(cache.first().getAddress());
        }
    }
    
    /**
     * Creates a remainder input.
     * This remainder is added to the store, the store is saved.
     * It is also added to the balance cache.
     * 
     * @param remainder the remainder we have left from the inputs
     * @return 
     */
    public Input createRemainder(long remainder) {
        synchronized (lock) {
            int key;
            DepositRequest deposit;
            
            // Editing store will edit the underlying state, and take care of saving
            
            key = state.getKeyIndex();
            store.writeIndex(accountId, key+1);
            
            deposit = new DepositRequest(null, false, remainder);
            
            addDepositRequest(key, new StoredDepositRequest(deposit, options.getSecurityLevel()));
        
        
            Input remainderInput = new Input(
                addressService.get(key).getAddress().getHash(), 
                remainder, 
                key, 
                options.getSecurityLevel()
            );
            
            cache.addBalance(remainderInput, deposit);
            
            return remainderInput;
        }
    }
    
    /**
     * Calculates the required inputs for the given balance.
     * All inputs returned are marked as used in storage. 
     * 
     * @param value required value
     * @return
     */
    public List<Input> getInputAddresses(long value) throws AccountError {
        synchronized (lock) {
            List<Input> inputs = inputSelector.getInput(value, false);
        
        
            for (Input i : inputs) {
                store.removeDepositRequest(accountId, i.getKeyIndex());
                cache.removeInput(i);
            }

            return inputs;
        }
    }
    
    public long getTotalBalance() {
        return cache.getTotalBalance();
    }
    
    public long getUsableBalance() {
        return inputSelector.getUsableBalance();
    }

    public boolean isNew() {
        return state.isNew();
    }
    
    //
    // Section for store method wrappers over the stored accountId
    //
    
    public int readIndex() {
        return state.getKeyIndex();
    }
    
    public void writeIndex(int index) {
        store.writeIndex(accountId, index);
    }
    
    public void addDepositRequest(int index, StoredDepositRequest request) {
        store.addDepositRequest(accountId, index, request);
    }
    
    public void removeDepositRequest(int index) {
        store.removeDepositRequest(accountId, index);
    }
    
    public Map<Integer, StoredDepositRequest> getDepositRequests(){
        return store.getDepositRequests(accountId);
    }
    
    public void addPendingTransfer(Hash tailTx, Trytes[] bundleTrytes, int... indices) {
        store.addPendingTransfer(accountId, tailTx, bundleTrytes, indices);
    }
    
    public void removePendingTransfer(Hash tailHash ) {
        store.removePendingTransfer(accountId, tailHash);
    }
    
    public void addTailHash(Hash tailHash, Hash newTailTxHash) {
        store.addTailHash(accountId, tailHash, newTailTxHash);
    }
    
    public Map<String, PendingTransfer> getPendingTransfers(){
        return store.getPendingTransfers(accountId);
    }
}
