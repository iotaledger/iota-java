package jota.model;

import jota.pow.Curl;
import jota.utils.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pinpong on 09.12.16.
 */
public class Bundle {

    private List<Transaction> transactions;
    private int length;

    public static String EMPTY_HASH = "999999999999999999999999999999999999999999999999999999999999999999999999999999999";


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

    public void addEntry(int signatureMessageLength, String address, long value, String tag, long timestamp) {
        for (int i = 0; i < signatureMessageLength; i++) {

            List<Transaction> transactions = new ArrayList<>(getTransactions());
            transactions.add(new Transaction(address, String.valueOf(i == 0 ? value : 0), tag, String.valueOf(timestamp)));

            setTransactions(transactions);

        }
    }

    public void finalize() {

        Curl curl = new Curl();
        curl.reset();

        for (int i = 0; i < this.getTransactions().size(); i++) {

            int[] valueTrits = Converter.trits(this.getTransactions().get(i).getValue());
            while (valueTrits.length < 81) {
                valueTrits[valueTrits.length] = 0;
            }

            int[] timestampTrits = Converter.trits(this.getTransactions().get(i).getTimestamp());
            while (timestampTrits.length < 27) {
                timestampTrits[timestampTrits.length] = 0;
            }

            int[] currentIndexTrits = Converter.trits(this.getTransactions().get(i).setCurrentIndex("" + i));
            while (currentIndexTrits.length < 27) {
                currentIndexTrits[currentIndexTrits.length] = 0;
            }

            int[] lastIndexTrits = Converter.trits(this.getTransactions().get(i).setLastIndex("" + (this.getTransactions().size() - 1)));
            while (lastIndexTrits.length < 27) {
                lastIndexTrits[lastIndexTrits.length] = 0;
            }

            int[] t = Converter.trits(this.getTransactions().get(i).getAddress() + Converter.trytes(valueTrits) + this.getTransactions().get(i).getTag() + Converter.trytes(timestampTrits) + Converter.trytes(currentIndexTrits) + Converter.trytes(lastIndexTrits));
            curl.absorb(t, 0, t.length);
        }

        int[] hash = new int[90];
        curl.squeeze(hash, 0, hash.length);
        String hashInTrytes = Converter.trytes(hash);

        for (int i = 0; i < this.getTransactions().size(); i++) {
            this.getTransactions().get(i).setBundle(hashInTrytes);
        }
    }


    public void addTrytes(List<String> signatureFragments) {
        String emptySignatureFragment = "";
        String emptyHash = EMPTY_HASH;

        for (int j = 0; emptySignatureFragment.length() < 2187; j++) {
            emptySignatureFragment += '9';
        }

        for (int i = 0; i < this.getTransactions().size(); i++) {

            // Fill empty signatureMessageFragment
            this.getTransactions().get(i).setSignatureFragments(signatureFragments.get(i) == null ? signatureFragments.get(i) : emptySignatureFragment);
            // Fill empty trunkTransaction
            this.getTransactions().get(i).setTrunkTransaction(emptyHash);

            // Fill empty branchTransaction
            this.getTransactions().get(i).setBranchTransaction(emptyHash);

            // Fill empty nonce
            this.getTransactions().get(i).setNonce(emptyHash);
        }
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
