package jota.dto.response;

import jota.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeTransactionResponse extends AbstractResponse {

    private List<Transaction> transactions = new ArrayList<Transaction>();

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
