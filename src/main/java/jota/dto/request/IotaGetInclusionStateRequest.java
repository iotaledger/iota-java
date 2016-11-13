package jota.dto.request;

import jota.IotaAPICommands;

import java.util.Collection;

public class IotaGetInclusionStateRequest extends IotaCommandRequest {

    private IotaGetInclusionStateRequest(final String[] transactions, final String[] tips) {
        super(IotaAPICommands.GET_INCLUSIONS_STATES);
        this.transactions = transactions;
        this.tips = tips;
    }

    private String[] transactions;
    private String[] tips;

    public static IotaGetInclusionStateRequest createGetInclusionStateRequest(String[] transactions, String[] tips) {
        return new IotaGetInclusionStateRequest(transactions, tips);
    }

    public static IotaGetInclusionStateRequest createGetInclusionStateRequest(Collection<String> transactions, Collection<String> tips) {
        return createGetInclusionStateRequest(
                transactions.toArray(new String[] {}),
                tips.toArray(new String[] {}));
    }

}
