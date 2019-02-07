package org.iota.jota.account.deposits;

import java.util.Objects;

public class StoredDepositRequest {
    
    private DepositRequest request;
    
    private int securityLevel;
    
    private StoredDepositRequest() {
        
    }

    public StoredDepositRequest(DepositRequest request, int securityLevel) {
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
        if (!obj.getClass().equals(StoredDepositRequest.class)) {
            return false;
        }
        StoredDepositRequest sr = (StoredDepositRequest) obj;
        return sr.securityLevel == securityLevel
                && Objects.equals(sr.request, request);
    }
    
    @Override
    public StoredDepositRequest clone() throws CloneNotSupportedException {
        return (StoredDepositRequest) super.clone();
    }
}
