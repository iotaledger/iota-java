package jota.dto.request;

import jota.IotaAPICommands;

import java.util.Collection;

/**
 * This class represents the core API request 'getInclusionStates'
 **/
public class IotaGetInclusionStateRequest extends IotaCommandRequest {

    private String[] transactions;
    private String[] tips;

    private IotaGetInclusionStateRequest(final String[] transactions, final String[] tips) {
        super(IotaAPICommands.GET_INCLUSIONS_STATES);
        this.transactions = transactions;
        this.tips = tips;
    }

    public static IotaGetInclusionStateRequest createGetInclusionStateRequest(String[] transactions, String[] tips) {
        return new IotaGetInclusionStateRequest(transactions, tips);
    }

    public static IotaGetInclusionStateRequest createGetInclusionStateRequest(Collection<String> transactions, Collection<String> tips) {
        return createGetInclusionStateRequest(
                transactions.toArray(new String[]{}),
                tips.toArray(new String[]{}));
    }

    public String[] getTransactions() {
        return transactions;
    }

    public void setTransactions(String[] transactions) {
        this.transactions = transactions;
    }

    public String[] getTips() {
        return tips;
    }

    public void setTips(String[] tips) {
        this.tips = tips;
    }
}
