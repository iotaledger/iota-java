package org.iota.jota.store;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlatFileStore implements Store {

    private static final Logger log = LoggerFactory.getLogger(FlatFileStore.class);
    
    private File f;
    private Properties properties;
    
    public FlatFileStore(String location) {
        f = new File(location);
    }
    
    public FlatFileStore(URI location) {
        f = new File(location);
    }

    @Override
    public void load() throws Exception {
        properties = new Properties();
        
        if (!f.exists()) {
            f.createNewFile();
            
        } else if (f.canRead() && f.canWrite()) {
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            
            try {
                //node_config.properties
                fileReader = new FileReader(f);
                bufferedReader = new BufferedReader(fileReader);
                
                properties.load(bufferedReader);
                fileReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                log.debug(f.getName() + " not found. Rolling back for another solution...");
            } finally {
                if (bufferedReader != null) bufferedReader.close();
                if (fileReader != null) fileReader.close();
            }
        } else {
            log.debug("node_config.properties not found. Rolling back for another solution...");
        }
    }

    @Override
    public void save() {
        FileWriter fileWriter = null;
        
        try {
            fileWriter = new FileWriter(f);
            properties.store(fileWriter, "");
        } catch (IOException e) {
            log.warn("Failed to save config to disk! " + e.getMessage());
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    log.warn("Failed to close file stream! " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public <T extends Serializable> T get(String key) {
        return get(key,  null);
    }

    @Override
    public <T extends Serializable> T get(String key, T def) {
        String prop = properties.getProperty(key);
        if (null != prop) {
            ObjectInputStream objIn;
            try {
                objIn = new ObjectInputStream(new ByteArrayInputStream(prop.getBytes()));
                T actual = (T) objIn.readObject();
                
                return prop != null ? actual : def;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        return def;
    }

    @Override
    public <T extends Serializable> T set(String key, T value) {
        T prop = get(key);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut;
        try {
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(value);
            objOut.close();
            
            properties.setProperty(key, out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return prop;
    }
    
    @Override
    public boolean canWrite() {
        return true;
    }
    
    @Override
    public String toString() {
        return f.getName();
    }
}
