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

    public Neighbor(String address, Integer numberOfAllTransactions, Integer numberOfInvalidTransactions, Integer numberOfNewTransactions) {
        this.address = address;
        this.numberOfAllTransactions = numberOfAllTransactions;
        this.numberOfInvalidTransactions = numberOfInvalidTransactions;
        this.numberOfNewTransactions = numberOfNewTransactions;
    }

    public String getAddress() {
        return address;
    }

    public Integer getNumberOfAllTransactions() {
        return numberOfAllTransactions;
    }

    public Integer getNumberOfInvalidTransactions() {
        return numberOfInvalidTransactions;
    }

    public Integer getNumberOfNewTransactions() {
        return numberOfNewTransactions;
    }

}
