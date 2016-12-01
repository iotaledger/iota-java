package jota.dto.response;

public class GetBalancesResponse extends AbstractResponse {

    private String[] balances;
    private String milestone;
    private int milestoneIndex;

    public int getMilestoneIndex() {
        return milestoneIndex;
    }

    public String getMilestone() {
        return milestone;
    }

    public String[] getBalances() {
        return balances;
    }
}
