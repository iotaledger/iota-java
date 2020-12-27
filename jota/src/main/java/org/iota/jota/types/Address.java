package org.iota.jota.types;

public class Address {
    
    private final Hash addressHash;
    
    private final int index;
    
    private final int securityLevel;

    /**
     * 
     * @param addressHash Represents the address in the form of a hash.
     * @param index The index of an address.
     * @param securityLevel The securityLevel of an address.
     */
    public Address(Hash addressHash, int index, int securityLevel) {
        this.addressHash = addressHash;
        this.index = index;
        this.securityLevel = securityLevel;
    }

    public Hash getAddressHash() {
        return addressHash;
    }

    public int getIndex() {
        return index;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    @Override
    public String toString() {
        return "Address [address=" + addressHash + ", index=" + index + ", securityLevel=" + securityLevel + "]";
    }
}
