package org.iota.jota.account.inputselector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.iota.jota.account.AccountBalanceCache;
import org.iota.jota.account.AccountState;
import org.iota.jota.account.AccountStore;
import org.iota.jota.account.clock.Clock;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.errors.AccountNoBalanceError;
import org.iota.jota.model.Input;
import org.iota.jota.types.Address;

public class InputSelectionStrategyImpl implements InputSelectionStrategy {
    
    private Clock clock;
    private AccountBalanceCache cache;
    
    public InputSelectionStrategyImpl(AccountBalanceCache cache, Clock clock) {
        this.cache = cache;
        this.clock = clock;
    }

    @Override
    public List<Input> getInput(long requiredValue, boolean balanceCheck) {
        long remaining = requiredValue;
        
        List<Input> balances = cache.getStream()
                .filter(entry -> isUsable(entry.getKey(), entry.getValue()))
                .map(Entry::getKey) //Get Inputs
                .collect(Collectors.toList()); // remove unwanted inputs
        
        List<Input> usedInputs = new LinkedList<>();
        for (Input i : balances) {
            remaining -= i.getBalance();
            usedInputs.add(i);
            
            if (remaining <= 0) break;
        }
        
        if (remaining > 0) {
            throw new AccountNoBalanceError("Missing " + remaining + " to fullfill input request");
        }
        
        return Collections.unmodifiableList(usedInputs);
    }

    @Override
    public long getUsableBalance() {
        synchronized(cache) {
            long balance = cache.getStream()
                .filter(entry -> isUsable(entry.getKey(), entry.getValue())) // remove unwanted inputs
                .map(Entry::getKey) //Get Inputs
                .mapToLong(Input::getBalance) // Turn input balance into an long stream
                .sum(); // get sum of balances

            return balance;
        }
    }
    
    private boolean isUsable(Input input, DepositRequest request) {
        if (!request.hasTimeOut()) {
            return true;
        } else if (request.getTimeOut().after(clock.now())) {
            // TODO: Check for persist after timeout
            
            if (request.isMultiUse()) {
                if (request.hasExpectedAmount()) {
                    if (input.getBalance() >= request.getExpectedAmount()) {
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                if (request.hasExpectedAmount()) {
                    if (input.getBalance() >= request.getExpectedAmount()) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        } 
        
        // Any other input is discarded
        return false;
    }
}
