package org.iota.jota.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.Signing;
import org.iota.jota.utils.TrytesConverter;


/**
 * This class represents a Bundle, a set of transactions.
 *
 **/
public class Bundle implements Comparable<Bundle> {

    /**
     * Use {@link Constants#NULL_HASH } instead, will be removed in a future release.
     */
    @Deprecated
    public static final String EMPTY_HASH = Constants.NULL_HASH;
    
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
     * 
     * @param transactions
     */
    public Bundle(List<Transaction> transactions) {
        this(transactions, transactions.size());
    }
    
    /**
     * Initializes a new instance of the Bundle class.
     * 
     * @param transactions
     * @param length
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
    
    public String getBundleHash() {
        if (getLength() == 0) {
            return Constants.NULL_HASH;
        }
        
        return transactions.get(0).getBundle();
        
    }
    
    public String getMessage() {
        StringBuilder str = new StringBuilder();
        
        for (Transaction t : getTransactions()) {
            if (t.getValue() == 0) {
                str.append(t.getSignatureFragments());
            }
        }
        if (str.length() % 2 != 0) {
            str.deleteCharAt(str.length() - 1);
        }
        return TrytesConverter.trytesToAscii(str.toString());
    }
    
    public void addTransaction(Transaction transaction) {
        if (getTransactions() == null) {
            transactions = new ArrayList<>(getTransactions());
        }
        
        transactions.add(transaction);
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
            
            int[] t = Converter.trits(
                    this.getTransactions().get(i).getAddress().substring(0, 81) + 
                    Converter.trytes(valueTrits) + 
                    this.getTransactions().get(i).getObsoleteTag() + 
                    Converter.trytes(timestampTrits) + 
                    Converter.trytes(currentIndexTrits) + 
                    Converter.trytes(lastIndexTrits));
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
        String emptyHash = Constants.NULL_HASH;
        long emptyTimestamp = 999999999L;

        emptySignatureFragment = StringUtils.rightPad(emptySignatureFragment, 2187, '9');

        for (int i = 0; i < this.getTransactions().size(); i++) {
            Transaction t = this.getTransactions().get(i);
            
            // Fill empty signatureMessageFragment
            t.setSignatureFragments((signatureFragments.size() <= i || signatureFragments.get(i).isEmpty()) ? emptySignatureFragment : signatureFragments.get(i));
            // Fill empty trunkTransaction
            t.setTrunkTransaction(emptyHash);

            // Fill empty branchTransaction
            t.setBranchTransaction(emptyHash);


            t.setAttachmentTimestamp(emptyTimestamp);
            t.setAttachmentTimestampLowerBound(emptyTimestamp);
            t.setAttachmentTimestampUpperBound(emptyTimestamp);

            // Fill empty nonce
            t.setNonce(StringUtils.rightPad("", 27, "9"));

        }
    }

    /**
     * Normalized the bundle.
     *
     * @param bundleHash The bundle hash.
     * @return normalizedBundle A normalized bundle hash.
     * @deprecated will be removed in a future release, replaced by {@link #normalizedBundle(String, ICurl)}
     */
    @Deprecated
    public int[] normalizedBundle(String bundleHash) {
        return normalizedBundle(bundleHash, SpongeFactory.create(SpongeFactory.Mode.KERL));
    }

    /**
     * Normalized the bundle.
     *
     * @param bundleHash The bundle hash.
     * @param curl       Hashing function that should be used to normalize.
     * @return normalizedBundle A normalized bundle hash.
     */
    public int[] normalizedBundle(String bundleHash, ICurl curl) {
        return new Signing(curl).normalizedBundle(bundleHash);
    }

    /**
     * Compares the current object with another object of the same type.
     *
     * @param o An object to compare with this object.
     * @return A value that indicates the relative order of the objects being compared. 
     *         The return value has the following meanings: 
     *         Value Meaning Less than zero This object is less than the parameter.
     *         Zero This object is equal to other. 
     *         Greater than zero This object is greater than other.
     */
    @Override
    public int compareTo(Bundle o) {
        return Long.compare(this.getTransactions().get(0).getAttachmentTimestamp(), o.getTransactions().get(0).getAttachmentTimestamp());
    }
    
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
