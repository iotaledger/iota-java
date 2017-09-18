package jota.model;

import jota.pow.ICurl;
import jota.pow.SpongeFactory;
import jota.utils.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * This class represents an iota transaction.
 *
 * @author pinpong
 */
public class Transaction {

    private static final transient Logger log = LoggerFactory.getLogger(Transaction.class);

    private transient ICurl customCurl;

    private String hash;
    private String signatureFragments;
    private String address;
    private long value;
    private String tag;
    private long timestamp;
    private long currentIndex;
    private long lastIndex;
    private String bundle;
    private String trunkTransaction;
    private String branchTransaction;
    private String nonce;
    private Boolean persistence;

    /**
     * Initializes a new instance of the Signature class.
     */
    public Transaction(ICurl curl) {
        customCurl = curl;
    }

    /**
     * Initializes a new instance of the Signature class.
     */
    public Transaction() {
        customCurl = null;
    }

    /**
     * Initializes a new instance of the Signature class.
     */
    public Transaction(String trytes) {
        transactionObject(trytes);
    }

    /**
     * Initializes a new instance of the Signature class.
     */
    public Transaction(String trytes, ICurl customCurl) {
        transactionObject(trytes);
        this.customCurl = customCurl;
    }

    /**
     * Initializes a new instance of the Signature class.
     */
    public Transaction(String signatureFragments, long currentIndex, long lastIndex, String nonce, String hash, String tag, long timestamp, String trunkTransaction, String branchTransaction, String address, long value, String bundle) {

        this.hash = hash;
        this.tag = tag;
        this.signatureFragments = signatureFragments;
        this.address = address;
        this.value = value;
        this.timestamp = timestamp;
        this.currentIndex = currentIndex;
        this.lastIndex = lastIndex;
        this.bundle = bundle;
        this.trunkTransaction = trunkTransaction;
        this.branchTransaction = branchTransaction;
        this.nonce = nonce;
    }

    /**
     * Initializes a new instance of the Signature class.
     */
    public Transaction(String address, long value, String tag, long timestamp) {
        this.address = address;
        this.value = value;
        this.tag = tag;
        this.timestamp = timestamp;
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

    public boolean equals(Object obj) {
        return obj != null && ((Transaction) obj).getHash().equals(this.getHash());
    }


    /**
     * Converts the transaction to the corresponding trytes representation
     */
    public String toTrytes() {
        int[] valueTrits = Converter.trits(this.getValue(), 81);

        int[] timestampTrits = Converter.trits(this.getTimestamp(), 27);


        int[] currentIndexTrits = Converter.trits(this.getCurrentIndex(), 27);


        int[] lastIndexTrits = Converter.trits(this.getLastIndex(), 27);


        return this.getSignatureFragments()
                + this.getAddress()
                + Converter.trytes(valueTrits)
                + this.getTag()
                + Converter.trytes(timestampTrits)
                + Converter.trytes(currentIndexTrits)
                + Converter.trytes(lastIndexTrits)
                + this.getBundle()
                + this.getTrunkTransaction()
                + this.getBranchTransaction()
                + this.getNonce();
    }

    /**
     * Initializes a new instance of the Signature class.
     */
    public void transactionObject(final String trytes) {

        if (StringUtils.isEmpty(trytes)) {
            log.warn("Warning: empty trytes in input for transactionObject");
            return;
        }

        // validity check
        for (int i = 2279; i < 2295; i++) {
            if (trytes.charAt(i) != '9') {
                log.warn("Trytes {} does not seem a valid tryte", trytes);
                return;
            }
        }

        int[] transactionTrits = Converter.trits(trytes);
        int[] hash = new int[243];

        ICurl curl = SpongeFactory.create(SpongeFactory.Mode.CURL);
        // generate the correct transaction hash
        curl.reset();
        curl.absorb(transactionTrits, 0, transactionTrits.length);
        curl.squeeze(hash, 0, hash.length);

        this.setHash(Converter.trytes(hash));
        this.setSignatureFragments(trytes.substring(0, 2187));
        this.setAddress(trytes.substring(2187, 2268));
        this.setValue(Converter.longValue(Arrays.copyOfRange(transactionTrits, 6804, 6837)));
        this.setTag(trytes.substring(2295, 2322));
        this.setTimestamp(Converter.longValue(Arrays.copyOfRange(transactionTrits, 6966, 6993)));
        this.setCurrentIndex(Converter.longValue(Arrays.copyOfRange(transactionTrits, 6993, 7020)));
        this.setLastIndex(Converter.longValue(Arrays.copyOfRange(transactionTrits, 7020, 7047)));
        this.setBundle(trytes.substring(2349, 2430));
        this.setTrunkTransaction(trytes.substring(2430, 2511));
        this.setBranchTransaction(trytes.substring(2511, 2592));
        this.setNonce(trytes.substring(2592, 2673));
    }
}