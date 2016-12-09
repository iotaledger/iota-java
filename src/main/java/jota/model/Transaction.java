package jota.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by pinpong on 02.12.16.
 */
public class Transaction {

    private String signatureMessageChunk;
    private String index;
    private String approvalNonce;
    private String hash;
    private String digest;
    private String type;
    private String timestamp;
    private String trunkTransaction;
    private String branchTransaction;
    private String signatureNonce;
    private String address;
    private String value;
    private String bundle;

    public Transaction(String signatureMessageChunk, String index, String approvalNonce, String hash, String digest, String type, String timestamp, String trunkTransaction, String branchTransaction, String signatureNonce, String address, String value, String bundle) {

        this.hash = hash;
        this.type = type;
        this.signatureMessageChunk = signatureMessageChunk;
        this.digest = digest;
        this.address = address;
        this.value = value;
        this.timestamp = timestamp;
        this.index = index;
        this.bundle = bundle;
        this.signatureNonce = signatureNonce;
        this.approvalNonce = approvalNonce;
        this.trunkTransaction = trunkTransaction;
        this.branchTransaction = branchTransaction;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public void setSignatureMessageChunk(String signatureMessageChunk) {

        this.signatureMessageChunk = signatureMessageChunk;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setApprovalNonce(String approvalNonce) {
        this.approvalNonce = approvalNonce;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTrunkTransaction(String trunkTransaction) {
        this.trunkTransaction = trunkTransaction;
    }

    public void setBranchTransaction(String branchTransaction) {
        this.branchTransaction = branchTransaction;
    }

    public void setSignatureNonce(String signatureNonce) {
        this.signatureNonce = signatureNonce;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getValue() {
        return value;
    }

    public String getDigest() {
        return digest;
    }

    public String getTrunkTransaction() {
        return trunkTransaction;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSignatureNonce() {
        return signatureNonce;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getApprovalNonce() {
        return approvalNonce;
    }

    public String getBranchTransaction() {
        return branchTransaction;
    }

    public String getBundle() {
        return bundle;
    }

    public String getHash() {
        return hash;
    }

    public String getIndex() {
        return index;
    }

    public String getSignatureMessageChunk() {
        return signatureMessageChunk;
    }

}
