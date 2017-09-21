package jota.dto.response;

import jota.model.Transaction;

import java.util.ArrayList;
import java.util.List;

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
