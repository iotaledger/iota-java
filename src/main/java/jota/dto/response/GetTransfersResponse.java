package jota.dto.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GetTransfersResponse extends AbstractResponse {

    private Transfers[] transfers;

    public Transfers[] getTransfers() {
        return transfers;
    }

    public static class Transfers {
        private String timestamp;
        private String address;
        private String hash;
        private String persistence;
        private String value;

        public String getAddress() {
            return address;
        }

        public String getHash() {
            return hash;
        }

        public String getPersistence() {
            return persistence;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }
}
