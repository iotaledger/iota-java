package org.iota.jota.account;

import java.util.List;

import org.iota.jota.account.addressgenerator.AddressGeneratorServiceImpl;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.StoredDepositRequest;
import org.iota.jota.account.errors.AccountError;
import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.account.inputselector.InputSelectionStrategy;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.model.Input;
import org.iota.jota.types.Hash;

public class AccountStateManager {

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
        synchronized (this) {
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
        int key;
        DepositRequest deposit;
        
        synchronized (state) {
            // Editing store will edit the underlying state, and take care of saving
            
            key = state.getKeyIndex();
            store.writeIndex(accountId, key+1);
            
            deposit = new DepositRequest(null, false, remainder);
            
            store.addDepositRequest(accountId, key, new StoredDepositRequest(deposit, options.getSecurityLevel()));
        }
        
        Input remainderInput = new Input(
            addressService.get(key).getAddress().getHash(), 
            remainder, 
            key, 
            options.getSecurityLevel()
        );
        
        cache.addBalance(remainderInput, deposit);
        
        return remainderInput;
    }
    
    /**
     * Calculates the required inputs for the given balance.
     * All inputs returned are marked as used in storage. 
     * 
     * @param value required value
     * @return
     */
    public List<Input> getInputAddresses(long value) throws AccountError {
        List<Input> inputs = inputSelector.getInput(value, false);
        
        for (Input i : inputs) {
            store.removeDepositRequest(accountId, i.getKeyIndex());
            cache.removeInput(i);
        }
        
        return inputs;
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
}
