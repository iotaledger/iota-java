package jota.dto.request;

import jota.IotaAPICommands;

public class IotaGetTransactionsToApproveRequest extends IotaCommandRequest {

    private Integer depth;

    private IotaGetTransactionsToApproveRequest(final Integer depth) {
        super(IotaAPICommands.GET_TRANSACTIONS_TO_APPROVE);
        this.depth = depth;
    }

    public static IotaGetTransactionsToApproveRequest createIotaGetTransactionsToApproveRequest(Integer depth) {
        return new IotaGetTransactionsToApproveRequest(depth);
    }
}
