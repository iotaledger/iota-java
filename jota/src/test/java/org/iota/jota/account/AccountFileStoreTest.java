package org.iota.jota.account;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.StoredDepositAddress;
import org.iota.jota.account.store.AccountFileStore;
import org.iota.jota.account.store.AccountStoreImpl;
import org.iota.jota.store.JsonFlatFileStore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountFileStoreTest {
    
    private static final String addressId =
            "J9SPZIPMIHEGZEBNDLMBTVVTCGQREQXZFXUYTJTYVQCR9TUZWZDBSJBOZLTTLJYXCGGVAIEQFPWLNUGHD";
    
    private File file;
    
    private AccountStoreImpl store;
    
    @BeforeEach
    public void setUp() throws Exception {
        file = File.createTempFile("client", "store");
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        store.shutdown();
        FileUtils.forceDelete(file);
    }
    
    @Test
    public void testNewNewAccount() throws Exception {
        store = new AccountFileStore(file);
        store.load();
        assertEquals(new AccountState(), store.loadAccount(addressId));
    }
    
    @Test
    public void testExistingStore() throws Exception {
        JsonFlatFileStore store = new JsonFlatFileStore(this.getClass().getResourceAsStream("/accounts/client-test.store"), System.out);

        AccountState state = new AccountState();
        state.addDepositRequest(1, new StoredDepositAddress(new DepositRequest(new Date(0), false, 5), 3));
        state.setKeyIndex(4);

        this.store = new AccountFileStore(store);
        this.store.load();
        AccountState loadedState = this.store.loadAccount(addressId);

        assertEquals(state, loadedState, "New state and store state should be equal");
    }
}
