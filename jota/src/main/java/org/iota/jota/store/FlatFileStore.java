package org.iota.jota.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlatFileStore implements Store {

    private static final Logger log = LoggerFactory.getLogger(FlatFileStore.class);
    
    private File file;
    
    private InputStream inputStream;
    private OutputStream outputStream;
    
    private MemoryStore memoryStore = null;
    
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
        
        Map<String, Serializable> store = new HashMap<>();
        try {
            ObjectInputStream s = new ObjectInputStream(inputStream);
            
            store = (Map<String, Serializable>) s.readObject();
            
            memoryStore = new MemoryStore(store);

            s.close();
        } catch (EOFException e) {
            // Empty
            memoryStore = new MemoryStore(store);
        }
        
        memoryStore.load();
    }

    private Properties loadFromBytes(Properties byteProperties) {
        Properties properties = new Properties();
        for (Object key : byteProperties.keySet()) {
            String prop = byteProperties.getProperty(key.toString());
            
            ObjectInputStream objIn;
            try {
                objIn = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(prop.getBytes())));
                Object actual = objIn.readObject();
                
                objIn.close();
                
                properties.put(key.toString(), actual);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        return properties;
    }

    @Override
    public void save() {
        memoryStore.save();
        try {
            if (file != null) {
                outputStream = new FileOutputStream(file);
            }
            
            ObjectOutputStream s = new ObjectOutputStream(outputStream);
            s.writeObject(memoryStore.getStore());
            s.flush();
            s.close();
            
            System.out.println(memoryStore.getStore());
        } catch (IOException e) {
            log.warn("Failed to save config to disk! " + e.getMessage());
        } finally {
            
        }
        
    }

    private Properties saveToBytes(Map<String, Serializable> store) {
        Properties properties = new Properties();
        for (String key : store.keySet()) {
            Serializable prop = store.get(key);
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objOut;
            try {
                objOut = new ObjectOutputStream(out);
                objOut.writeObject(prop);
                
                properties.setProperty(key, new String(Base64.encode(out.toByteArray())));

                objOut.flush();
                objOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return properties;
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
