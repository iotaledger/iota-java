package org.iota.jota.types;

public class Trits {
    
    private String trits;

    public Trits(String trits) {
        this.trits = trits;
    }

    public String getTrits() {
        return trits;
    }
    
    @Override
    public String toString() {
        return new String(trits);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(Trits.class)) {
            return false;
        }
        return obj.toString().equals(toString());
    }
}
