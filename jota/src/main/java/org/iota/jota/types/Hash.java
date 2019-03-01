package org.iota.jota.types;

import java.io.Serializable;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;

public class Hash implements Serializable {

    private String hash;
    private String hashCheckSum;
    
    private Hash() {
        
    }

    public Hash(String hash) throws ArgumentException {
        if (InputValidator.isAddress(hash)) {
            hashCheckSum = hash;
            hash = hash.substring(0, 81);
        } else if (!InputValidator.isHash(hash)){
            throw new ArgumentException(Constants.INVALID_HASH_INPUT_ERROR);
        }
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
    
    @Override
    public String toString() {
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(Hash.class) && obj.toString().equals(toString());
    }
    
    public String getWithChecksum() {
        if (null != hashCheckSum) {
            return hashCheckSum;
        }
        return hashCheckSum = Checksum.addChecksum(hash);
    }
}
