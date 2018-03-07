package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaGetBalancesRequest}.
 **/
public class GetBalancesResponse extends AbstractResponse {

    private String[] balances;
    private String[] references;
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
     * Gets the references.
     *
     * @return The references.
     */
    public String[] getReferences() {
        return references;
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
