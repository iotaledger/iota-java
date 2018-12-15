package org.iota.jota.dto.response;

import java.util.List;

import org.iota.jota.model.Input;

/**
 * Response of api request 'getBalancesAndFormatResponse'.
 **/
public class GetBalancesAndFormatResponse extends AbstractResponse {

    private List<Input> inputs;
    private long totalBalance;

    /**
     * Initializes a new instance of the GetBalancesAndFormatResponse class.
     */
    public static GetBalancesAndFormatResponse create(List<Input> inputs, long totalBalance, long duration) {
        GetBalancesAndFormatResponse res = new GetBalancesAndFormatResponse();
        res.inputs = inputs;
        res.totalBalance = totalBalance;
        res.setDuration(duration);
        return res;
    }

    /**
     * Gets the input.
     *
     * @return The transactions.
     */
    public List<Input> getInputs() {
        return inputs;
    }

    /**
     * Sets the input.
     *
     * @param input The input.
     */
    public void setInputs(List<Input> input) {
        this.inputs = input;
    }

    /**
     * Gets the total balance.
     *
     * @return The total balance.
     */
    public long getTotalBalance() {
        return totalBalance;
    }

    /**
     * Sets the total balance.
     *
     * @param totalBalance The total balance.
     */
    public void setTotalBalance(long totalBalance) {
        this.totalBalance = totalBalance;
    }
}
