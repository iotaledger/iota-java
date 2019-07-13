package org.iota.jota.dto.request;

import org.iota.jota.IotaAPICommand;

/**
 * This class represents the core API request 'getTransactionsToApprove'.
 *
 * It stores transactions into the local storage. The trytes to be used for this call are returned by attachToTangle.
 **/
public class IotaStoreTransactionsRequest extends IotaCommandRequest {

    private String[] trytes;

    /**
     * Initializes a new instance of the IotaStoreTransactionsRequest class
     * .
     * @param trytes
     */
    private IotaStoreTransactionsRequest(final String... trytes) {
        super(IotaAPICommand.STORE_TRANSACTIONS);
        this.trytes = trytes;
    }

    /**
     * Initializes a new instance of the IotaStoreTransactionsRequest class.
     * 
     * @param trytes
     * @return the instance
     */
    public static IotaStoreTransactionsRequest createStoreTransactionsRequest(final String... trytes) {
        return new IotaStoreTransactionsRequest(trytes);
    }

    /**
     * Gets the trytes.
     *
     * @return The trytes.
     */
    public String[] getTrytes() {
        return trytes;
    }

    /**
     * Sets the trytes.
     *
     * @param trytes The trytes.
     */
    public void setTrytes(String[] trytes) {
        this.trytes = trytes;
    }
}
