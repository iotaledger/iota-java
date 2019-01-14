package org.iota.jota.account;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.store.FlatFileStore;
import org.iota.jota.store.MemoryStore;
import org.iota.jota.store.Store;
import org.junit.Before;
import org.junit.Test;

public class AccountStoreTest {
    
    protected static final String addressId = 
            "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZC";
    
    private Store store;

    @Before
    public void setUp() throws Exception {
        store = new FlatFileStore(this.getClass().getResourceAsStream("/accounts/client-test.store"), System.out);
        store.load();
    }
    
    @Test
    public void testNewNewAccount() throws CloneNotSupportedException {
        MemoryStore mem = new MemoryStore();
        mem.load();
        
        AccountStoreImpl store = new AccountStoreImpl(mem);
        
        assertEquals(new AccountState(), store.LoadAccount(addressId));
    }
    
    @Test
    public void testExistingStore() throws Exception {
        AccountState state = new AccountState();
        state.addDepositRequest(1, new DepositRequest(new Date(0), false, 5));
        
        AccountStore store = new AccountStoreImpl(this.store);
        assertEquals(state, store.LoadAccount(addressId));
        
        this.store.set(addressId, state);
        this.store.save();
    }
}
