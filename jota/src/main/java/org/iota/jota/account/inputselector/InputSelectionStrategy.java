package org.iota.jota.account.inputselector;

import org.iota.jota.model.Input;

import java.util.List;

public interface InputSelectionStrategy {
    
    /**
     *
     * @param requiredValue
     * @param balanceCheck
     * @return
     */
    List<Input> getInput(long requiredValue, boolean balanceCheck );

    long getAvailableBalance();
}
