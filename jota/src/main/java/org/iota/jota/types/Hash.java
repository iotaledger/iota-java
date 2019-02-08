package org.iota.jota.types;

import java.io.Serializable;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;

public class Hash  implements Serializable {

    private String hash;
    
    private Hash() {
        
    }

    public Hash(String hash) throws ArgumentException {
        if (!InputValidator.isHash(hash)){
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
}
