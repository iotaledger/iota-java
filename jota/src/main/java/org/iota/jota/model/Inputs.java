package org.iota.jota.model;

import java.util.List;

import com.google.gson.Gson;

/**
 * This class represents an Inputs.
 *
 * @author Adrian
 **/
public class Inputs {

    private List<Input> inputsList;
    private long totalBalance;

    /**
     * Initializes a new instance of the Input class.
     */
    public Inputs(List<Input> inputsList, long totalBalance) {
        this.inputsList = inputsList;
        this.totalBalance = totalBalance;
    }

    /**
     * Returns a Json Object that represents this object.
     *
     * @return Returns a string representation of this object.
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Get the input list.
     *
     * @return The input list.
     */
    public List<Input> getInputsList() {
        return inputsList;
    }

    /**
     * Set the input list.
     *
     * @param inputsList The input list.
     */
    public void setInputsList(List<Input> inputsList) {
        this.inputsList = inputsList;
    }

    /**
     * Get the total balance.
     *
     * @return The total balance.
     */
    public long getTotalBalance() {
        return totalBalance;
    }

    /**
     * Set the total balance.
     *
     * @param totalBalance The total balance.
     */
    public void setTotalBalance(long totalBalance) {
        this.totalBalance = totalBalance;
    }
}
