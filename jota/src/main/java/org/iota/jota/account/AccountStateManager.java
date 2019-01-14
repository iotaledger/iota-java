package org.iota.jota.account;

import org.iota.jota.account.deposits.methods.DepositFactory;
import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.account.services.AddressGeneratorService;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.store.PersistenceAdapter;
import org.iota.jota.types.Address;

public class AccountStateManager {

    private AccountStore store;
    private AccountState state;
    
    private AccountOptions options;
    private AddressGeneratorService addressService;
    
    
    public AccountStateManager(AccountState state, AddressGeneratorService addressService, AccountOptions options, AccountStore store) {
        this.state = state;
        this.addressService = addressService;
        
        this.store = store;
        this.options = options;
    }

    public AccountState getAccountState() {
        return state;
    }

    public void save() {
        //state.save(store);
    }
    
    public Address nextZeroValueAddress(int secLvl) throws AddressGenerationError {
        synchronized (this) {
            Address address = null;
            return address;
        }
    }

    

    public Address getInputAddress(int secLvl) {
        try {
            return nextZeroValueAddress(secLvl);
        } catch (AddressGenerationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
