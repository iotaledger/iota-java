package org.iota.jota.account.deposits;

public class StoredDepositRequest {
    
    DepositRequest request;
    
    int securityLevel;

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
}
