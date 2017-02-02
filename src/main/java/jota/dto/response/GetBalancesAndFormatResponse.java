package jota.dto.response;

import jota.model.Input;

import java.util.List;

public class GetBalancesAndFormatResponse extends AbstractResponse {

    private List<Input> input;
    private long totalBalance;

    public static GetBalancesAndFormatResponse create(List<Input> inputs, long totalBalance2, long duration) {
        GetBalancesAndFormatResponse res = new GetBalancesAndFormatResponse();
        res.setInput(inputs);
        res.setTotalBalance(totalBalance2);
        res.setDuration(duration);
        return res;
    }

    public List<Input> getInput() {
        return input;
    }

    public void setInput(List<Input> input) {
        this.input = input;
    }

    public long getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(long totalBalance) {
        this.totalBalance = totalBalance;
    }
}
