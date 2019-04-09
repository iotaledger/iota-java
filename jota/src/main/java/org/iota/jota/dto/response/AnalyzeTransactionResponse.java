package org.iota.jota.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.iota.jota.model.Transaction;

/**
 * Response of {@link jota.dto.request.IotaGetBalancesRequest}.
 **/
public class AnalyzeTransactionResponse extends AbstractResponse {

    private List<Transaction> transactions = new ArrayList<>();

    /**
     * Gets the transactions.
     *
     * @return transactions trytes The transactions.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
