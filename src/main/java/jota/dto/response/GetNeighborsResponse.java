package jota.dto.response;

public class GetNeighborsResponse extends AbstractResponse {

    private Neighbors[] neighbors;

    public Neighbors[] getNeighbors() {
        return neighbors;
    }

    static class Neighbors {

        private String address;
        private String numberOfAllTransactions;
        private String numberOfInvalidTransactions;
        private String numberOfNewTransactions;

        public String getAddress() {
            return address;
        }

        public String getNumberOfAllTransactions() {
            return numberOfAllTransactions;
        }

        public String getNumberOfInvalidTransactions() {
            return numberOfInvalidTransactions;
        }

        public String getNumberOfNewTransactions() {
            return numberOfNewTransactions;
        }
    }
}
