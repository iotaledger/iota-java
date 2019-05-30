package org.iota.jota.account.deposits;

import java.util.Objects;

public class StoredDepositAddress {
    
    private DepositRequest request;
    
    private int securityLevel;
    
    /**
     * Used in json de/construction
     */
    @SuppressWarnings("unused")
    private StoredDepositAddress() {
        
    }

    public StoredDepositAddress(DepositRequest request, int securityLevel) {
        this.request = request;
        this.securityLevel = securityLevel;
    }

    public DepositRequest getRequest() {
        return request;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    @Override
    public String toString() {
        return "StoredDepositRequest [request=" + request.toString() + ", securityLevel=" + securityLevel + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(StoredDepositAddress.class)) {
            return false;
        }
        StoredDepositAddress sr = (StoredDepositAddress) obj;
        return sr.securityLevel == securityLevel
                && Objects.equals(sr.request, request);
    }
    
    @Override
    public StoredDepositAddress clone() throws CloneNotSupportedException {
        return (StoredDepositAddress) super.clone();
    }
}
