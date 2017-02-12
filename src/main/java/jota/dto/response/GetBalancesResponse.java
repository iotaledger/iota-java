package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaGetBalancesRequest}
 **/
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
