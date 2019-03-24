package org.iota.jota.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Date;

import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MongoDBStoreTest {

    
    private MongoDBStore store;

    @Before
    public void setUp() {
        this.store = new MongoDBStore("heroku_d8ntbcbh", IotaDefaultConfig.Defaults.TABLE_NAME, 
                "ds157538.mlab.com", 57538);
        store.addCredentials("heroku_d8ntbcbh", "2pl8q378ppb2ofgiegmc0qs9in");
    }
    
    @After
    public void tearDown() throws Exception {
        store.save(true);
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
        
        store.set("test", request);
        store.save(false);
        
        DepositRequest result = store.get("test");
        assertEquals("Store should have value after setting", request, result);
        
        assertNull("Store should no longer have this field", store.delete("test"));
    }
}
