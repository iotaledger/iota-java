package org.iota.jota;

import org.apache.commons.io.FileUtils;
import org.iota.jota.account.store.AccountFileStore;
import org.iota.jota.account.store.AccountStoreImpl;
import org.iota.jota.dto.response.GetNodeInfoResponse;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IotaAccountTest {

    private static final String TEST_SEED = "IJEEPFTJEFGFRDTSQGLGEAUZPUJFP9LDMDOOYUNOZFJ9JMJFALJATJGHEUPHHFVTFDYSGZNKMRK9EQKWG";
    private static final String TEST_SEED_ID = "J9SPZIPMIHEGZEBNDLMBTVVTCGQREQXZFXUYTJTYVQCR9TUZWZDBSJBOZLTTLJYXCGGVAIEQFPWLNUGHDf";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private IotaAPI MOCK_API;

    private File file;

    private AccountStoreImpl store;

    @BeforeEach
    public void setUp() throws Exception {
        file = File.createTempFile("client", "account");
        store = new AccountFileStore(file);

        when(MOCK_API.getNodeInfo()).thenReturn(mock(GetNodeInfoResponse.class));
    }

    @AfterEach
    public void tearDown() throws IOException {
        store.shutdown();
        FileUtils.forceDelete(file);
    }

    @Test
    void load() {
        IotaAccount account = new IotaAccount.Builder(TEST_SEED).store(store).api(MOCK_API).build();

        assertTrue("Account should be loaded after build", account.loaded);
        assertEquals("Account ID should be set to the seed id ", TEST_SEED_ID, account.getId());
        assertTrue("Should be a new account", account.isNew());
    }

    @Test
    void usableBalance() {

    }

    @Test
    void totalBalance() {

    }

    @Test
    void updateSettings() {

    }

    @Test
    void send() {
    }

    @Test
    void send1() {
    }

    @Test
    void newDepositAddress() {
    }

    @Test
    void newDepositRequest() {
    }

    @Test
    void send2() {
    }

    @Test
    void sendZeroValue() {
    }

    @Test
    void sendZeroValue1() {
    }

    @Test
    void sendMulti() {
    }

    @Test
    void exportAccount() {
    }

    @Test
    void importAccount() {
    }

    @Test
    void getSeed() {
    }

    @Test
    void getApi() {
    }

    @Test
    void getEventManager() {
    }

    @Test
    void getAccountManager() {
    }
}
