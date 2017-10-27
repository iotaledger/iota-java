package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaGetBalancesRequest}.
 **/
public class GetBalancesResponse extends AbstractResponse {

    private String[] balances;
    private String milestone;
    private int milestoneIndex;

    /**
     * Gets the milestone index.
     *
     * @return The milestone index.
     */
    public int getMilestoneIndex() {
        return milestoneIndex;
    }

    /**
     * Gets the milestone.
     *
     * @return The milestone.
     */
    public String getMilestone() {
        return milestone;
    }

    /**
     * Gets the balances.
     *
     * @return The balances.
     */
    public String[] getBalances() {
        return balances;
    }
}
