package jota.model;

/**
 * This class represents an Neighbor
 * @author pinpong
 **/
public class Neighbor {

    private String address;
    private Integer numberOfAllTransactions;
    private Integer numberOfInvalidTransactions;
    private Integer numberOfNewTransactions;

    /**
     * Initializes a new instance of the Neighbor class.
     */
    public Neighbor(String address, Integer numberOfAllTransactions, Integer numberOfInvalidTransactions, Integer numberOfNewTransactions) {
        this.address = address;
        this.numberOfAllTransactions = numberOfAllTransactions;
        this.numberOfInvalidTransactions = numberOfInvalidTransactions;
        this.numberOfNewTransactions = numberOfNewTransactions;
    }

    /**
     * Get the address.
     *
     * @return address The address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the number of all transactions.
     *
     * @return numberOfAllTransactions The number of all transactions.
     */
    public Integer getNumberOfAllTransactions() {
        return numberOfAllTransactions;
    }

    /**
     * Get the number of invalid transactions.
     * @return numberOfInvalidTransactions The number of invalid transactions.
     */
    public Integer getNumberOfInvalidTransactions() {
        return numberOfInvalidTransactions;
    }

    /**
     * Get the number of nwe transactions.
     * @return numberOfNewTransactions The number of new transactions.
     */
    public Integer getNumberOfNewTransactions() {
        return numberOfNewTransactions;
    }

}
