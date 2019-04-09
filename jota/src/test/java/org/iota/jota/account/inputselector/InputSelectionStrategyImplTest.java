package org.iota.jota.account.inputselector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.iota.jota.account.AccountBalanceCache;
import org.iota.jota.account.clock.SystemClock;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.model.Input;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class InputSelectionStrategyImplTest {

    @Rule 
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    
    @Mock
    AccountBalanceCache cache;
    
    InputSelectionStrategyImpl impl;
    
    @Before
    public void setUp() throws Exception {
        impl = new InputSelectionStrategyImpl(cache, new SystemClock());
        
    }
    
    @Test
    public void test() {
        Input i = new Input("", 1, 0, 1);
        DepositRequest request = new DepositRequest(new Date(Long.MAX_VALUE), false, 1);
        
        assertTrue("fullfilled request should be usable", impl.isUsable(i, request));
        
        i = new Input("", 0, 0, 1);
        request = new DepositRequest(new Date(0), false, 1);
        
        assertTrue("unfullfilled request which timed out should be usable", impl.isUsable(i, request));
        
        i = new Input("", 1, 0, 1);
        request = new DepositRequest(new Date(Long.MAX_VALUE), true, 2);
        
        assertFalse("unfullfilled should not be usable", impl.isUsable(i, request));
    }
}
