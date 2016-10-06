package jota.dto.response;

public class GetTransfersResponse extends AbstractResponse {

    private Transfers[] transfers;
    
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
    }
    
    public Transfers[] getTransfers() {
        return transfers;
    }
}
