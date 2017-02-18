package jota.model;

import jota.pow.ICurl;
import jota.pow.JCurl;
import jota.utils.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a Bundle, a set of transactions.
 *
 * @author pinpong
 **/
public class Bundle implements Comparable<Bundle> {

    public static String EMPTY_HASH = "999999999999999999999999999999999999999999999999999999999999999999999999999999999";
    private List<Transaction> transactions;
    private int length;


    /**
     * Initializes a new instance of the Bundle class without transactions.
     */
    public Bundle() {
        this(new ArrayList<Transaction>(), 0);
    }

    /**
     * Initializes a new instance of the Bundle class.
     */
    public Bundle(List<Transaction> transactions, int length) {
        this.transactions = transactions;
        this.length = length;
    }

    /**
     * Gets the transactions
     *
     * @return transactions The transactions.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Gets the length of the bundle
     *
     * @return length The length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the length of the bundle
     *
     * @param length The length.
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Adds a bundle entry.
     *
     * @param signatureMessageLength Length of the signature message.
     * @param address                The address.
     * @param value                  The value.
     * @param tag                    The tag.
     * @param timestamp              The timestamp.
     */
    public void addEntry(int signatureMessageLength, String address, long value, String tag, long timestamp) {
        if (getTransactions() == null) {
            this.transactions = new ArrayList<>(getTransactions());
        }

        for (int i = 0; i < signatureMessageLength; i++) {
            Transaction trx = new Transaction(address, i == 0 ? value : 0, tag, timestamp);
            transactions.add(trx);
        }
    }

    /**
     * Finalizes the bundle using the specified curl implementation,
     *
     * @param customCurl The custom curl.
     */
    public void finalize(ICurl customCurl) {

        ICurl curl = customCurl == null ? new JCurl() : customCurl;
        curl.reset();

        for (int i = 0; i < this.getTransactions().size(); i++) {

            int[] valueTrits = Converter.trits(this.getTransactions().get(i).getValue(), 81);

            int[] timestampTrits = Converter.trits(this.getTransactions().get(i).getTimestamp(), 27);

            this.getTransactions().get(i).setCurrentIndex(i);

            int[] currentIndexTrits = Converter.trits(this.getTransactions().get(i).getCurrentIndex(), 27);

            this.getTransactions().get(i).setLastIndex(this.getTransactions().size() - 1);

            int[] lastIndexTrits = Converter.trits(this.getTransactions().get(i).getLastIndex(), 27);

            int[] t = Converter.trits(this.getTransactions().get(i).getAddress() + Converter.trytes(valueTrits) + this.getTransactions().get(i).getTag() + Converter.trytes(timestampTrits) + Converter.trytes(currentIndexTrits) + Converter.trytes(lastIndexTrits));
            curl.absorb(t, 0, t.length);
        }

        int[] hash = new int[243];
        curl.squeeze(hash, 0, hash.length);
        String hashInTrytes = Converter.trytes(hash);

        for (int i = 0; i < this.getTransactions().size(); i++) {
            this.getTransactions().get(i).setBundle(hashInTrytes);
        }
    }

    /**
     * Adds the trytes.
     *
     * @param signatureFragments The signature fragments.
     */
    public void addTrytes(List<String> signatureFragments) {
        String emptySignatureFragment = "";
        String emptyHash = EMPTY_HASH;

        emptySignatureFragment = StringUtils.rightPad(emptySignatureFragment, 2187, '9');

        for (int i = 0; i < this.getTransactions().size(); i++) {

            // Fill empty signatureMessageFragment
            this.getTransactions().get(i).setSignatureFragments((signatureFragments.size() <= i || signatureFragments.get(i).isEmpty()) ? emptySignatureFragment : signatureFragments.get(i));
            // Fill empty trunkTransaction
            this.getTransactions().get(i).setTrunkTransaction(emptyHash);

            // Fill empty branchTransaction
            this.getTransactions().get(i).setBranchTransaction(emptyHash);

            // Fill empty nonce
            this.getTransactions().get(i).setNonce(emptyHash);
        }
    }


    /**
     * Normalized the bundle.
     *
     * @param bundleHash The bundle hash.
     * @return normalizedBundle A normalized bundle hash.
     */
    public int[] normalizedBundle(String bundleHash) {
        int[] normalizedBundle = new int[81];

        for (int i = 0; i < 3; i++) {

            long sum = 0;
            for (int j = 0; j < 27; j++) {

                sum += (normalizedBundle[i * 27 + j] = Converter.value(Converter.tritsString("" + bundleHash.charAt(i * 27 + j))));
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


    /**
     * Compares the current object with another object of the same type.
     *
     * @param o An object to compare with this object.
     * @return A value that indicates the relative order of the objects being compared. The return value has the following meanings: Value Meaning Less than zero This object is less than the <paramref name="other" /> parameter.Zero This object is equal to <paramref name="other" />. Greater than zero This object is greater than <paramref name="other" />.
     */
    @Override
    public int compareTo(Bundle o) {
        return Long.compare(this.getTransactions().get(0).getTimestamp(), o.getTransactions().get(0).getTimestamp());
    }
}