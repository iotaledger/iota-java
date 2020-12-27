package org.iota.jota.types;

import java.io.Serializable;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;

public class Hash implements Serializable {

    private static final long serialVersionUID = -5040410304130966841L;
    
    private String hashString;
    private transient String hashCheckSum;
    
    /**
     * Used in json de/construction
     */
    @SuppressWarnings("unused")
    private Hash() {
        
    }

    public Hash(String hashString) {
        if (InputValidator.isAddress(hashString)) {
            hashCheckSum = hashString;
            this.hashString = hashString.substring(0, 81);
        } else if (!InputValidator.isHash(hashString)){
            throw new ArgumentException(Constants.INVALID_HASH_INPUT_ERROR);
        } else {
            this.hashString = hashString;
        }
    }

    public String getHashString() {
        return hashString;
    }
    
    public String getHashCheckSum() {
        if (null == hashCheckSum) {
            hashCheckSum = Checksum.addChecksum(hashString);
        }
        return hashCheckSum;
    }
    
    @Override
    public String toString() {
        return hashString;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(Hash.class) && hashString.equals(((Hash)obj).hashString);
    }
}
