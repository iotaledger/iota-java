package org.iota.jota.model;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an iota transaction.
 *
 */
public class Transaction {

    private static final transient Logger log = LoggerFactory.getLogger(Transaction.class);

    private transient ICurl customCurl;

    private String hash;
    private String signatureFragments;
    private String address;
    private long value;
    private String obsoleteTag;
    private long timestamp;
    private long currentIndex;
    private long lastIndex;
    private String bundle;
    private String trunkTransaction;
    private String branchTransaction;
    private String nonce;
    private Boolean persistence;
    private long attachmentTimestamp;
    private String tag;
    private long attachmentTimestampLowerBound;
    private long attachmentTimestampUpperBound;
    
    /**
     * Converts an array of transaction trytes into an array of transaction objects.
     * @param trytes the array of transactions trytes
     * @return the transaction objects
     */
    public static Transaction[] asTransactionObjects(String... trytes) {
        Transaction[] transactions = new Transaction[trytes.length];
        for (int i = 0; i < trytes.length; i++) {
            transactions[i] = asTransactionObject(trytes[i]);
        }
        return transactions;
    }
    
    /**
     * Converts  transaction trytes into a transaction object.
     * @param trytes the transaction trytes
     * @return the transaction object
     */
    public static Transaction asTransactionObject(String trytes) {
        return new Transaction(trytes);
    }

    /**
     * Initializes a new instance of the Signature class.
     * 
     * @param signatureFragments
     * @param currentIndex
     * @param lastIndex
     * @param nonce
     * @param hash
     * @param obsoleteTag
     * @param timestamp
     * @param trunkTransaction
     * @param branchTransaction
     * @param address
     * @param value
     * @param bundle
     * @param tag
     * @param attachmentTimestamp
     * @param attachmentTimestampLowerBound
     * @param attachmentTimestampUpperBound
     */
    public Transaction(String signatureFragments, long currentIndex, long lastIndex, String nonce, String hash, String obsoleteTag, long timestamp, String trunkTransaction, String branchTransaction, String address, long value, String bundle, String tag, long attachmentTimestamp, long attachmentTimestampLowerBound, long attachmentTimestampUpperBound) {
        this();
        this.hash = hash;
        this.obsoleteTag = obsoleteTag;
        this.signatureFragments = signatureFragments;
        this.address = address;
        this.value = value;
        this.timestamp = timestamp;
        this.currentIndex = currentIndex;
        this.lastIndex = lastIndex;
        this.bundle = bundle;
        this.trunkTransaction = trunkTransaction;
        this.branchTransaction = branchTransaction;
        this.tag = tag;
        this.attachmentTimestamp = attachmentTimestamp;
        this.attachmentTimestampLowerBound = attachmentTimestampLowerBound;
        this.attachmentTimestampUpperBound = attachmentTimestampUpperBound;
        this.nonce = nonce;
    }

    /**
     * Initializes a new instance of the Signature class.
     * 
     * @param address
     * @param value
     * @param tag
     * @param timestamp
     */
    public Transaction(String address, long value, String tag, long timestamp) {
        this();
        this.address = address;
        this.value = value;
        this.tag = tag;
        this.obsoleteTag = tag;
        this.timestamp = timestamp;
    }
    
    /**
     * Initializes a new instance of the Signature class.
     * 
     * @param curl custom curl instance used for creating the hash
     */
    public Transaction(ICurl curl) {
        customCurl = curl;
    }

    /**
     * Initializes a new instance of the Signature class.
     * Default Mode.CURL_P81 is being used
     */
    public Transaction() {
        customCurl = SpongeFactory.create(SpongeFactory.Mode.CURL_P81);
    }

    /**
     * Initializes a new instance of the Signature class.
     * 
     * @param trytes transaction trytes
     */
    public Transaction(String trytes) {
        this();
        transactionObject(trytes);
    }

    /**
     * Initializes a new instance of the Signature class.
     * 
     * @param trytes transaction trytes
     * @param customCurl custom curl instance used for creating the hash
     */
    public Transaction(String trytes, ICurl customCurl) {
       this(customCurl);
        transactionObject(trytes);
    }
    
    public void setCustomCurl(ICurl customCurl) {
        this.customCurl = customCurl;
    }
    
    public long getAttachmentTimestampLowerBound() {
        return attachmentTimestampLowerBound;
    }

