package org.iota.jota.types;

import java.io.Serializable;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;

public class Hash implements Serializable {

    private static final long serialVersionUID = -5040410304130966841L;
    
    private String hash;
    private transient String hashCheckSum;
    
    /**
     * Used in json de/construction
     */
    @SuppressWarnings("unused")
    private Hash() {
        
    }

    public Hash(String hash) throws ArgumentException {
        if (InputValidator.isAddress(hash)) {
            hashCheckSum = hash;
            this.hash = hash.substring(0, 81);
        } else if (!InputValidator.isHash(hash)){
            throw new ArgumentException(Constants.INVALID_HASH_INPUT_ERROR);
        } else {
            this.hash = hash;
        }
    }

    public String getHash() {
        return hash;
    }
    
    public String getHashCheckSum() {
        if (null != hashCheckSum) {
            return hashCheckSum;
        }
        return hashCheckSum = Checksum.addChecksum(hash);
    }
    
    @Override
    public String toString() {
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(Hash.class) && hash.equals(((Hash)obj).hash);
    }
}
