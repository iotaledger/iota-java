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
                log.debug(file.getName() + " not found. Rolling back for another solution...");
            }   
            inputStream = new FileInputStream(file);
        }
        Map<String, String> store = loadFromInputStream(inputStream);
        
        memoryStore = new MemoryStore(store);
        memoryStore.load();
        
        inputStream.close();
    }
    
    protected Map<String, String> loadFromInputStream(InputStream stream){
        Map<String, String> store = new HashMap<String, String>();
        try {
            Properties properties = new Properties();
            properties.load(stream);
            
            return (Map) properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return store;
    }
    
    protected void writeToOutputStream(OutputStream stream, Map<String, String> store) throws IOException {
        Properties properties = new Properties();
        properties.putAll(store);

        properties.store(stream, null);
    }

    @Override
    public void save(boolean closeResources) {
        memoryStore.save(closeResources);
        try {
            if (file != null) {
                outputStream = new FileOutputStream(file);
            }
            
            writeToOutputStream(outputStream, memoryStore.getAll());
        } catch (IOException e) {
            log.warn("Failed to save config to disk! " + e.getMessage());
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                //TODO Throw account error
                e.printStackTrace();
            }
        }
    }

    @Override
    public String get(String key) {
        return memoryStore.get(key, null);
    }

    @Override
    public String get(String key, String def) {
        return memoryStore.get(key, def);
    }

    @Override
    public String set(String key, String value) {
        return memoryStore.set(key, value);
    }
    
    @Override
    public Map<String, String> getAll() {
        return memoryStore.getAll();
    }
    
    @Override
    public boolean canWrite() {
        return true;
    }
    
    @Override
    public String toString() {
        return file.getName();
    }

    @Override
    public String delete(String key) {
        return memoryStore.delete(key);
    }
}
