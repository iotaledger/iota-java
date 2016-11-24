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
        private Integer persistence;
        private long value;

        public String getAddress() {
            return address;
        }

        public String getHash() {
            return hash;
        }

        public Integer getPersistence() {
            return persistence;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public long getValue() {
            return value;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }
}
