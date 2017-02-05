package jota.dto.response;

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