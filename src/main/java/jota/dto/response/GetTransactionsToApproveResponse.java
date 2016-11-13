package jota.dto.response;

public class GetTransactionsToApproveResponse extends AbstractResponse {

    private String trunkTransaction;
    private String branchTransactionToApprove;

    public String getBranchTransactionToApprove() {
        return branchTransactionToApprove;
    }
    public String getTrunkTransaction() {
        return trunkTransaction;
    }
}
