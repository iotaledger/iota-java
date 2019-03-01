package org.iota.jota.account.deposits;

import java.io.Serializable;

import org.iota.jota.types.Hash;

public class DepositConditions implements Serializable {

    DepositRequest request;
    
    Hash depositAddress;
    
    private DepositConditions() {
        
    }

    public DepositConditions(DepositRequest request, Hash depositAddress) {
        this.request = request;
        this.depositAddress = depositAddress;
    }

    /**
     * The information about this deposit
     * 
     * @return
     */
    public DepositRequest getRequest() {
        return request;
    }

    /**
     * The address this deposit is requesting to
     * @return
     */
    public Hash getDepositAddress() {
        return depositAddress;
    }

    @Override
    public String toString() {
        return "DepositConditions [request=" + request.toString() + ", depositAddress=" + depositAddress + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(DepositConditions.class)) {
            return false;
        }
        
        DepositConditions dc = (DepositConditions) obj;
        return dc.depositAddress.equals(depositAddress)
                && dc.request.equals(request);
    }
}
