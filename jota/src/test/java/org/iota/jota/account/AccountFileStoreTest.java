package org.iota.jota.account;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.StoredDepositRequest;
import org.iota.jota.account.store.AccountFileStore;
import org.iota.jota.account.store.AccountStoreImpl;
import org.iota.jota.store.FlatFileStore;
import org.iota.jota.store.JsonFlatFileStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccountFileStoreTest {
    
    protected static final String addressId = 
            "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZC";
    
    private File file;
    
    private AccountStoreImpl store;
    
    @Before
    public void setUp() throws Exception {
        file = File.createTempFile("client", "store");
    }
    
    @After
    public void tearDown() throws IOException {
        store.shutdown();
        FileUtils.forceDelete(file);
    }
    
    @Test
    public void testNewNewAccount() throws CloneNotSupportedException {
        store = new AccountFileStore(file);
        
        assertEquals(new AccountState(), store.loadAccount(addressId));
    }
    
    @Test
    public void testExistingStore() throws Exception {
        FlatFileStore store = new JsonFlatFileStore(this.getClass().getResourceAsStream("/accounts/client-test.store"), System.out);
        store.load();
        
        AccountState state = new AccountState();
        state.addDepositRequest(1, new StoredDepositRequest(new DepositRequest(new Date(0), false, 5), 1));
        state.setKeyIndex(4);
        
        this.store = new AccountFileStore(store);
        AccountState loadedState = this.store.loadAccount(addressId);
        
        assertEquals(state, loadedState);
    }
}
