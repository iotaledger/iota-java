package org.iota.jota.account.store;

public abstract class DatabaseStore extends AccountStoreImpl {

    private final String tableName;
    private final String databaseName;

    public DatabaseStore(String databaseName, String tableName) {
        this.tableName = tableName;
        this.databaseName = databaseName;
    }
    
    protected String getTableName() {
        return tableName;
    }
    
    protected String getDatabaseName() {
        return databaseName;
    }
    
}
