package jota.model;

import jota.utils.Converter;
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

    public int[] normalizedBundle(String bundleHash) {
        int[] normalizedBundle = new int[33 * 27 + 27];

        for (int i = 0; i < 3; i++) {

            long sum = 0;
            for (int j = 0; j < 27; j++) {

                sum += (normalizedBundle[i * 27 + j] = Converter.value(Converter.trits("" + bundleHash.charAt(i * 27 + j))));
            }

            if (sum >= 0) {
                while (sum-- > 0) {
                    for (int j = 0; j < 27; j++) {
                        if (normalizedBundle[i * 27 + j] > -13) {
                            normalizedBundle[i * 27 + j]--;
                            break;
                        }
                    }
                }
            } else {

                while (sum++ < 0) {

                    for (int j = 0; j < 27; j++) {

                        if (normalizedBundle[i * 27 + j] < 13) {

                            normalizedBundle[i * 27 + j]++;
                            break;
                        }
                    }
                }
            }
        }

        return normalizedBundle;
    }

}

