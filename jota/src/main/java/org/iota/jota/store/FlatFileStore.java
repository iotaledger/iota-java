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
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlatFileStore implements Store {

    private static final Logger log = LoggerFactory.getLogger(FlatFileStore.class);
    
    private File file;
    
    private InputStream inputStream;
    private OutputStream outputStream;
    
    protected MemoryStore memoryStore = null;
    
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
            System.out.println(file.getName());
            inputStream = new FileInputStream(file);
        }
        
        Map<String, Serializable> store = loadFromInputStream(inputStream);
        
        memoryStore = new MemoryStore(store);
        memoryStore.load();
    }
    
    protected Map<String, Serializable> loadFromInputStream(InputStream stream){
        Map<String, Serializable> store = new HashMap<String, Serializable>();
        try {
            Properties properties = new Properties();
            properties.load(stream);
            properties.putAll(store);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return store;
    }
    
    protected void writeToOutputStream(OutputStream stream, Map<String, Serializable> store) throws IOException {
        Properties properties = new Properties();
        properties.putAll(store);

        properties.store(stream, null);
    }

    @Override
    public void save() {
        memoryStore.save();
        try {
            if (file != null) {
                outputStream = new FileOutputStream(file);
            }
            
            writeToOutputStream(outputStream, memoryStore.getStore());
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