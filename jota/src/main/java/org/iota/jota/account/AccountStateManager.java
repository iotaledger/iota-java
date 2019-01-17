package org.iota.jota.account;

import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.account.inputselector.InputSelectionStrategy;
import org.iota.jota.account.services.AddressGeneratorService;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.types.Address;

public class AccountStateManager {

    private AccountStore store;
    private AccountState state;
    
    private AccountOptions options;
    
    private AddressGeneratorService addressService;
    
    private InputSelectionStrategy inputSelector;
    
    
    public AccountStateManager(InputSelectionStrategy inputSelector, 
                               AccountState state, 
                               AddressGeneratorService addressService, 
                               AccountOptions options, 
                               AccountStore store) {
        
        this.inputSelector = inputSelector;
        this.state = state;
        this.addressService = addressService;
        
        this.store = store;
        this.options = options;
    }

    public AccountState getAccountState() {
        return state;
    }

    public void save() {
        
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
