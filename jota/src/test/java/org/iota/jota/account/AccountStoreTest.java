package org.iota.jota.account;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.store.FlatFileStore;
import org.iota.jota.store.IotaFileStore;
import org.iota.jota.store.MemoryStore;
import org.iota.jota.store.Store;
import org.junit.Before;
import org.junit.Test;

public class AccountStoreTest {
    
    protected static final String addressId = 
            "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZC";

    @Before
    public void setUp() throws Exception {
        
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
        Store store = new FlatFileStore(this.getClass().getResourceAsStream("/accounts/client-test.store"), System.out);
        store.load();
        
        AccountState state = new AccountState();
        state.addDepositRequest(1, new DepositRequest(new Date(0), false, 5));
        state.setKeyIndex(4);
        
        AccountStore as = new AccountStoreImpl(store);
        AccountState loadedState = as.LoadAccount(addressId);
        

        assertEquals(state, loadedState);
    }
}
