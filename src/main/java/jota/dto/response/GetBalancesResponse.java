package jota.dto.response;

public class GetBalancesResponse extends AbstractResponse {

    private String[] balances;
    private String milestone;
    private Integer milestoneIndex;

    public Integer getMilestoneIndex() {
        return milestoneIndex;
    }

    public String getMilestone() {
        return milestone;
    }

    public String[] getBalances() {
        return balances;
    }
}
