package org.iota.jota.model;

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

import java.util.Arrays;

/**
 * This class represents an iota transaction.
 */
public class Transaction {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

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

    private Transaction(Builder builder) {
        this.customCurl = builder.customCurl;
        this.hash = builder.hash;
        this.signatureFragments = builder.signatureFragments;
        this.address = builder.address;
        this.value = builder.value;
        this.obsoleteTag = builder.obsoleteTag;
        this.timestamp = builder.timestamp;
        this.currentIndex = builder.currentIndex;
        this.lastIndex = builder.lastIndex;
        this.bundle = builder.bundle;
        this.trunkTransaction = builder.trunkTransaction;
        this.branchTransaction = builder.branchTransaction;
        this.nonce = builder.nonce;
        this.persistence = builder.persistence;
        this.attachmentTimestamp = builder.attachmentTimestamp;
        this.tag = builder.tag;
        this.attachmentTimestampLowerBound = builder.attachmentTimestampLowerBound;
        this.attachmentTimestampUpperBound = builder.attachmentTimestampUpperBound;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return ((Transaction) obj).getHash().equals(this.getHash());
    }

    /**
     * Converts the transaction to the corresponding trytes representation
     *
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

        return this.getSignatureFragments()
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
    }

    public static class Builder {
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
         * Start builder with curl mode CURL_P81 as default
         */
        public Builder() {
            this.customCurl = SpongeFactory.create(SpongeFactory.Mode.CURL_P81);
        }

        public Builder curl(ICurl curl) {
            this.customCurl = curl;
            return this;
        }

        public Builder hash(String hash) {
            this.hash = hash;
            return this;
        }

        public Builder signatureFragments(String signatureFragments) {
            this.signatureFragments = signatureFragments;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder value(long value) {
            this.value = value;
            return this;
        }

        public Builder obsoleteTag(String obsoleteTag) {
            this.obsoleteTag = obsoleteTag;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder currentIndex(long currentIndex) {
            this.currentIndex = currentIndex;
            return this;
        }

        public Builder lastIndex(long lastIndex) {
            this.lastIndex = lastIndex;
            return this;
        }

        public Builder bundle(String bundle) {
            this.bundle = bundle;
            return this;
        }

        public Builder trunkTransaction(String trunkTransaction) {
            this.trunkTransaction = trunkTransaction;
            return this;
        }

        public Builder branchTransaction(String branchTransaction) {
            this.branchTransaction = branchTransaction;
            return this;
        }

        public Builder nonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder persistence(boolean persistence) {
            this.persistence = persistence;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder attachmentTimestamp(long attachmentTimestamp) {
            this.attachmentTimestamp = attachmentTimestamp;
            return this;
        }

        public Builder attachmentTimestampLowerBound(long attachmentTimestampLowerBound) {
            this.attachmentTimestampLowerBound = attachmentTimestampLowerBound;
            return this;
        }

        public Builder attachmentTimestampUpperBound(long attachmentTimestampUpperBound) {
            this.attachmentTimestampUpperBound = attachmentTimestampUpperBound;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }

        public Transaction buildWithTrytes(String trytes) {
            if (StringUtils.isEmpty(trytes)) {
                throw new IllegalArgumentException("Trytes must not be empty");
            }

            if (!InputValidator.isNinesTrytes(trytes.substring(2279, 2295), 16)) {
                LOGGER.warn("Trytes {} does not seem a valid tryte", trytes);
                throw new IllegalArgumentException("Trytes does not seem a valid tryte");
            }

            int[] transactionTrits = Converter.trits(trytes);
            int[] hash = new int[Constants.HASH_LENGTH_TRITS];

            // generate the correct transaction hash
            this.customCurl.reset();
            this.customCurl.absorb(transactionTrits, 0, transactionTrits.length);
            this.customCurl.squeeze(hash, 0, hash.length);

            this.hash = Converter.trytes(hash);
            this.signatureFragments = trytes.substring(0, Constants.MESSAGE_LENGTH);
            this.address = trytes.substring(Constants.MESSAGE_LENGTH, Constants.MESSAGE_LENGTH + Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
            this.value = Converter.longValue(Arrays.copyOfRange(transactionTrits, 6804, 6837));
            this.obsoleteTag = trytes.substring(2295, 2295 + Constants.TAG_LENGTH);
            this.timestamp = Converter.longValue(Arrays.copyOfRange(transactionTrits, 6966, 6993));
            this.currentIndex = Converter.longValue(Arrays.copyOfRange(transactionTrits, 6993, 7020));
            this.lastIndex = Converter.longValue(Arrays.copyOfRange(transactionTrits, 7020, 7047));
            this.bundle = trytes.substring(2349, 2349 + Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
            this.trunkTransaction = trytes.substring(2430, 2430 + Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
            this.branchTransaction = trytes.substring(2511, 2511 + Constants.ADDRESS_LENGTH_WITHOUT_CHECKSUM);
            this.tag = trytes.substring(2592, 2592 + Constants.TAG_LENGTH);
            this.attachmentTimestamp = Converter.longValue(Arrays.copyOfRange(transactionTrits, 7857, 7884));
            this.attachmentTimestampLowerBound = Converter.longValue(Arrays.copyOfRange(transactionTrits, 7884, 7911));
            this.attachmentTimestampUpperBound = Converter.longValue(Arrays.copyOfRange(transactionTrits, 7911, 7938));
            this.nonce = trytes.substring(2646, 2673);

            return new Transaction(this);
        }

    }

}
