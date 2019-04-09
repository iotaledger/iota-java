package org.iota.jota.account.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.bson.Document;
import org.iota.jota.account.PendingTransfer;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.StoredDepositAddress;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trits;
import org.iota.jota.types.Trytes;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.TrytesConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class MongoDBStoreTest {
    
    private static final String ID = "id";
    private MongoStore store;

    @Before
    public void setUp() {
        this.store = new MongoStore("heroku_d8ntbcbh", IotaDefaultConfig.Defaults.TABLE_NAME, 
                "ds157538.mlab.com", 57538);
        store.addCredentials("heroku_d8ntbcbh", "2pl8q378ppb2ofgiegmc0qs9in");
    }
    
    @After
    public void tearDown() throws Exception {
        store.removeAccount(ID);
        store.shutdown();
    }
    
    @Test
    public void testConnection() {
        try {
            store.start();
        } catch (Exception e) {
            fail("Store should connect to database");
        }
    }
    
    @Test
    public void depositRequestTest() throws Exception{
        store.start();
        
        DepositRequest request = new DepositRequest(new Date(0), false, 5);
        StoredDepositAddress stored = new StoredDepositAddress(request, 1);
        
        store.addDepositAddress(ID, 1, stored);
        
        Map<Integer, StoredDepositAddress> result = store.getDepositAddresses(ID);
        assertEquals("Store should have a value after setting", 1, result.size());
        assertEquals("Store should have the same value after setting", stored, result.get(1));
        
        store.removeDepositAddress(ID, 1);
        result = store.getDepositAddresses(ID);
        assertEquals("Store should no longer have this field", 0, result.size());
    }
    
    @Test
    public void indexTest() throws Exception{
        store.start();

        assertEquals("Store should start with index -1", -1, store.readIndex(ID));
        
        store.writeIndex(ID, 5);
        
        int result = store.readIndex(ID);
        assertEquals("Store should have a value after setting", 5, result);
    }
    
    @Test
    public void pendingTransferTest() throws Exception{
        store.start();
        
        Hash hash = new Hash(Constants.NULL_HASH);
        Hash nextTail = new Hash(Constants.NULL_HASH.substring(1) + "A");
        
        store.addPendingTransfer(ID, hash, new Trytes[] {new Trytes("WE9ARE9TRYTES")}, 1);
        
        PendingTransfer transfer = new PendingTransfer(
                Arrays.asList(new Trits[] { 
                        new Trits(Converter.trits("WE9ARE9TRYTES")) 
                    }));
        transfer.addTail(hash);
        
        Map<String, PendingTransfer> result = store.getPendingTransfers(ID);
        assertEquals("Store should have a value after setting", 1, result.size());
        assertEquals("Store should have the same value after setting", transfer, result.get(hash.getHash()));
        
        store.addTailHash(ID, hash, nextTail);
        result = store.getPendingTransfers(ID);
        
        transfer.addTail(nextTail);
        assertEquals("Store should have a value after setting", 1, result.size());
        assertEquals("Store should have the same value after setting", transfer, result.get(hash.getHash()));
        
        store.removePendingTransfer(ID, hash);
        result = store.getPendingTransfers(ID);
        assertEquals("Store should no longer have this pending transfer", 0, result.size());
    }
}
