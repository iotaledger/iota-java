package org.iota.jota.store;

public abstract class DatabaseStore implements Store {

    private static final String tableName = "accounts";
    private static final String databaseName = "iota_account";
    
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
