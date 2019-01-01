package org.iota.jota.account;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.iota.jota.account.errors.AccountLoadError;
import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.account.services.AddressGeneratorService;
import org.iota.jota.store.PersistenceAdapter;

public class AccountState implements Cloneable {
    
    private Map<Integer, String> indexes;

    public AccountState() {
        this.indexes = new HashMap<>();
    }
    
    public void load(AddressGeneratorService addressService, PersistenceAdapter store) throws AccountLoadError {
        String indexesString = store.getIndexes();
        if (!indexesString.equals("")) {
            for (String indexString : indexesString.split(" ")) {
                try {
                    Integer index = Integer.parseInt(indexString);
                    indexes.put(index, addressService.get(index));
                } catch (AddressGenerationError e) {
                    throw new AccountLoadError(e);
                }
            }
        }
    }
    
    public void save(PersistenceAdapter store) {
        
    }

    @Override
    public AccountState clone() throws CloneNotSupportedException {
        return (AccountState) super.clone();
    }
    
    public Set<Integer> getIndexes() {
        return indexes.keySet();
    }

    public String getSeed() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getAddress(Integer index) {
        return indexes.get(index);
    }
    
    public void addAddress(String address, Integer index) {
        indexes.put(index, address);
    }
}
