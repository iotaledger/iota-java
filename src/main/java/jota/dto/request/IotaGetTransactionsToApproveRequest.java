package jota.dto.request;

import jota.IotaAPICommands;

public class IotaGetTransactionsToApproveRequest extends IotaCommandRequest {

    private String milestone;

    private IotaGetTransactionsToApproveRequest(final String milestone) {
        super(IotaAPICommands.GET_TRANSACTIONS_TO_APPROVE);
        this.milestone = milestone;
    }

    public static IotaGetTransactionsToApproveRequest createIotaGetTransactionsToApproveRequest(String milestone) {
        return new IotaGetTransactionsToApproveRequest(milestone);
    }
}
