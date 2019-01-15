package org.iota.jota.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.iota.jota.account.AccountState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class FlatFileStore implements Store {

    private static final Logger log = LoggerFactory.getLogger(FlatFileStore.class);
    
    private File file;
    
    private InputStream inputStream;
    private OutputStream outputStream;
    
    private MemoryStore memoryStore = null;
    
    ObjectMapper objectMapper = new ObjectMapper();
    
    public FlatFileStore(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
    
    public FlatFileStore(File file) {
        this.file = file;
    }
    
    public FlatFileStore(String location) {
        file = new File(location);
    }
    
    public FlatFileStore(URI location) {
        file = new File(location);
    }

    @Override
    public void load() throws Exception {
        if (null != file) {
            if (!file.exists()) {
                file.createNewFile();
            } 
            
            if (!file.canRead() || !file.canWrite()) {
                log.debug("node_config.properties not found. Rolling back for another solution...");
            }   
            
            inputStream = new FileInputStream(file);
        }
        
        Map<String, Serializable> store;
        try {
            store = objectMapper.readValue(inputStream, new TypeReference<Map<String, AccountState>>(){});
        } catch (MismatchedInputException e) {
            store = new HashMap<String, Serializable>();
        }
        
        memoryStore = new MemoryStore(store);
        memoryStore.load();
    }

    @Override
    public void save() {
        memoryStore.save();
        try {
            if (file != null) {
                outputStream = new FileOutputStream(file);
            }
            
            objectMapper.writeValue(outputStream, memoryStore.getStore());
        } catch (IOException e) {
            log.warn("Failed to save config to disk! " + e.getMessage());
        } finally {
            
        }
        
    }

    @Override
    public <T extends Serializable> T get(String key) {
        return memoryStore.get(key, null);
    }

    @Override
    public <T extends Serializable> T get(String key, T def) {
        return memoryStore.get(key, def);
    }

    @Override
    public <T extends Serializable> T set(String key, T value) {
        return memoryStore.set(key, value);
    }
    
    @Override
    public boolean canWrite() {
        return true;
    }
    
    @Override
    public String toString() {
        return file.getName();
    }
}
