package org.iota.jota.model;

/**
 * This class represents an Neighbor.
 *
 **/
public class Neighbor {

    private String address;
    private Integer numberOfAllTransactions;
    private Integer numberOfInvalidTransactions;
    private Integer numberOfNewTransactions;
    private Integer numberOfRandomTransactionRequests;
    private Integer numberOfSentTransactions;
    private String connectionType;

    /**
     * Initializes a new instance of the Neighbor class.
     * 
     * @param address
     * @param numberOfAllTransactions
     * @param numberOfInvalidTransactions
     * @param numberOfNewTransactions
     * @param numberOfRandomTransactionRequests
     * @param numberOfSentTransactions
     * @param connectionType
     */
    public Neighbor(String address, Integer numberOfAllTransactions, Integer numberOfInvalidTransactions, Integer numberOfNewTransactions, Integer numberOfRandomTransactionRequests, Integer numberOfSentTransactions, String connectionType) {
        this.address = address;
        this.numberOfAllTransactions = numberOfAllTransactions;
        this.numberOfInvalidTransactions = numberOfInvalidTransactions;
        this.numberOfNewTransactions = numberOfNewTransactions;
        this.numberOfRandomTransactionRequests = numberOfRandomTransactionRequests;
        this.numberOfSentTransactions = numberOfSentTransactions;
        this.connectionType = connectionType;
    }

    /**
     * Get the address.
     *
     * @return The address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the number of all transactions.
     *
     * @return The number of all transactions.
     */
    public Integer getNumberOfAllTransactions() {
        return numberOfAllTransactions;
    }

    /**
     * Get the number of invalid transactions.
     *
     * @return The number of invalid transactions.
     */
    public Integer getNumberOfInvalidTransactions() {
        return numberOfInvalidTransactions;
    }

    /**
     * Get the number of new transactions.
     *
     * @return The number of new transactions.
     */
    public Integer getNumberOfNewTransactions() {
        return numberOfNewTransactions;
    }

    /**
     * Get the number of random transaction requests.
     *
     * @return The number of random transaction requests.
     */
    public Integer getNumberOfRandomTransactionRequests() {
        return numberOfRandomTransactionRequests;
    }

    /**
     * Get the number of sent transactions.
     *
     * @return The number of sent transactions.
     */
    public Integer getNumberOfSentTransactions() {
        return numberOfSentTransactions;
    }

    /**
     * Get the connection type.
     *
     * @return The connection type.
     */
    public String getConnectionType() {
        return connectionType;
    }
}
