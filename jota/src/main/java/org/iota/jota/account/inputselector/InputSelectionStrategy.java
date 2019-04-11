package org.iota.jota.account.inputselector;

import java.util.List;

import org.iota.jota.model.Input;

public interface InputSelectionStrategy {
    
    /**
     * 
     * @param state
     * @param requiredValue
     * @param balanceCheck
     * @return
     */
    List<Input> getInput(long requiredValue, boolean balanceCheck );

    long getUsableBalance();
}
