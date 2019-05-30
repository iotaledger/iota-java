package org.iota.jota.account;

import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountStateTest {

    @BeforeEach
    public void setUp() throws Exception {
        
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        AccountState state = new AccountState();
        AccountState clone = state.clone();
        
        assertNotSame(clone, state);
    }

    
}
