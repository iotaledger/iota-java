package jota.model;

import jota.pow.ICurl;
import jota.pow.JCurl;
import jota.utils.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * This class represents an iota transaction
 * @author pinpong
 */
public class Transaction {
    private static final Logger log = LoggerFactory.getLogger(Transaction.class);
    private ICurl customCurl;

    private String hash;
    private String signatureFragments;
    private String address;
    private String value;
    private String tag;
    private String timestamp;
    private String currentIndex;
    private String lastIndex;
    private String bundle;
    private String trunkTransaction;
    private String branchTransaction;
    private String nonce;
    private Boolean persistence;

    public Transaction(ICurl curl) {
        customCurl = curl;
    }

    public Transaction() {
        customCurl = null;
    }

    public Transaction(String trytes) {
        transactionObject(trytes);
    }

    public Transaction(String trytes, ICurl customCurl) {
        transactionObject(trytes);
        this.customCurl = customCurl;
    }

    public Transaction(String signatureFragments, String currentIndex, String lastIndex, String nonce, String hash, String tag, String timestamp, String trunkTransaction, String branchTransaction, String address, String value, String bundle) {

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


    public Transaction(String address, String value, String tag, String timestamp) {
        this.address = address;
        this.value = value;
        this.tag = tag;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSignatureFragments() {
        return signatureFragments;
    }

    public String setSignatureFragments(String signatureFragments) {
        return this.signatureFragments = signatureFragments;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public String setCurrentIndex(String currentIndex) {
        return this.currentIndex = currentIndex;
    }

    public String getLastIndex() {
        return lastIndex;
    }

    public String setLastIndex(String lastIndex) {
        return this.lastIndex = lastIndex;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getTrunkTransaction() {
        return trunkTransaction;
    }

    public void setTrunkTransaction(String trunkTransaction) {
        this.trunkTransaction = trunkTransaction;
    }

    public String getBranchTransaction() {
        return branchTransaction;
    }

    public void setBranchTransaction(String branchTransaction) {
        this.branchTransaction = branchTransaction;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public Boolean getPersistence() {
        return persistence;
    }

    public void setPersistence(Boolean persistence) {
        this.persistence = persistence;
    }

    public boolean equals(Object obj) {
        return obj != null && ((Transaction) obj).getHash().equals(this.getHash());
    }

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

        final ICurl curl = customCurl == null ? new JCurl() : customCurl; // we need a fluent JCurl.

        // generate the correct transaction hash
        curl.reset();
        curl.absorb(transactionTrits, 0, transactionTrits.length);
        curl.squeeze(hash, 0, hash.length);

        this.setHash(Converter.trytes(hash));
        this.setSignatureFragments(trytes.substring(0, 2187));
        this.setAddress(trytes.substring(2187, 2268));
        this.setValue("" + Converter.longValue(Arrays.copyOfRange(transactionTrits, 6804, 6837)));
        this.setTag(trytes.substring(2295, 2322));
        this.setTimestamp("" + Converter.longValue(Arrays.copyOfRange(transactionTrits, 6966, 6993)));
        this.setCurrentIndex("" + Converter.longValue(Arrays.copyOfRange(transactionTrits, 6993, 7020)));
        this.setLastIndex("" + Converter.longValue(Arrays.copyOfRange(transactionTrits, 7020, 7047)));
        this.setBundle(trytes.substring(2349, 2430));
        this.setTrunkTransaction(trytes.substring(2430, 2511));
        this.setBranchTransaction(trytes.substring(2511, 2592));
        this.setNonce(trytes.substring(2592, 2673));
    }
}