package org.iota.jota.types;

import java.io.Serializable;
import java.util.Arrays;

public class Trits implements Serializable {
    
    private int[] trits;

    public Trits(int[] trits) {
        this.trits = trits;
    }

    public int[] getTrits() {
        return trits;
    }
    
    @Override
    public String toString() {
        return new String(Arrays.toString(trits));
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        
        return obj.toString().equals(toString());
    }
}
