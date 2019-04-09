package org.iota.jota.account.store;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModelBuilder;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.PropertyModelBuilder;
import org.bson.json.JsonWriterSettings;
import org.iota.jota.account.AccountState;
import org.iota.jota.account.ExportedAccountState;
import org.iota.jota.account.PendingTransfer;
import org.iota.jota.account.deposits.StoredDepositAddress;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trytes;
import org.iota.jota.utils.JsonParser;

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
    
    //Used to deserialize mongodb bson to jackson accepted json
    private static final JsonWriterSettings settings = JsonWriterSettings.builder()
            .int64Converter((value, writer) -> writer.writeNumber(value.toString()))
            .dateTimeConverter((value, writer) -> writer.writeString(value + ""))
            .build();
    
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
        
        List<Convention> conventions = new LinkedList<>(Conventions.DEFAULT_CONVENTIONS);
        conventions.add(new SnakeConvention());
        
        PojoCodecProvider.Builder builder = PojoCodecProvider.builder();
        builder.register(StringAccountState.class);
        builder.conventions(conventions);
        
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder()
                        .conventions(conventions)
                        .automatic(true)
                        .build())
        );
        
        this.options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry)
                .build();
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
            e.printStackTrace();
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
        
        if (client != null) {
            client.close();
        }
    }
    
    @Override
    public AccountState loadAccount(String id) {
        StringAccountState state = collection.find(Filters.eq("_id", id), StringAccountState.class)
                .projection(Projections.excludeId())
                .first();
        
        AccountState accState;
        if (null == state) {
            accState = new AccountState();
            saveAccount(id, accState);
        } else {
            accState = state.toAccountState();
        }

        return accState;
    }

    @Override
    public void saveAccount(String id, AccountState state) {
        Document doc = new Document(id, new StringAccountState(state));
        
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
        Document index = collection.find(Filters.eq("_id", id))
            .projection(Projections.fields(Projections.include(INDEX)))
            .first();
        return index != null && index.containsKey(INDEX) ? index.getInteger(INDEX) : -1;
    }

    @Override
    public void writeIndex(String id, int index) {
        UpdateResult result = collection.updateOne(
                Filters.eq("_id", id), 
                new Document("$set", new Document(INDEX, index)),
                updateOptions);
        if (result.isModifiedCountAvailable() && result.getModifiedCount() == 0) {
            // TODO Log account error
        }
    }

    @Override
    public void addDepositAddress(String id, int index, StoredDepositAddress request) {
        UpdateResult result = collection.updateOne(
                Filters.eq("_id", id), 
                new Document("$set", new Document(DEPOSIT, new Document(index + "", request))), 
                updateOptions);
        if (result == null) {
            // TODO Log account error
        }
    }

    @Override
    public void removeDepositAddress(String id, int index) {
        UpdateResult result = collection.updateOne(
                Filters.eq("_id", id), 
                new Document("$unset", new Document(DEPOSIT, new Document(index + "", ""))),
                updateOptions);
        if (result == null) {
            // TODO Log account error
        }
    }

    @Override
    public Map<Integer, StoredDepositAddress> getDepositAddresses(String id) {
        Document requests = collection.find(
                Filters.eq("_id", id))
                .projection(Projections.fields(Projections.include(DEPOSIT)))
                .first();
        
        Map<Integer, StoredDepositAddress> deposits = null;
        if (requests != null) {
            Map<String, Document> strDeposits = requests.get(DEPOSIT, Map.class);

            deposits = new java.util.HashMap<>();
            if (strDeposits  != null) {
                for (Entry<String, Document> entry : strDeposits.entrySet()) {
    
                    BsonDocument store = entry.getValue().toBsonDocument(StoredDepositAddress.class, collection.getCodecRegistry());
                    try {
                        // Get custom Reverse snake parser, read Document as bson using custom builder
                        StoredDepositAddress dep = JsonParser.get().getObjectMapper().readValue(
                                store.toJson(settings), 
                                StoredDepositAddress.class);
                        
                        deposits.put(Integer.valueOf(entry.getKey()), dep);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                }
            }
        }
        
        return deposits;
    }

    @Override
    public void addPendingTransfer(String id, Hash tailTx, Trytes[] bundleTrytes, int... indices) {
        PendingTransfer transfer = new PendingTransfer(super.trytesToTrits(bundleTrytes));
        transfer.addTail(tailTx);

        UpdateResult result = collection.updateOne(
                Filters.eq("_id", id), 
                new Document("$set", 
                        new Document(PENDING, 
                        new Document(tailTx.getHash(), transfer))),
                updateOptions);
        
        if (result.isModifiedCountAvailable() && result.getModifiedCount() == 0) {
            // TODO Log account error
        }
    }

    @Override
    public void removePendingTransfer(String id, Hash tailHash) {
        UpdateResult result = collection.updateOne(
                Filters.eq("_id", id), 
                new Document("$unset", 
                        new Document(PENDING, 
                                new Document(tailHash.getHash(), ""))),
                updateOptions);
        if (result == null) {
            // TODO Log account error
        }
    }

    @Override
    public void addTailHash(String id, Hash tailHash, Hash newTailTxHash) {
        UpdateResult result = collection.updateOne(
                Filters.eq("_id", id), 
                new Document("$push", 
                        new Document(PENDING + "." + tailHash.getHash() + "." + TAILS, newTailTxHash)),
                updateOptions);
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
        
        Map<String, Document> requestsMap = requests.get(PENDING, Map.class);
        Map<String, PendingTransfer> pendingTransfers = new java.util.HashMap<>();
        if (requestsMap != null) {
            for (Entry<String, Document> entry : requestsMap.entrySet()) {

                BsonDocument store = entry.getValue().toBsonDocument(PendingTransfer.class, collection.getCodecRegistry());
                try {
                    // Get custom Reverse snake parser, read Document as bson using custom builder
                    PendingTransfer dep = JsonParser.get().getObjectMapper().readValue(
                            store.toJson(settings), 
                            PendingTransfer.class);
                    
                    pendingTransfers.put(entry.getKey(), dep);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        }
        return pendingTransfers;
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
    
    public MongoCollection<Document> getCollection() {
        return collection;
    }
    
    private class SnakeConvention implements Convention {
        @Override
        public void apply(ClassModelBuilder<?> classModelBuilder) {
            
            for (PropertyModelBuilder<?> fieldModelBuilder : classModelBuilder.getPropertyModelBuilders()) {
                fieldModelBuilder.discriminatorEnabled(false);
                fieldModelBuilder.readName(
                        fieldModelBuilder.getName()
                                .replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase());
                fieldModelBuilder.writeName(
                        fieldModelBuilder.getName()
                                .replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase());
            }
        }
    }
          
    private class StringAccountState {
        int keyIndex;
        Map<String, PendingTransfer> pendingTransfers;
        Map<String, StoredDepositAddress> depositRequests;
        
        public StringAccountState(AccountState state) {
            keyIndex = state.getKeyIndex();
            pendingTransfers = state.getPendingTransfers();
            
            depositRequests = new java.util.HashMap<>();
            for (Entry<Integer, StoredDepositAddress> entry : state.getDepositRequests().entrySet()) {
                depositRequests.put(entry.getKey() + "", entry.getValue());
            }
        }
        
        public AccountState toAccountState() {
            Map<Integer, StoredDepositAddress> depositRequests = new java.util.HashMap<>();
            for (Entry<String, StoredDepositAddress> entry : this.depositRequests.entrySet()) {
                depositRequests.put(Integer.valueOf(entry.getKey()), entry.getValue());
            }
            
            return new AccountState(keyIndex, depositRequests, pendingTransfers);
        }
    }
}
