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
    
    /**
     * Careful when supplying a stream, FlatFileStore CONTINUOUSLY writes to output stream.
     * 
     * @param inputStream
     * @param outputStream
     */
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
        Map<String, Serializable> store = loadFromInputStream(inputStream);
        memoryStore = new MemoryStore(store);
        memoryStore.load();
        
        inputStream.close();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Map<String, Serializable> loadFromInputStream(InputStream stream){
        Map<String, Serializable> store = new HashMap<String, Serializable>();
        try {
            Properties properties = new Properties();
            properties.load(stream);
            
            return (Map) properties;
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
    public void save(boolean closeResources) {
        memoryStore.save(closeResources);
        try {
            boolean closed = false;
            if (file != null && outputStream == null) {
                outputStream = new FileOutputStream(file);
                closed = true;
            }
            
            writeToOutputStream(outputStream, memoryStore.getAll());
            if (closed) {
                outputStream.close();
                outputStream = null;
            }
        } catch (IOException e) {
            log.warn("Failed to save config to disk! " + e.getMessage());
        } finally {
            if (closeResources) {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    //TODO Throw account error
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Serializable get(String key) {
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
    public Map<String, Serializable> getAll() {
        return memoryStore.getAll();
    }
    
    @Override
    public boolean canWrite() {
        return true;
    }
    
    @Override
    public String toString() {
        return "FlatFileStore: [" + (file != null ? file.getName() : inputStream.toString()) + "]";
    }

    @Override
    public Serializable delete(String key) {
        return memoryStore.delete(key);
    }
}
