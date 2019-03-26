package org.iota.jota.account.store;

import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.iota.jota.account.AccountState;
import org.iota.jota.account.ExportedAccountState;
import org.iota.jota.account.PendingTransfer;
import org.iota.jota.account.deposits.StoredDepositRequest;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trytes;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

@SuppressWarnings("unchecked")
public class MongoStore extends DatabaseStore {
    
    private static final String PENDING = "pending_transfers";
    private static final String DEPOSIT = "deposit_requests";
    private static final String INDEX = "key_index";
    private static final String TAILS = "tail_hashes";
    
    private MongoClientOptions options;
    
    private List<MongoCredential> credentials;
    
    private ServerAddress address;
    
    private MongoClient client;
    
    private MongoCollection<Document> collection;
    private MongoDatabase database;
    
    private UpdateOptions updateOptions;
    
    public MongoStore() {
        this(IotaDefaultConfig.Defaults.DATABASE_NAME, 
                IotaDefaultConfig.Defaults.TABLE_NAME, "localhost", 27017);
    }
    
    public MongoStore(String databaseName) {
        this(databaseName, IotaDefaultConfig.Defaults.TABLE_NAME, "localhost", 27017);
    }

    public MongoStore(String databaseName, String tableName) {
        this(databaseName, tableName, "localhost", 27017);
    }
    
    public MongoStore(String databaseName, String tableName, String hostName) {
        this(databaseName, tableName, hostName, 27017);
    }
    
    public MongoStore(String databaseName, String tableName, URL databaseUrl) {
        this(databaseName, tableName, databaseUrl.getHost(), databaseUrl.getPort());
    }
    
    public MongoStore(String databaseName, String tableName, String hostName, int port) {
        super(databaseName, tableName);
        this.address = new ServerAddress(hostName, port);
        
        this.updateOptions = new UpdateOptions().upsert(true);
        
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
    public void load() {
        //Cannot load anything since calling client.stop() ends the client opject for further use
    }

    @Override
    public boolean start() {
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
                return false;
            }
        }
        return true;
    }

    @Override
    public void shutdown() {
        collection = null;
        database = null;
        client.close();
    }
    
    @Override
    public AccountState loadAccount(String id) {
        AccountState state = collection.find(Filters.eq("_id", id), AccountState.class)
                .projection(Projections.excludeId())
                .first();
        
        if (null == state) {
            state = new AccountState();
            saveAccount(id, state);
        }

        return state;
    }

    @Override
    public void saveAccount(String id, AccountState state) {
        Document doc = new Document(id, state);
        
        UpdateResult result = collection.replaceOne(
                Filters.eq("_id", id), 
                doc, 
                updateOptions); 
        
        if (result.isModifiedCountAvailable() && result.getModifiedCount() == 0) {
            // TODO Log account error
        }
    }

    @Override
    public void removeAccount(String id) {
        DeleteResult result = collection.deleteOne(Filters.eq("_id", id));
        
        if (result.getDeletedCount() == 0) {
            // TODO Log account error
        }
    }

    @Override
    public int readIndex(String id) {
        Integer index = collection.find(Filters.eq("_id", id), Integer.class)
            .projection(Projections.fields(Projections.include(INDEX)))
            .first();
        return index;
    }

    @Override
    public void writeIndex(String id, int index) {
        UpdateResult result = collection.updateOne(
                Filters.eq("_id", id), 
                new Document(INDEX, index));
        if (result.isModifiedCountAvailable() && result.getModifiedCount() == 0) {
            // TODO Log account error
        }
    }

    @Override
    public void addDepositRequest(String id, int index, StoredDepositRequest request) {
        Document result = collection.findOneAndUpdate(
                Filters.eq("_id", id), 
                new Document(DEPOSIT, new BasicDBObject(index + "", request)));
        if (result == null) {
            // TODO Log account error
        }
    }

    @Override
    public void removeDepositRequest(String id, int index) {
        Document result = collection.findOneAndUpdate(
                Filters.eq("_id", id), 
                new Document(DEPOSIT, new BasicDBObject(index + "", null)));
        if (result == null) {
            // TODO Log account error
        }
    }

    @Override
    public Map<Integer, StoredDepositRequest> getDepositRequests(String id) {
        Document requests = collection.find(
                Filters.eq("_id", id))
                .projection(Projections.fields(Projections.include(DEPOSIT)))
                .first();
        return requests != null ? requests.get(DEPOSIT, Map.class) : null;
    }

    @Override
    public void addPendingTransfer(String id, Hash tailTx, Trytes[] bundleTrytes, int... indices) {
        PendingTransfer transfer = new PendingTransfer(super.trytesToTrits(bundleTrytes));
        transfer.addTail(tailTx);
        
        UpdateResult result = collection.updateOne(
                Filters.eq("_id", id), 
                new Document(PENDING, 
                        new BasicDBObject(tailTx.getHash(), transfer)));
        
        if (result.isModifiedCountAvailable() && result.getModifiedCount() == 0) {
            // TODO Log account error
        }
    }

    @Override
    public void removePendingTransfer(String id, Hash tailHash) {
        Document result = collection.findOneAndUpdate(
                Filters.eq("_id", id), 
                new Document(PENDING, new BasicDBObject(tailHash.getHash(), null)));
        if (result == null) {
            // TODO Log account error
        }
    }

    @Override
    public void addTailHash(String id, Hash tailHash, Hash newTailTxHash) {
        Document result = collection.findOneAndUpdate(
                Filters.eq("_id", id), 
                new Document(PENDING + "." + tailHash.getHash(), 
                        new BasicDBObject(TAILS, newTailTxHash)));
        if (result == null) {
            // TODO Log account error
        }
    }

    @Override
    public Map<String, PendingTransfer> getPendingTransfers(String id) {
        Document requests = collection.find(
                Filters.eq("_id", id))
                .projection(Projections.fields(Projections.include(PENDING)))
                .first();
        return requests != null ? requests.get(PENDING, Map.class) : null;
    }

    @Override
    public void importAccount(ExportedAccountState state) {
        saveAccount(state.getId(), state.getState());
    }

    @Override
    public ExportedAccountState exportAccount(String id) {
        AccountState state = loadAccount(id);
        return new ExportedAccountState(new Date(), id, state);
    }
}
