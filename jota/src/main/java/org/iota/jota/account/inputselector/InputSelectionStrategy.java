package org.iota.jota.account.inputselector;

import org.iota.jota.account.AccountState;
import org.iota.jota.model.Input;

public interface InputSelectionStrategy {
    
    Input[] getInput(AccountState state, long value, boolean balanceCheck );
}
