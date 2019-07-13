package org.iota.jota.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;

public class Trits implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2929622764168608194L;
    
    //Mongodb bson parsing doesnt support arrays...
    private List<Integer> trits;
    
    /**
     * Used in json de/construction
     */
    @SuppressWarnings("unused")
    private Trits() {
        
    }
    
    public Trits(int[] trits) {
        if (!InputValidator.isTrits(trits)){
            throw new ArgumentException(Constants.INVALID_TRITS_INPUT_ERROR);
        }
        
        this.trits = new ArrayList<>();
        for (int i : trits) {
            this.trits.add(i);
        }
    }

    public Trits(List<Integer> trits) {
        if (!InputValidator.isTrits(trits)){
            throw new ArgumentException(Constants.INVALID_TRITS_INPUT_ERROR);
        }
        
        this.trits = trits;
    }

    public List<Integer> getTrits() {
        return trits;
    }
    
    @Override
    public String toString() {
        return new String(Arrays.toString(trits.toArray()));
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((trits == null) ? 0 : trits.hashCode());
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
        
        return obj.toString().equals(toString());
    }
}
