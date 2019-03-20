package org.iota.jota.store;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.iota.jota.utils.JsonParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class MongoDBStore extends DatabaseStore {

    private String hostName;
    private int port;
    
    private MongoClient client;
    
    private MongoCollection<Document> collection;
    private MongoDatabase database;
    private String databaseName;
    
    private JsonParser jsonParser;
    private UpdateOptions updateOptions;

    public MongoDBStore(String databaseName) {
        this(databaseName, "localhost", 27017);
    }
    
    public MongoDBStore(String databaseName, String hostName) {
        this(databaseName, hostName, 27017);
    }
    
    public MongoDBStore(String databaseName, URL databaseUrl) {
        this(databaseName, databaseUrl.getHost(), databaseUrl.getPort());
    }
    
    public MongoDBStore(String databaseName, String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        
        this.databaseName = databaseName;
        
        this.jsonParser = new JsonParser();
        
        this.updateOptions = new UpdateOptions();
        this.updateOptions.upsert(true);
    }
    
    @Override
    public void load() throws Exception {
         client = new MongoClient(hostName , port );
         database = client.getDatabase(databaseName);
         
         try {
             collection = database.getCollection(getTableName());
         } catch (IllegalArgumentException e) {
             try {
                 database.createCollection(getTableName());
                 collection = database.getCollection(getTableName());
             } catch (IllegalArgumentException e2) {
                 //TODO Log account error
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
    public Serializable get(String key) {
        return get(key, null);
    }

    @Override
    public <T extends Serializable> T get(String key, T def) {
        Document doc = collection.find(Filters.eq("_id", key)).first();
        
        if (null == doc) {
            return def;
        } else {
            try {
                return jsonParser.parsJson(doc.toJson());
            } catch (IOException e) {
                // TODO Log account error
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public <T extends Serializable> T set(String key, T value) {
        try {
            String json = jsonParser.toJson(value);
            
            UpdateResult result = collection.updateOne(Filters.eq("_id", key), Document.parse(json), updateOptions);
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
