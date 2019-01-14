package org.iota.jota.account.deposits;

import java.io.Serializable;
import java.util.Date;

import org.iota.jota.account.AccountState;

public class DepositRequest  implements Serializable {
    
    Date timeOut;
    
    boolean multiUse;
    
    long expectedAmount;

    public DepositRequest(Date timeOut, boolean multiUse, long expectedAmount) {
        this.timeOut = timeOut;
        this.multiUse = multiUse;
        this.expectedAmount = expectedAmount;
    }

    /**
     * The timeout after this deposit address becomes invalid (creation+timeout)
     * 
     * @return
     */
    public Date getTimeOut() {
        return timeOut;
    }

    /**
     * Whether to expect multiple deposits to this address
     * in the given timeout.
     * If this flag is false, the deposit address is considered
     * in the input selection as soon as one deposit is available
     * (if the expected amount is set and also fulfilled)
     * 
     * @return
     */
    public boolean isMultiUse() {
        return multiUse;
    }

    /**
     * The expected amount which gets deposited.
     * If the timeout is hit, the address is automatically
     * considered in the input selection.
     * 
     * @return
     */
    public long getExpectedAmount() {
        return expectedAmount;
    }

    @Override
    public String toString() {
        return "DepositRequest ["
                + "timeOut=" + timeOut + ", "
                + "multiUse=" + multiUse + ", "
                + "expectedAmount=" + expectedAmount
                + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(DepositRequest.class)) {
            return false;
        }
        DepositRequest dr = (DepositRequest) obj;
        return dr.multiUse == multiUse
                && dr.timeOut.equals(timeOut)
                && dr.expectedAmount == expectedAmount;
    }
    
    @Override
    public DepositRequest clone() throws CloneNotSupportedException {
        return (DepositRequest) super.clone();
    }
}
