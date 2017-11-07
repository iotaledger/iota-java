package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core API request 'broadcastTransaction'.
 *
 * Broadcast a list of transactions to all neighbors. The input trytes for this call are provided by attachToTangle
 **/
public class IotaBroadcastTransactionRequest extends IotaCommandRequest {

    private String[] trytes;

    /**
     * Initializes a new instance of the IotaBroadcastTransactionRequest class.
     */
    private IotaBroadcastTransactionRequest(final String... trytes) {
        super(IotaAPICommands.BROADCAST_TRANSACTIONS);
        this.trytes = trytes;
    }

    /**
     * Initializes a new instance of the IotaBroadcastTransactionRequest class.
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