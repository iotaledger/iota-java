package jota.dto.response;

import jota.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class GetBundleResponse extends AbstractResponse {

    private List<Transaction> transactions = new ArrayList<>();

    public static GetBundleResponse create (List<Transaction> transactions){
        GetBundleResponse res = new GetBundleResponse();
        res.transactions = transactions;
        return res;
    }


    public List<Transaction> getTransactions() {
        return transactions;
    }
}