package org.iota.jota.dto.request;

import org.iota.jota.IotaAPICommand;

import java.util.Collection;

/**
 * This class represents the core API request 'getInclusionStates'.
 **/
public class IotaGetInclusionStateRequest extends IotaCommandRequest {

    private String[] transactions;

    /**
     * Initializes a new instance of the IotaGetInclusionStateRequest class.
     * @param transactions
     * @param tips
     */
    private IotaGetInclusionStateRequest(final String[] transactions) {
        super(IotaAPICommand.GET_INCLUSIONS_STATES);
        this.transactions = transactions;
    }

    /**
     * Initializes a new instance of the IotaGetInclusionStateRequest class.
     * 
     * @param transactions
     * @return the instance      
     */
    public static IotaGetInclusionStateRequest createGetInclusionStateRequest(String[] transactions) {
        return new IotaGetInclusionStateRequest(transactions);
    }

    /**
     * Create a new instance of the IotaGetInclusionStateRequest class.
     * 
     * @param transactions
     * @param tips
     * @return the instance
     */
    public static IotaGetInclusionStateRequest createGetInclusionStateRequest(Collection<String> transactions) {
        return createGetInclusionStateRequest(
                transactions.toArray(new String[]{}));
    }

    /**
     * Gets the transactions.
     *
     * @return The transactions.
     */
    public String[] getTransactions() {
        return transactions;
    }

    /**
     * Sets the transactions.
     *
     * @param transactions The transactions.
     */
    public void setTransactions(String[] transactions) {
        this.transactions = transactions;
    }
}
