package org.iota.jota.account;

import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

public class AccountStateTest {

    @Before
    public void setUp() throws Exception {
        
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        AccountState state = new AccountState();
        AccountState clone = state.clone();
        
        assertNotSame(clone, state);
    }

    
}
