package org.iota.jota.account.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Map;

import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.StoredDepositRequest;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MongoDBStoreTest {

    
    private MongoStore store;

    @Before
    public void setUp() {
        this.store = new MongoStore("heroku_d8ntbcbh", IotaDefaultConfig.Defaults.TABLE_NAME, 
                "ds157538.mlab.com", 57538);
        store.addCredentials("heroku_d8ntbcbh", "2pl8q378ppb2ofgiegmc0qs9in");
    }
    
    @After
    public void tearDown() throws Exception {
        store.shutdown();
    }
    
    @Test
    public void testConnection() {
        try {
            store.load();
        } catch (Exception e) {
            fail("Store should connect to database");
        }
    }
    
    @Test
    public void addAndRemove() throws Exception{
        store.load();
        
        DepositRequest request = new DepositRequest(new Date(0), false, 5);
        StoredDepositRequest stored = new StoredDepositRequest(request, 1);
        store.addDepositRequest("id", 1, stored);
        
        Map<Integer, StoredDepositRequest> result = store.getDepositRequests("id");
        assertEquals("Store should have a value after setting", 1, result.size());
        assertEquals("Store should have the same value after setting", stored, result);
        
        store.removeDepositRequest("id", 1);
        result = store.getDepositRequests("id");
        assertEquals("Store should no longer have this field", 0, result.size());
    }
}
