package org.iota.jota.account.addressgenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.iota.jota.account.AccountOptions;
import org.iota.jota.account.errors.AddressGenerationError;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.types.Address;
import org.iota.jota.types.Hash;
import org.iota.jota.utils.IotaAPIUtils;

public class AddressGeneratorServiceImpl implements AddressGeneratorService {
    
    // Maybe later: https://github.com/ben-manes/caffeine
    private Map<IndexSec, Hash> map;
    
    private AccountOptions options;

    public AddressGeneratorServiceImpl(AccountOptions options) {
        this(options, true);
    }
    
    public AddressGeneratorServiceImpl(AccountOptions options, boolean cache) {
        this.options = options;
        
        if (cache) {
            map = new ConcurrentHashMap<>();
        }
    }
    
    public Address get(int index) throws AddressGenerationError {
        return this.get(index, options.getSecurityLevel());
    }

    @Override
    public Address get(int index, int securityLevel) throws AddressGenerationError {
        try {
            Hash hash = null;
            boolean add = false;
            
            if (null != map) {
                hash = map.get(new IndexSec(index, securityLevel));
                add = hash == null;
            }
            
            if (null == hash) {
                hash = new Hash(IotaAPIUtils.newAddress(options.getSeed().getSeed().getTrytesString(), 
                    securityLevel, 
                    index, false,
                    options.getApi().getCurl()));
            }
            
            if (add) {
                map.put(new IndexSec(index, securityLevel), hash);
            }
            
            return new Address(hash, index, securityLevel);
        } catch (ArgumentException e) {
            throw new AddressGenerationError(e);
        }
    }
    
    @Override
    public int getSecurityLevel() {
        return options.getSecurityLevel();
    }

    private class IndexSec {
        public final int index, security;

        IndexSec(int index, int security) {
            this.index = index;
            this.security = security;
        }

        public int hashCode() {
            return index ^ security;
        }

        public boolean equals(Object o) {
            IndexSec that;
            return o instanceof IndexSec && (that = (IndexSec) o).index == index && that.security == security;
        }
    }
}
