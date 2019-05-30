package org.iota.jota.account.deposits;

import java.io.Serializable;
import java.util.Date;

public class DepositRequest implements Serializable {
    
    private static final long serialVersionUID = -1214895100919711824L;

    Date timeOut;
    
    boolean multiUse;
    
    long expectedAmount;

    /**
     * Used in json de/construction
     */
    @SuppressWarnings("unused")
    private DepositRequest() {
        
    }
    
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
     * 
     * @return
     */
    public boolean hasTimeOut() {
        return timeOut != null;
    }
    
    public boolean getMultiUse() {
        return multiUse;
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
    
    /**
     * 
     * Checks if we are expecting a specific amount in this request
     * 
     * @return <code>true</code> if we expect anything but 0, otherwise <code>false</code>
     */
    public boolean hasExpectedAmount() {
        return getExpectedAmount() != 0;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (expectedAmount ^ (expectedAmount >>> 32));
        result = prime * result + (multiUse ? 1231 : 1237);
        result = prime * result + ((timeOut == null) ? 0 : timeOut.hashCode());
        return result;
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
