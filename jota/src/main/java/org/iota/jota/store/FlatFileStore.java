package org.iota.jota.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlatFileStore implements Store {
    
    private static final String KEY_INDEX = "index";

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
        if (!f.exists()) {
            f.createNewFile();
        } else if (f.canRead() && f.canWrite()) {
            properties = new Properties();
            
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
    public Serializable get(String key) {
        return properties.getProperty(key);
    }

    @Override
    public Serializable get(String key, Serializable def) {
        String prop = properties.getProperty(key);
        return prop != null ? prop : def;
    }

    @Override
    public Serializable set(String key, Serializable value) {
        String prop = properties.getProperty(key);
        properties.setProperty(key, value.toString());
        
        if (key.startsWith(KEY_INDEX)) {
            save();
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
