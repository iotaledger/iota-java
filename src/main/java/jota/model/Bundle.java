package jota.model;

import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pinpong on 09.12.16.
 */
public class Bundle {

    private List<Transaction> transactions;
    private int length;

    public Bundle() {
        this(new ArrayList<Transaction>(), 0);
    }

    public Bundle(List<Transaction> transactions, int length) {
        this.transactions = transactions;
        this.length = length;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void addEntry(int signatureMessageLength, String slice, long value, String tag, long timestamp) {
        throw new NotImplementedException("");
    }

    public void finalize() {
        throw new NotImplementedException("");
    }

    public void addTrytes(List<String> signatureFragments) {
        throw new NotImplementedException("");
    }

    public String normalizedBundle(String bundleHash) {
        throw new NotImplementedException("");
    }


}
