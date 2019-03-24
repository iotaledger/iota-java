package org.iota.jota.store;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.utils.JsonParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class MongoDBStore extends DatabaseStore {

    private MongoClientOptions options;
    
    private List<MongoCredential> credentials;
    
    private ServerAddress address;
    
    private MongoClient client;
    
    private MongoCollection<Document> collection;
    private MongoDatabase database;
    
    private JsonParser jsonParser;
    private UpdateOptions updateOptions;
    
    public MongoDBStore() {
        this(IotaDefaultConfig.Defaults.DATABASE_NAME, 
                IotaDefaultConfig.Defaults.TABLE_NAME, "localhost", 27017);
    }
    
    public MongoDBStore(String databaseName) {
        this(databaseName, IotaDefaultConfig.Defaults.TABLE_NAME, "localhost", 27017);
    }

    public MongoDBStore(String databaseName, String tableName) {
        this(databaseName, tableName, "localhost", 27017);
    }
    
    public MongoDBStore(String databaseName, String tableName, String hostName) {
        this(databaseName, tableName, hostName, 27017);
    }
    
    public MongoDBStore(String databaseName, String tableName, URL databaseUrl) {
        this(databaseName, tableName, databaseUrl.getHost(), databaseUrl.getPort());
    }
    
    public MongoDBStore(String databaseName, String tableName, String hostName, int port) {
        super(databaseName, tableName);
        this.address = new ServerAddress(hostName, port);
        
        this.jsonParser = new JsonParser();
        
        this.updateOptions = new UpdateOptions();
        this.updateOptions.upsert(true);
        
        this.options = MongoClientOptions.builder().build();
    }
    
    /**
     * Adds a credential to the mongodb connection.
     * Must be added before {@link #load()}
     * @param userName
     * @param password
     */
    public void addCredentials(String userName, String password) {
        addCredentials(userName, password, getDatabaseName());
    }
    
    public void addCredentials(String userName, String password, String database) {
        if (credentials == null) {
            credentials = new LinkedList<>();
        }
        
        credentials.add(
                MongoCredential.createCredential(userName, database, password.toCharArray())
                );
    }
    
    public void setOptions(MongoClientOptions options) {
        this.options = options;
    }
    
    @Override
    public void load() throws Exception {
        
        if (credentials != null) {
            client = new MongoClient(address, credentials, options);
        } else {
            client = new MongoClient(address, options);
        }
         
        database = client.getDatabase(getDatabaseName());

        try {
            collection = database.getCollection(getTableName());
        } catch (IllegalArgumentException e) {
            try {
                database.createCollection(getTableName());
                collection = database.getCollection(getTableName());
            } catch (IllegalArgumentException e2) {
                // TODO Log account error
                e2.printStackTrace();
            }
        }
    }

    @Override
    public void save(boolean closeResources) throws Exception {
        if (closeResources) {
            collection = null;
            database = null;
            client.close();
        }
    }

    @Override
    public <T extends Serializable> T get(String key) {
        return get(key, null);
    }

    @Override
    public <T extends Serializable> T get(String key, T def) {
        Document doc = collection.find(Filters.eq("_id", key))
                .projection(Projections.excludeId())
                .first();
        
        if (null == doc) {
            return def;
        } else {
            try {
                
                TypeReference<T> t = new TypeReference<T>(){};
                JavaType type = jsonParser.getTypeFactory().constructType(t);
                
                String json = doc.toJson();
                Object res = jsonParser.parsJson(json, type);
                
                return (T) res;
            } catch (IOException e) {
                // TODO Log account error
                e.printStackTrace();
                return def;
            }
        }
    }
    
    @Override
    public Serializable delete(String key) {
        Serializable old = get(key);
        DeleteResult res = collection.deleteOne(Filters.eq("_id", key));
        
        if (old != null && res.getDeletedCount() == 0) {
            // TODO Log account error
        }
        
        return old;
    }

    @Override
    public <T extends Serializable> T set(String key, T value) {
        try {
            String json = jsonParser.toJson(value);
            UpdateResult result = collection.replaceOne(
                    Filters.eq("_id", key), 
                    Document.parse(json), 
                    updateOptions);
            if (result.isModifiedCountAvailable() && result.getModifiedCount() == 0) {
                // TODO Log account error
            }
            
            return null;
        } catch (JsonProcessingException e) {
            // TODO Log account error
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Serializable> getAll() {
        Map<String, Serializable> all = new HashMap<String, Serializable>();
        
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document next = cursor.next();
                String id = next.getString("_id");
                try {
                    all.put(id, jsonParser.parsJson(next.toJson()));
                } catch (IOException e) {
                    // TODO Log account error
                    e.printStackTrace();
                }
            }
        } finally {
            cursor.close();
        }
        
        return all;
    }
}