    public void setAttachmentTimestampLowerBound(long attachmentTimestampLowerBound) {
        this.attachmentTimestampLowerBound = attachmentTimestampLowerBound;
    }

    public long getAttachmentTimestampUpperBound() {
        return attachmentTimestampUpperBound;
    }

    public void setAttachmentTimestampUpperBound(long attachmentTimestampUpperBound) {
        this.attachmentTimestampUpperBound = attachmentTimestampUpperBound;
    }

    /**
     * Returns a String that represents this object.
     *
     * @return Returns a string representation of this object.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * Get the hash.
     *
     * @return The hash.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Set the hash.
     *
     * @param hash The hash.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Get the signature fragments.
     *
     * @return The signature fragments.
     */
    public String getSignatureFragments() {
        return signatureFragments;
    }

    /**
     * Set the signature fragments.
     *
     * @param signatureFragments The signature fragments.
     */
    public void setSignatureFragments(String signatureFragments) {
        this.signatureFragments = signatureFragments;
    }

    /**
     * Get the address.
     *
     * @return The address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address.
     *
     * @param address The address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the value.
     *
     * @return The value.
     */
    public long getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value The value.
     */
    public void setValue(long value) {
        this.value = value;
    }

    /**
     * Get the tag.
     *
     * @return The tag.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Set the tag.
     *
     * @param tag The tag.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Get the timestamp.
     *
     * @return The timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp.
     *
     * @param timestamp The timestamp.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the current index.
     *
     * @return The current index.
     */
    public long getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Set the current index.
     *
     * @param currentIndex The current index.
     */
    public void setCurrentIndex(long currentIndex) {
        this.currentIndex = currentIndex;
    }

    /**
     * Get the last index.
     *
     * @return The last index.
     */
    public long getLastIndex() {
        return lastIndex;
    }

    /**
     * Set the last index.
     *
     * @param lastIndex The last index.
     */
    public void setLastIndex(long lastIndex) {
        this.lastIndex = lastIndex;
    }

    /**
     * Get the bundle.
     *
     * @return The bundle.
     */
    public String getBundle() {
        return bundle;
    }

    /**
     * Set the bundle.
     *
     * @param bundle The bundle.
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    /**
     * Get the trunk transaction.
     *
     * @return The trunk transaction.
     */
    public String getTrunkTransaction() {
        return trunkTransaction;
    }

    /**
     * Set the trunk transaction.
     *
     * @param trunkTransaction The trunk transaction.
     */
    public void setTrunkTransaction(String trunkTransaction) {
        this.trunkTransaction = trunkTransaction;
    }

    /**
     * Get the branch transaction.
     *
     * @return The branch transaction.
     */
    public String getBranchTransaction() {
        return branchTransaction;
    }

    /**
     * Set the branch transaction.
     *
     * @param branchTransaction The branch transaction.
     */
    public void setBranchTransaction(String branchTransaction) {
        this.branchTransaction = branchTransaction;
    }

    /**
     * Get the nonce.
     *
     * @return The nonce.
     */
    public String getNonce() {
        return nonce;
    }

    /**
     * Set the nonce.
     *
     * @param nonce The trunk nonce.
     */
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    /**
     * Get the persistence.
     *
     * @return The persistence.
     */
    public Boolean getPersistence() {
        return persistence;
    }

    /**
     * Set the persistence.
     *
     * @param persistence The persistence.
     */
    public void setPersistence(Boolean persistence) {
        this.persistence = persistence;
    }

    /**
     * Get the obsoleteTag.
     *
     * @return The obsoleteTag.
     */
    public String getObsoleteTag() {
        return obsoleteTag;
    }

    /**
     * Set the obsoleteTag.
     *
     * @param obsoleteTag The persistence.
     */
    public void setObsoleteTag(String obsoleteTag) {
        this.obsoleteTag = obsoleteTag;
    }

    /**
     * Get the attachmentTimestamp.
     *
     * @return The attachmentTimestamp.
     */
    public long getAttachmentTimestamp() {
        return attachmentTimestamp;
    }

    /**
     * Set the attachmentTimestamp.
     *
     * @param attachmentTimestamp The persistence.
     */
    public void setAttachmentTimestamp(long attachmentTimestamp) {
        this.attachmentTimestamp = attachmentTimestamp;
    }

    public boolean equals(Object obj) {
        return obj != null && ((Transaction) obj).getHash().equals(this.getHash());
    }


