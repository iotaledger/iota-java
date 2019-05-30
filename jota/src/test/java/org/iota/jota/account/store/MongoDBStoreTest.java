package org.iota.jota.account.store;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.iota.jota.account.PendingTransfer;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.StoredDepositAddress;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trits;
import org.iota.jota.types.Trytes;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.Converter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Disabled
public class MongoDBStoreTest {
    
    private static final String ID = "id";
    private MongoStore store;

    @BeforeEach
    public void setUp() {
        // Want to run this test? Update it with your mongodb details locally for now
        this.store = new MongoStore("databasename", IotaDefaultConfig.Defaults.TABLE_NAME, 
                "url", 1337);
        //store.addCredentials("username", "password");
    }
    
    @AfterEach
    public void tearDown() throws Exception {
        store.removeAccount(ID);
        store.shutdown();
    }
    
    @Disabled
    @Test
    public void testConnection() {
        try {
            store.start();
        } catch (Exception e) {
            fail("Store should connect to database");
        }
    }
    
    @Disabled
    @Test
    public void depositRequestTest() throws Exception{
        store.start();
        
        DepositRequest request = new DepositRequest(new Date(0), false, 5);
        StoredDepositAddress stored = new StoredDepositAddress(request, 1);
        
        store.addDepositAddress(ID, 1, stored);
        
        Map<Integer, StoredDepositAddress> result = store.getDepositAddresses(ID);
        assertEquals(1, result.size(), "Store should have a value after setting");
        assertEquals(stored, result.get(1), "Store should have the same value after setting");
        
        store.removeDepositAddress(ID, 1);
        result = store.getDepositAddresses(ID);
        assertEquals(0, result.size(), "Store should no longer have this field");
    }
    
    @Disabled
    @Test
    public void indexTest() throws Exception{
        store.start();

        assertEquals(-1, store.readIndex(ID), "Store should start with index -1");
        
        store.writeIndex(ID, 5);
        
        int result = store.readIndex(ID);
        assertEquals(5, result, "Store should have a value after setting");
    }
    
    @Disabled
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
        assertEquals(1, result.size(), "Store should have a value after setting");
        assertEquals(transfer, result.get(hash.getHash()), "Store should have the same value after setting");
        
        store.addTailHash(ID, hash, nextTail);
        result = store.getPendingTransfers(ID);
        
        transfer.addTail(nextTail);
        assertEquals(1, result.size(), "Store should have a value after setting");
        assertEquals(transfer, result.get(hash.getHash()), "Store should have the same value after setting");
        
        store.removePendingTransfer(ID, hash);
        result = store.getPendingTransfers(ID);
        assertEquals(0, result.size(), "Store should no longer have this pending transfer");
    }
}
