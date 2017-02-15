package jota.dto.response;

import jota.model.Input;

import java.util.List;

/**
 * Response of api request 'getBalancesAndFormatResponse'
 **/
public class GetBalancesAndFormatResponse extends AbstractResponse {

    private List<Input> input;
    private long totalBalance;

    /**
     * Initializes a new instance of the GetBalancesAndFormatResponse class.
     */
    public static GetBalancesAndFormatResponse create(List<Input> inputs, long totalBalance2, long duration) {
        GetBalancesAndFormatResponse res = new GetBalancesAndFormatResponse();
        res.setInput(inputs);
        res.setTotalBalance(totalBalance2);
        res.setDuration(duration);
        return res;
    }

    /**
     * Gets the input.
     *
     * @return The transactions.
     */
    public List<Input> getInput() {
        return input;
    }

    /**
     * Sets the input.
     *
     * @param input The input.
     */
    public void setInput(List<Input> input) {
        this.input = input;
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
