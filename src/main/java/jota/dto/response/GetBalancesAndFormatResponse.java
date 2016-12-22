package jota.dto.response;

import java.util.List;

import jota.model.Input;

public class GetBalancesAndFormatResponse extends AbstractResponse {

    private List<Input> input;
    private long totalBalance;
    
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

    public static GetBalancesAndFormatResponse create(List<Input> inputs, long totalBalance2) {
        GetBalancesAndFormatResponse res = new GetBalancesAndFormatResponse();
        res.setInput(inputs);
        res.setTotalBalance(totalBalance2);
        return res;
    }
}
