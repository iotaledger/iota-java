package jota.model;

import jota.pow.ICurl;
import jota.pow.SpongeFactory;
import jota.utils.Converter;
import jota.utils.Signing;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a Bundle, a set of transactions.
 *
 **/
public class Bundle implements Comparable<Bundle> {

    public static String EMPTY_HASH = "999999999999999999999999999999999999999999999999999999999999999999999999999999999";
    private List<Transaction> transactions;
    private int length;


    /**
     * Initializes a new instance of the Bundle class without transactions.
     */
    public Bundle() {
        this(new ArrayList<>(), 0);
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
        ICurl curl;
        int[] normalizedBundleValue;
        int[] hash = new int[243];
        int[] obsoleteTagTrits = new int[81];
        String hashInTrytes;
        boolean valid = true;
        curl = customCurl == null ? SpongeFactory.create(SpongeFactory.Mode.KERL) : customCurl;
        do {
          curl.reset();

          for (int i = 0; i < this.getTransactions().size(); i++) {

            int[] valueTrits = Converter.trits(this.getTransactions().get(i).getValue(), 81);

            int[] timestampTrits = Converter.trits(this.getTransactions().get(i).getTimestamp(), 27);

            this.getTransactions().get(i).setCurrentIndex(i);

            int[] currentIndexTrits = Converter.trits(this.getTransactions().get(i).getCurrentIndex(), 27);

            this.getTransactions().get(i).setLastIndex(this.getTransactions().size() - 1);

            int[] lastIndexTrits = Converter.trits(this.getTransactions().get(i).getLastIndex(), 27);

            int[] t = Converter.trits(this.getTransactions().get(i).getAddress() + Converter.trytes(valueTrits) + this.getTransactions().get(i).getObsoleteTag() + Converter.trytes(timestampTrits) + Converter.trytes(currentIndexTrits) + Converter.trytes(lastIndexTrits));

            curl.absorb(t, 0, t.length);
          }

          curl.squeeze(hash, 0, hash.length);
          hashInTrytes = Converter.trytes(hash);
          normalizedBundleValue = normalizedBundle(hashInTrytes);

          boolean foundValue = false;
            for (int aNormalizedBundleValue : normalizedBundleValue) {
                if (aNormalizedBundleValue == 13) {
                    foundValue = true;
                    obsoleteTagTrits = Converter.trits(this.getTransactions().get(0).getObsoleteTag());
                    Converter.increment(obsoleteTagTrits, 81);
                    this.getTransactions().get(0).setObsoleteTag(Converter.trytes(obsoleteTagTrits));
                }
            }
          valid = !foundValue;

        } while (!valid);

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
        long emptyTimestamp = 999999999L;

        emptySignatureFragment = StringUtils.rightPad(emptySignatureFragment, 2187, '9');

        for (int i = 0; i < this.getTransactions().size(); i++) {

            // Fill empty signatureMessageFragment
            this.getTransactions().get(i).setSignatureFragments((signatureFragments.size() <= i || signatureFragments.get(i).isEmpty()) ? emptySignatureFragment : signatureFragments.get(i));
            // Fill empty trunkTransaction
            this.getTransactions().get(i).setTrunkTransaction(emptyHash);

            // Fill empty branchTransaction
            this.getTransactions().get(i).setBranchTransaction(emptyHash);


            this.getTransactions().get(i).setAttachmentTimestamp(emptyTimestamp);
            this.getTransactions().get(i).setAttachmentTimestampLowerBound(emptyTimestamp);
            this.getTransactions().get(i).setAttachmentTimestampUpperBound(emptyTimestamp);

            // Fill empty nonce
            this.getTransactions().get(i).setNonce(StringUtils.rightPad("", 27, "9"));

        }
    }


    /**
     * Normalized the bundle.
     *
     * @param bundleHash The bundle hash.
     * @return normalizedBundle A normalized bundle hash.
     */
    public int[] normalizedBundle(String bundleHash) {
        return new Signing().normalizedBundle(bundleHash);
    }


    /**
     * Compares the current object with another object of the same type.
     *
     * @param o An object to compare with this object.
     * @return A value that indicates the relative order of the objects being compared. The return value has the following meanings: Value Meaning Less than zero This object is less than the <paramref name="other" /> parameter.Zero This object is equal to <paramref name="other" />. Greater than zero This object is greater than <paramref name="other" />.
     */
    @Override
    public int compareTo(Bundle o) {
        return Long.compare(this.getTransactions().get(0).getAttachmentTimestamp(), o.getTransactions().get(0).getAttachmentTimestamp());
    }
}
