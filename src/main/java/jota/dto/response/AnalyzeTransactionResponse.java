package jota.dto.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AnalyzeTransactionResponse extends AbstractResponse {

    private Transactions[] transactions;

    public Transactions[] getTransactions() {
        return transactions;
    }

    static class Transactions {
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

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
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
}
