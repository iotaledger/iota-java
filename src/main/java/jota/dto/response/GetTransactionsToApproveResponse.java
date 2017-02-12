package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaGetTransactionsToApproveRequest}
 **/
public class GetTransactionsToApproveResponse extends AbstractResponse {

    private String trunkTransaction;
    private String branchTransaction;

    public String getTrunkTransaction() {
        return trunkTransaction;
    }

    public String getBranchTransaction() {
        return branchTransaction;
    }
}