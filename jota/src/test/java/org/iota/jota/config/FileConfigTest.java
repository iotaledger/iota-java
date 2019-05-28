package org.iota.jota.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.iota.jota.config.types.FileConfig;
import org.iota.jota.store.FlatFileStore;
import org.junit.jupiter.api.Test;

public class FileConfigTest {
    
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
