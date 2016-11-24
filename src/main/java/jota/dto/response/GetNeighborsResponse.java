package jota.dto.response;

public class GetNeighborsResponse extends AbstractResponse {

    public Neighbors[] neighbors;

    public Neighbors[] getNeighbors() {
        return neighbors;
    }

    public static class Neighbors {

        private String address;
        private Integer numberOfAllTransactions;
        private Integer numberOfInvalidTransactions;
        private Integer numberOfNewTransactions;

        public String getAddress() {
            return address;
        }

        public Integer getNumberOfAllTransactions() {
            return numberOfAllTransactions;
        }

        public Integer getNumberOfInvalidTransactions() {
            return numberOfInvalidTransactions;
        }

        public Integer getNumberOfNewTransactions() {
            return numberOfNewTransactions;
        }
    }
}
