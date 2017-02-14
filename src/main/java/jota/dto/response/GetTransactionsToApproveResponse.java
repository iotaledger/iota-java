package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaGetTransactionsToApproveRequest}
 **/
public class GetTransactionsToApproveResponse extends AbstractResponse {

    private String trunkTransaction;
    private String branchTransaction;

    /**
     * Gets the trunk transaction.
     *
     * @return trunkTransaction The trunk transaction.
     */
    public String getTrunkTransaction() {
        return trunkTransaction;
    }

    /**
     * Gets the branch transaction.
     *
     * @return branchTransaction The branch transaction.
     */
    public String getBranchTransaction() {
        return branchTransaction;
    }
}