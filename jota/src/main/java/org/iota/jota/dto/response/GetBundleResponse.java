package org.iota.jota.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.iota.jota.model.Transaction;

/**
 * Response of api request 'getBundle.
 **/
public class GetBundleResponse extends AbstractResponse {

    private List<Transaction> transactions = new ArrayList<>();

    /**
     * Initializes a new instance of the GetBundleResponse class.
     */
    public static GetBundleResponse create(List<Transaction> transactions, long duration) {
        GetBundleResponse res = new GetBundleResponse();
        res.transactions = transactions;
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
}