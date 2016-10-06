package jota.dto.response;

public class GetNeighborsResponse extends AbstractResponse {

    private Neighbors[] neighbors;

    public Neighbors[] getNeighbors() {
        return neighbors;
    }

    static class Neighbors {

        private String numberOfAllTransactions;
        private String address;
        private String numberOfNewTransactions;

        public String getAddress() {
            return address;
        }
        public String getNumberOfAllTransactions() {
            return numberOfAllTransactions;
        }
        public String getNumberOfNewTransactions() {
            return numberOfNewTransactions;
        }
    }
}
