package org.iota.jota.types;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;

public class Trytes {

    private String trytesString;
    
    public Trytes(String trytes) {
        if (!InputValidator.isTrytes(trytes)){
            throw new ArgumentException(Constants.INVALID_TRYTES_INPUT_ERROR);
        }
        
        this.trytesString = trytes;
    }
    
    public String getTrytesString() {
        return trytesString;
    }

    @Override
    public String toString() {
        return trytesString;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((trytesString == null) ? 0 : trytesString.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Trytes other = (Trytes) obj;
        if (trytesString == null) {
            if (other.trytesString != null) {
                return false;
            }
        } else if (!trytesString.equals(other.trytesString)) {
            return false;
        }
        return true;
    }
    
    
}
