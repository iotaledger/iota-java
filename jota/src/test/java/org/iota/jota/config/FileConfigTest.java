package org.iota.jota.config;

import static org.junit.Assert.*;

import org.iota.jota.store.FlatFileStore;
import org.junit.Before;
import org.junit.Test;

public class FileConfigTest {
    
    @Before
    public void setUp() throws Exception {
        
    }
    
    @Test
    public void testOldConfig() throws Exception {
        FlatFileStore store = new FlatFileStore(this.getClass().getResourceAsStream("/configs/config_old.properties"), System.out);
        FileConfig config = new FileConfig(store);
        
        assertEquals("http", config.getLegacyProtocol());
        assertEquals("nodes.testnet.iota.org", config.getLegacyHost());
        assertEquals(80, config.getLegacyPort());
    }
    
    
    @Test
    public void testNewConfig() throws Exception {
        FlatFileStore store = new FlatFileStore(this.getClass().getResourceAsStream("/configs/config_new.properties"), System.out);
        FileConfig config = new FileConfig(store);
        
        assertEquals(500, config.getConnectionTimeout());
        assertEquals("client_new.store", config.getStore().toString());
        assertEquals(15, config.getMwm());
        assertEquals(5, config.getDepth());
        assertEquals(3, config.getSecurityLevel());
    }
    
    @Test
    public void testNewConfigMultipleNodes() throws Exception {
        FlatFileStore store = new FlatFileStore(this.getClass().getResourceAsStream("/configs/config_new_nodes_2.properties"), System.out);
        FileConfig config = new FileConfig(store);
        
        assertEquals(2, config.getNodes().size());
    }
}
