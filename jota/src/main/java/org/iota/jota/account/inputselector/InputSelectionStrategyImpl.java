package org.iota.jota.account.inputselector;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.iota.jota.account.AccountBalanceCache;
import org.iota.jota.account.clock.Clock;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.errors.AccountError;
import org.iota.jota.account.errors.AccountNoBalanceError;
import org.iota.jota.model.Input;

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
            
            if (remaining <= 0) {
                break;
            }
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
    
    //Package private for testing
    boolean isUsable(Input input, DepositRequest request) {
        if (!request.hasTimeOut()) {
            if (input.getBalance() == 0) {
                throw new AccountError("remainder address in system without 'expected amount'");
            }
            return true;
        }
        
        if (clock.time().after(request.getTimeOut())) {
            // We should recalculate this address first, maybe we missed something.
            // Best to check anyways before we spent
            return true;
        }
        
        if (request.hasExpectedAmount() 
                && input.getBalance() >= request.getExpectedAmount() 
                ) { 
            return true;
        } else {
            // This should timeout
        }
        
        // Any other input is discarded
        return false;
    }
}
