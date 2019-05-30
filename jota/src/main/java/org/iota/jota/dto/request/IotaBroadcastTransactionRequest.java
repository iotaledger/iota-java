package org.iota.jota.dto.request;

import org.iota.jota.IotaAPICommand;

/**
 * This class represents the core API request 'broadcastTransaction'.
 *
 * Broadcast a list of transactions to all neighbors. The input trytes for this call are provided by attachToTangle
 **/
public class IotaBroadcastTransactionRequest extends IotaCommandRequest {

    private String[] trytes;

    /**
     * Initializes a new instance of the IotaBroadcastTransactionRequest class.
     * 
     * @param trytes
     */
    private IotaBroadcastTransactionRequest(final String... trytes) {
        super(IotaAPICommand.BROADCAST_TRANSACTIONS);
        this.trytes = trytes;
    }

    /**
     * Initializes a new instance of the IotaBroadcastTransactionRequest class.
     * 
     * @param trytes
     * @return the new instance
     */
    public static IotaBroadcastTransactionRequest createBroadcastTransactionsRequest(final String... trytes) {
        return new IotaBroadcastTransactionRequest(trytes);
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