package org.iota.jota.account;

import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.account.services.AddressGeneratorService;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.store.PersistenceAdapter;

public class AccountStateManager {

    private PersistenceAdapter store;
    private AccountState state;
    
    private AccountOptions options;
    private AddressGeneratorService addressService;
    
    public AccountStateManager(AccountState state, AddressGeneratorService addressService, AccountOptions options, PersistenceAdapter store) {
        this.state = state;
        this.addressService = addressService;
        
        this.store = store;
        this.options = options;
    }

    public AccountState getAccountState() {
        return state;
    }

    public void save() {
        state.save(store);
    }
    
    public String nextZeroValueAddress(int secLvl) throws AddressGenerationError {
        synchronized (this) {
            for (Integer index : state.getIndexes()) {
                if (index > 0) {
                    // Spent
                } else {
                    // We can use it!
                    return state.getAddress(index);
                }
            }
            
            System.out.println(store);
            System.out.println(options);
            int newIndex = store.getIndexAndIncrease(options.getSeed());
            // We have none available, lets make one!
            String address = addressService.get(newIndex);
            
            // Zero value doesn't sign, so no risk of fund loss
            state.addAddress(address, -newIndex);
            return address;
        }
    }

    

    public String getInputAddress(int secLvl) {
        try {
            return nextZeroValueAddress(secLvl);
        } catch (AddressGenerationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
