package org.iota.jota.account;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AccountStateImpl implements AccountState, Cloneable {
    
    private Map<Integer, String> indexes;

    public AccountStateImpl() {
        this.indexes = new HashMap<>();
    }
    
    /*
     * public void load(AddressGeneratorService addressService, AccountStore store) throws AccountLoadError {
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
    */

    @Override
    public AccountStateImpl clone() throws CloneNotSupportedException {
        return (AccountStateImpl) super.clone();
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
