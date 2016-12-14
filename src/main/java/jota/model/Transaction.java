package jota.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by pinpong on 02.12.16.
 */
public class Transaction {
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

    public Transaction() {

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
}