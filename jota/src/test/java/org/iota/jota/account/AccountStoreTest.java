package org.iota.jota.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.store.MemoryStore;
import org.iota.jota.store.Store;
import org.junit.Before;
import org.junit.Test;

public class AccountStoreTest {
    
    protected static final String addressId = 
            "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZC";
    
    private Store memoryStore;

    @Before
    public void setUp() throws Exception {
        memoryStore = new MemoryStore();
    }

    @Test
    public void testNewStore() throws CloneNotSupportedException {
        AccountStoreImpl store = new AccountStoreImpl(memoryStore);
        assertNotNull(store);
    }
    
    @Test
    public void testNewNewAccount() throws CloneNotSupportedException {
        AccountStoreImpl store = new AccountStoreImpl(memoryStore);
        
        assertEquals(new AccountState(), store.LoadAccount(addressId));
    }
    
    @Test
    public void testExistingStore() throws CloneNotSupportedException {
        AccountState state = new AccountState();
        state.addDepositRequest(1, new DepositRequest(new Date(0), false, 5));
        
        memoryStore.set(addressId, state);
        
        AccountStore store = new AccountStoreImpl(memoryStore);
        assertEquals(state, store.LoadAccount(addressId));
    }
}
