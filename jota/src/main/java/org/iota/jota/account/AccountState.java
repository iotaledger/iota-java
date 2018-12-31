package org.iota.jota.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.iota.jota.pow.ICurl;
import org.iota.jota.store.PersistenceAdapter;
import org.iota.jota.utils.IotaAPIUtils;

public class AccountState implements Cloneable {
    
    private Map<Integer, String> indexes;

    public AccountState(ICurl iCurl, PersistenceAdapter store) {
        this.indexes = new HashMap<>();
        for (String indexString : store.getIndexes().split(" ")) {
            try {
                Integer index = Integer.parseInt(indexString);
                indexes.push(index, IotaAPIUtils.newAddress(seed, security, index, checksum, curl));
            } catch (NumberFormatException e) {
                //Broken number!!
            }
        }
    }
    
    public void save(PersistenceAdapter store) {
        
    }

    @Override
    public AccountState clone() throws CloneNotSupportedException {
        return (AccountState) super.clone();
    }
    
    public ArrayList<Integer> getIndexes() {
        return indexList;
    }

    public String getSeed() {
        // TODO Auto-generated method stub
        return null;
    }
}
