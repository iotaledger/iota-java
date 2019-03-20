package org.iota.jota.store;

public abstract class DatabaseStore implements Store {

    protected static final String accountsTableName = "accounts";
    
    @Override
    public boolean canWrite() {
        return true;
    }
    
    public String getTableName() {
        return accountsTableName;
    }
}
