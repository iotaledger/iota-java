package org.iota.jota.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.iota.jota.model.Transaction;

/**
 * Response of api request 'sendTransfer'.
 **/
public class SendTransferResponse extends AbstractResponse {

    private List<Transaction> transactions = new ArrayList<>();
    private Boolean[] successfully;

    /**
     * Initializes a new instance of the SendTransferResponse class.
     */
    public static SendTransferResponse create(List<Transaction> transactions, Boolean[] successfully, long duration) {
        SendTransferResponse res = new SendTransferResponse();
        res.transactions = transactions;
        res.successfully = successfully;
        res.setDuration(duration);
        return res;
    }

    /**
     * Gets the transactions.
     *
     * @return The transactions.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Sets the transactions.
     *
     * @param transactions The transactions.
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Gets the successfully.
     *
     * @return The successfully.
     */
    public Boolean[] getSuccessfully() {
        return successfully;
    }

    /**
     * Sets the successfully.
     *
     * @param successfully The successfully.
     */
    public void setSuccessfully(Boolean[] successfully) {
        this.successfully = successfully;
    }
}
