package org.iota.jota.store;

public abstract class DatabaseStore implements Store {

    private final String tableName;
    private final String databaseName;

    public DatabaseStore(String databaseName, String tableName) {
        this.tableName = tableName;
        this.databaseName = databaseName;
    }
    
    @Override
    public boolean canWrite() {
        return true;
    }
    
    protected String getTableName() {
        return tableName;
    }
    
    protected String getDatabaseName() {
        return databaseName;
    }
}
