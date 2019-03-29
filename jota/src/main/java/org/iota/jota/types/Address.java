package org.iota.jota.types;

public class Address {
    
    private Hash address;
    
    private int index;
    
    private int securityLevel;

    /**
     * 
     * @param address
     * @param index
     * @param securityLevel
     */
    public Address(Hash address, int index, int securityLevel) {
        this.address = address;
        this.index = index;
        this.securityLevel = securityLevel;
    }

    public Hash getAddress() {
        return address;
    }

    public int getIndex() {
        return index;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    @Override
    public String toString() {
        return "Address [address=" + address + ", index=" + index + ", securityLevel=" + securityLevel + "]";
    }
}
