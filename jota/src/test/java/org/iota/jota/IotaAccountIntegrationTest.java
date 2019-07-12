package org.iota.jota;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.iota.jota.account.deposits.ConditionalDepositAddress;
import org.iota.jota.account.errors.AccountError;
import org.iota.jota.account.errors.SendException;
import org.iota.jota.account.store.AccountFileStore;
import org.iota.jota.account.store.AccountStoreImpl;
import org.iota.jota.config.types.FileConfig;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.pow.pearldiver.PearlDiverLocalPoW;
import org.iota.jota.store.JsonFlatFileStore;
import org.iota.jota.utils.BundleValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class IotaAccountIntegrationTest {
    
    private static String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc lorem lorem, tristique vel pharetra in, consectetur sed ex. Maecenas sit amet porttitor mauris, in ullamcorper augue. Etiam pellentesque in velit ut pellentesque. Cras dignissim quam ut imperdiet pellentesque. Proin ac ullamcorper mi. Integer suscipit sagittis augue, quis elementum dui venenatis ut. Phasellus id elit malesuada, convallis libero eget, fringilla sem. In tincidunt semper massa, nec dictum velit hendrerit et. Maecenas venenatis, felis ut eleifend elementum, enim mauris pulvinar mi, mattis posuere dolor nunc quis metus.\n" + 
            "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Etiam lacinia malesuada justo, sed gravida risus iaculis quis. Pellentesque lorem libero, feugiat sed nisl id, auctor euismod velit. Nunc sit amet ornare ante, eleifend dapibus ex. Nam imperdiet, arcu in laoreet hendrerit, lorem ex lobortis ex, sollicitudin gravida nisl dolor id enim. In at turpis arcu. Phasellus egestas tortor ipsum. Donec euismod odio ut diam blandit aliquet. Cras at consequat turpis. Etiam ut nibh sit amet nibh venenatis fermentum ac id arcu. Suspendisse mollis ex ac libero finibus, quis scelerisque arcu condimentum.\n" + 
            "Duis leo massa, ullamcorper sed finibus in, ullamcorper ut lacus. Nulla commodo tellus at metus consectetur convallis. Sed finibus, felis vitae pharetra porttitor, nisl lectus tempus justo, quis imperdiet neque ante id turpis. Sed fermentum molestie erat quis luctus. Pellentesque et semper elit. Fusce at lectus cursus, hendrerit elit ac, cursus orci. Nunc vestibulum cursus leo, ac varius urna sagittis in. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris finibus lorem dolor, eu vehicula ante varius ac. Proin ut hendrerit massa. Maecenas pellentesque mattis risus, ac fermentum ex pellentesque in. Donec mauris lectus, vehicula vitae velit in, efficitur consectetur quam. Donec ut ipsum rhoncus, congue justo sit amet, tincidunt est. Duis tempor iaculis massa, sit amet imperdiet arcu scelerisque non. Nullam quis mi tincidunt, fermentum mauris at, rutrum risus. Etiam pharetra leo odio, ut suscipit felis auctor et.\n" + 
            "Interdum et malesuada fames ac ante ipsum primis in faucibus. Aliquam condimentum sapien ac lacinia varius. Proin sollicitudin sem ligula. Morbi suscipit maximus lorem ut aliquet. Vestibulum at feugiat orci. Maecenas condimentum vitae est vitae pretium. Etiam nec metus ut purus ullamcorper sodales at nec quam. Proin eleifend ante felis, et molestie mauris tincidunt ut. Fusce sit amet est tempus, luctus risus ac, imperdiet mi. Aliquam hendrerit leo orci. Mauris sed nisi ut mauris iaculis condimentum vitae fringilla libero. Praesent egestas ultricies nisl, interdum vehicula ipsum lobortis et. Praesent vitae pulvinar lacus, sed vestibulum dui. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec accumsan fermentum euismod.\n" + 
            "Morbi auctor massa et sem vulputate feugiat. In hac habitasse platea dictumst. Integer ullamcorper ipsum nec orci ultrices consectetur. Nullam id placerat odio, et eleifend lacus. Phasellus id mi ornare, blandit massa sit amet, rhoncus leo. Vestibulum pharetra bibendum lorem, quis efficitur odio consectetur sit amet. Nunc leo diam, interdum ac magna id, ornare porta nunc. Morbi lectus nibh, rutrum in rutrum sit amet, fermentum ut risus. Mauris tempor eget tortor ac iaculis. Suspendisse laoreet ullamcorper turpis, a rutrum diam ornare quis. Proin ac diam sodales risus volutpat pulvinar. Vestibulum scelerisque lorem ac leo auctor, sit amet fringilla dolor viverra. Fusce vel pretium magna. Etiam id dui fermentum, tristique arcu sed, finibus nisi.";
 

    private static final String TEST_SEED = "IJEEPFTJEFGFRDTSQGLGEAUZPUJFP9LDMDOOYUNOZFJ9JMJFALJATJGHEUPHHFVTFDYSGZNKMRK9EQKWG";
    private static final String TEST_SEED_ID = "J9SPZIPMIHEGZEBNDLMBTVVTCGQREQXZFXUYTJTYVQCR9TUZWZDBSJBOZLTTLJYXCGGVAIEQFPWLNUGHD";
    private static final String ADDR_1_SEC_3 = "TAKWNELREDNHLFYCQ9LMGZVYGTPTABFDEPQZILJAYAZSSCPXMEGCVAH9AHTJRDPVDCGIH9APCWG9KBSGA9VKXRLMU9";
    private static final String ADDR_2_SEC_3 = "LJGVBUTNFABXQUGMQROIMVXAPHAPXGZUFDMLALGSNUBAZLONQCKAUIMNUIHEDVRMBUEPUMCLHQZHVHCZBRGGLRBGAC";
    private IotaAPI iotaAPI;

    private File file;

    private AccountStoreImpl store;

    @BeforeEach
    public void setUp() throws Exception {
        file = File.createTempFile("client", "account");
        iotaAPI = new IotaAPI.Builder()
                .config(new FileConfig())
                .localPoW(new PearlDiverLocalPoW())
                .build();
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
        IotaAccount account = new IotaAccount.Builder(TEST_SEED)
                .securityLevel(3)
                .mwm(9)
                .store(store)
                .api(iotaAPI)
                .build();

        assertTrue(account.loaded, "Account should be loaded after build");
        assertEquals(TEST_SEED_ID, account.getId(), "Account ID should be set to the seed id ");
        assertTrue(account.isNew(), "Should be a new account");
        assertEquals(0, account.availableBalance(), "New accounts should have 0 balance");
        assertEquals(0, account.totalBalance(), "New accounts should have 0 balance");
    }

    @Test
    void sendZeroValueTest() throws AccountError, InterruptedException, ExecutionException {
        // CDA needed for getting first addr value
        JsonFlatFileStore json = new JsonFlatFileStore(this.getClass().getResourceAsStream("/accounts/client-test.store"), new NullOutputStream());
        store = new AccountFileStore(json);
        
        IotaAccount account = new IotaAccount.Builder(TEST_SEED).mwm(9).store(store).api(iotaAPI).build();
        
        Bundle sent = account.sendZeroValue("Another IOTA Accounts test run at " + new Date().toString(), 
                "IOTA9ACCOUNTS", 
                account.getAccountManager().getNextAddress().getAddress().getHashCheckSum()).get();

        assertTrue(BundleValidator.isBundle(sent), "Should be a valid bundle");
    }
    
    @Test
    void sendLongZeroMessage() throws ArgumentException, SendException, InterruptedException, ExecutionException {
        // CDA needed for getting first addr value
        JsonFlatFileStore json = new JsonFlatFileStore(this.getClass().getResourceAsStream("/accounts/client-test.store"), new NullOutputStream());
        store = new AccountFileStore(json);
        
        IotaAccount account = new IotaAccount.Builder(TEST_SEED).mwm(9).store(store).api(iotaAPI).build();
        
        Bundle sent = account.sendZeroValue(lorem, "IOTA9ACCOUNTS", 
                account.getAccountManager().getNextAddress().getAddress().getHashCheckSum()).get();

        assertTrue(BundleValidator.isBundle(sent), "Should be a valid bundle");
    }
    
    @Test
    void sendValueTest() throws AccountError, InterruptedException, ExecutionException {
        IotaAPI iotaAPI = fakeBalance(ADDR_1_SEC_3, 5l);
        
        JsonFlatFileStore json = new JsonFlatFileStore(this.getClass().getResourceAsStream("/accounts/client-test.store"), new NullOutputStream());
        store = new AccountFileStore(json);
        
        IotaAccount account = new IotaAccount.Builder(TEST_SEED).mwm(9).store(store).api(iotaAPI).build();
        
        Date timeOut = new Date(Long.MAX_VALUE);
        ConditionalDepositAddress cda = account.newDepositAddress(timeOut, false, 10).get();
        
        Bundle sent = account.send(cda.getDepositAddress().getHashCheckSum(), 5, 
                "Another IOTA Accounts test run at " + new Date().toString(), 
                "IOTA9ACCOUNTS").get();

        assertTrue(BundleValidator.isBundle(sent), "Should be a valid bundle");
    }
    
    @Test
    void sendLongValueTest() throws AccountError, InterruptedException, ExecutionException {
        IotaAPI iotaAPI = fakeBalance(ADDR_1_SEC_3, 10l);
        
        JsonFlatFileStore json = new JsonFlatFileStore(this.getClass().getResourceAsStream("/accounts/client-test.store"), new NullOutputStream());
        store = new AccountFileStore(json);
        
        IotaAccount account = new IotaAccount.Builder(TEST_SEED).mwm(9).store(store).api(iotaAPI).build();
        
        Date timeOut = new Date(Long.MAX_VALUE);
        ConditionalDepositAddress cda = account.newDepositAddress(timeOut, false, 10).get();
        
        Bundle sent = account.send(cda.getDepositAddress().getHashCheckSum(), 10, 
                lorem, "IOTA9ACCOUNTS").get();

        assertTrue(BundleValidator.isBundle(sent), "Should be a valid bundle");
    }
    
    @Test
    void sendLongMultiValueTest() throws AccountError, InterruptedException, ExecutionException {
        IotaAPI iotaAPI = fakeBalance(ADDR_1_SEC_3, 5l);
        iotaAPI = fakeBalance(ADDR_2_SEC_3, 5l, iotaAPI);
        
        JsonFlatFileStore json = new JsonFlatFileStore(this.getClass().getResourceAsStream("/accounts/client-testMulti.store"), new NullOutputStream());
        store = new AccountFileStore(json);
        
        IotaAccount account = new IotaAccount.Builder(TEST_SEED).mwm(9).store(store).api(iotaAPI).build();
        
        Date timeOut = new Date(Long.MAX_VALUE);
        ConditionalDepositAddress cda = account.newDepositAddress(timeOut, false, 10).get();
        
        Bundle sent = account.send(cda.getDepositAddress().getHashCheckSum(), 10, 
                lorem, "IOTA9ACCOUNTS").get();

        assertTrue(BundleValidator.isBundle(sent), "Should be a valid bundle");
    }
    
    @Test
    void sendLongMultiValueRemainderTest() throws AccountError, InterruptedException, ExecutionException {
        IotaAPI iotaAPI = fakeBalance(ADDR_1_SEC_3, 6l);
        iotaAPI = fakeBalance(ADDR_2_SEC_3, 5l, iotaAPI);
        
        JsonFlatFileStore json = new JsonFlatFileStore(this.getClass().getResourceAsStream("/accounts/client-testMulti.store"), new NullOutputStream());
        store = new AccountFileStore(json);
        
        IotaAccount account = new IotaAccount.Builder(TEST_SEED).mwm(9).store(store).api(iotaAPI).build();
        
        Date timeOut = new Date(Long.MAX_VALUE);
        ConditionalDepositAddress cda = account.newDepositAddress(timeOut, false, 10).get();
        
        Bundle sent = account.send(cda.getDepositAddress().getHashCheckSum(), 10, 
                lorem, "IOTA9ACCOUNTS").get();

        assertTrue(BundleValidator.isBundle(sent), "Should be a valid bundle");
    }
    
    private IotaAPI fakeBalance(String addr, long balance) {
        IotaAPI spyApi = Mockito.spy(iotaAPI);
        return fakeBalance(addr, balance, spyApi);
    }
    
    private IotaAPI fakeBalance(String addr, long balance, IotaAPI spyApi) {
        when(spyApi.getBalance(100, addr)).thenReturn(balance);
        return spyApi;
    }
}
