package org.iota.jota;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.iota.jota.account.deposits.ConditionalDepositAddress;
import org.iota.jota.account.store.AccountFileStore;
import org.iota.jota.account.store.AccountStoreImpl;
import org.iota.jota.dto.response.GetNodeInfoResponse;
import org.iota.jota.store.JsonFlatFileStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IotaAccountTest {

    private static final String TEST_SEED = "IJEEPFTJEFGFRDTSQGLGEAUZPUJFP9LDMDOOYUNOZFJ9JMJFALJATJGHEUPHHFVTFDYSGZNKMRK9EQKWG";
    private static final String TEST_SEED_ID = "J9SPZIPMIHEGZEBNDLMBTVVTCGQREQXZFXUYTJTYVQCR9TUZWZDBSJBOZLTTLJYXCGGVAIEQFPWLNUGHD";
    private static final String ADDR_0_SEC_3 = "TAKWNELREDNHLFYCQ9LMGZVYGTPTABFDEPQZILJAYAZSSCPXMEGCVAH9AHTJRDPVDCGIH9APCWG9KBSGA9VKXRLMU9";
    
    @Mock
    private IotaAPI MOCK_API;

    private File file;

    private AccountStoreImpl store;

    @BeforeEach
    public void setUp() throws Exception {
        file = File.createTempFile("client", "account");

        when(MOCK_API.getNodeInfo()).thenReturn(mock(GetNodeInfoResponse.class));
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (store != null){
            store.shutdown();
        }
        FileUtils.forceDelete(file);
    }

    @Test
    void load() {
        store = new AccountFileStore(file);
        IotaAccount account = new IotaAccount.Builder(TEST_SEED).mwm(9).store(store).api(MOCK_API).build();

        assertTrue(account.loaded, "Account should be loaded after build");
        assertEquals(TEST_SEED_ID, account.getId(), "Account ID should be set to the seed id ");
        assertTrue(account.isNew(), "Should be a new account");
        assertEquals(0, account.availableBalance(), "New accounts should have 0 balance");
        assertEquals(0, account.totalBalance(), "New accounts should have 0 balance");
    }

    @Test
    void totalBalance() throws ExecutionException, InterruptedException {
        // Has a CDA with 5
        JsonFlatFileStore json = new JsonFlatFileStore(this.getClass().getResourceAsStream("/accounts/client-test.store"), new NullOutputStream());
        store = new AccountFileStore(json);

        IotaAccount account = new IotaAccount.Builder(TEST_SEED).mwm(9).store(store).api(MOCK_API).build();

        assertTrue(account.loaded, "Account should be loaded after build");
        assertEquals(TEST_SEED_ID, account.getId(), "Account ID should be set to the seed id");
        assertFalse(account.isNew(), "Should not be a new account");
        assertEquals(0, account.availableBalance(), "Account should have 0 usable balance");
        assertEquals(5, account.totalBalance(), "Account should have 5 total balance");

        Date timeOut = new Date(Long.MAX_VALUE);
        ConditionalDepositAddress cda = account.newDepositAddress(timeOut, false, 10).get();

        assertEquals(0, account.availableBalance(), "Account should have 0 usable balance");
        assertEquals(15, account.totalBalance(), "Account should have 15 total balance");
        assertEquals( "GGAOVJJKOHECPAR9GQBFOISLYUXSRXXXPT9GEYBTRBBMTJAN9CMH9EVVRYDGXUTDMECGXKFWPYAXUO9QD", cda.getDepositAddress().getHash(),
                "Should have generated address at index 5");
    }

    @Test
    void availableBalance() {
        // Force CDA to have received
        mockBalance(ADDR_0_SEC_3, 5l);

        // Has a CDA with 5
        JsonFlatFileStore json = new JsonFlatFileStore(this.getClass().getResourceAsStream("/accounts/client-test.store"), new NullOutputStream());
        store = new AccountFileStore(json);

        IotaAccount account = new IotaAccount.Builder(TEST_SEED).mwm(9).store(store).api(MOCK_API).build();

        assertEquals(5, account.availableBalance(), "Account should have 5 usable balance");
        assertEquals(5, account.totalBalance(), "Account should have 5 total balance");
    }
    
    @Test
    void illegalNewDepositTest() throws InterruptedException {
        store = new AccountFileStore(file);
        IotaAccount account = new IotaAccount.Builder(TEST_SEED).mwm(9).store(store).api(MOCK_API).build();

        Date timeOut = new Date(Long.MAX_VALUE);
        try {
            account.newDepositAddress(timeOut, true, 10).get();
            fail("Account should have thrown an error on a wrong CDA request");
        } catch (ExecutionException e){
            assertEquals("Cannot use multi-use and amount simultaneously", e.getCause().getMessage(),
                    "Account error should have said what is wrong");
        }
    }
    
    private void mockBalance(String addr, long balance) {
        when(MOCK_API.getBalance(100, addr)).thenReturn(balance);
    }
}