    /**
     * Converts the transaction to the corresponding trytes representation
     * @return The transaction trytes
     */
    public String toTrytes() {
        int[] valueTrits = Converter.trits(this.getValue(), 81);

        int[] timestampTrits = Converter.trits(this.getTimestamp(), 27);

        int[] currentIndexTrits = Converter.trits(this.getCurrentIndex(), 27);

        int[] lastIndexTrits = Converter.trits(this.getLastIndex(), 27);

        int[] attachmentTimestampTrits = Converter.trits(this.getAttachmentTimestamp(), 27);

        int[] attachmentTimestampLowerBoundTrits = Converter.trits(this.getAttachmentTimestampLowerBound(), 27);

        int[] attachmentTimestampUpperBoundTrits = Converter.trits(this.getAttachmentTimestampUpperBound(), 27);

        this.tag = this.tag != null && !this.tag.isEmpty() ? this.tag : this.obsoleteTag;

        String trytes = this.getSignatureFragments()
                + this.getAddress().substring(0, 81)
                + Converter.trytes(valueTrits)
                + this.getObsoleteTag()
                + Converter.trytes(timestampTrits)
                + Converter.trytes(currentIndexTrits)
                + Converter.trytes(lastIndexTrits)
                + this.getBundle()
                + this.getTrunkTransaction()
                + this.getBranchTransaction()
                + this.getTag()
                + Converter.trytes(attachmentTimestampTrits)
                + Converter.trytes(attachmentTimestampLowerBoundTrits)
                + Converter.trytes(attachmentTimestampUpperBoundTrits)
                + this.getNonce();
        return trytes;
    }

    /**
     * Initializes a new instance of the Signature class.
     * 
     * @param trytes
     */
    public void transactionObject(final String trytes) {

        if (StringUtils.isEmpty(trytes)) {
            log.warn("Warning: empty trytes in input for transactionObject");
            return;
        }

        // validity check
        if (!InputValidator.isNinesTrytes(trytes.substring(2279, 2295), 16)) {
            log.warn("Trytes {} does not seem a valid tryte", trytes);
            return;
        }

        int[] transactionTrits = Converter.trits(trytes);
        int[] hash = new int[Constants.HASH_LENGTH_TRITS];

        ICurl curl = customCurl != null ? customCurl.clone() : SpongeFactory.create(SpongeFactory.Mode.CURL_P81);
        // generate the correct transaction hash
        curl.reset();
        curl.absorb(transactionTrits, 0, transactionTrits.length);
        curl.squeeze(hash, 0, hash.length);

        this.setHash(Converter.trytes(hash));
        this.setSignatureFragments(trytes.substring(0, Constants.MESSAGE_LENGTH));
        this.setAddress(trytes.substring(Constants.MESSAGE_LENGTH, Constants.MESSAGE_LENGTH + Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM));
        this.setValue(Converter.longValue(Arrays.copyOfRange(transactionTrits, 6804, 6837)));
        this.setObsoleteTag(trytes.substring(2295, 2295 + Constants.TAG_LENGTH));
        this.setTimestamp(Converter.longValue(Arrays.copyOfRange(transactionTrits, 6966, 6993)));
        this.setCurrentIndex(Converter.longValue(Arrays.copyOfRange(transactionTrits, 6993, 7020)));
        this.setLastIndex(Converter.longValue(Arrays.copyOfRange(transactionTrits, 7020, 7047)));
        this.setBundle(trytes.substring(2349, 2349 + Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM));
        this.setTrunkTransaction(trytes.substring(2430, 2430 + Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM));
        this.setBranchTransaction(trytes.substring(2511, 2511 + Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM));
        this.setTag(trytes.substring(2592, 2592 + Constants.TAG_LENGTH));
        this.setAttachmentTimestamp(Converter.longValue(Arrays.copyOfRange(transactionTrits, 7857, 7884)));
        this.setAttachmentTimestampLowerBound(Converter.longValue(Arrays.copyOfRange(transactionTrits, 7884, 7911)));
        this.setAttachmentTimestampUpperBound(Converter.longValue(Arrays.copyOfRange(transactionTrits, 7911, 7938)));
        this.setNonce(trytes.substring(2646, 2673));
    }
    
    /**
     * Checks if the current index is 0
     * @return if this is a tail transaction
     */
    public boolean isTailTransaction() {
        return getCurrentIndex() == 0;
    }
}
