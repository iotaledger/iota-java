package org.iota.jota.account.inputselector;

import java.util.Date;

import org.iota.jota.account.AccountBalanceCache;
import org.iota.jota.account.clock.SystemClock;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.model.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InputSelectionStrategyImplTest {
    
    @Mock
    AccountBalanceCache cache;
    
    InputSelectionStrategyImpl impl;
    
    @BeforeEach
    public void setUp() throws Exception {
        impl = new InputSelectionStrategyImpl(cache, new SystemClock());
        
    }
    
    @Test
    public void test() {
        Input i = new Input("", 1, 0, 1);
        DepositRequest request = new DepositRequest(new Date(Long.MAX_VALUE), false, 1);
        
        assertTrue(impl.isUsable(i, request), "fulfilled request should be usable");
        
        i = new Input("", 0, 0, 1);
        request = new DepositRequest(new Date(0), false, 1);
        
        assertTrue(impl.isUsable(i, request), "unfulfilled request which timed out should be usable");
        
        i = new Input("", 1, 0, 1);
        request = new DepositRequest(new Date(Long.MAX_VALUE), true, 2);
        
        assertFalse(impl.isUsable(i, request), "unfulfilled should not be usable");
    }
}
