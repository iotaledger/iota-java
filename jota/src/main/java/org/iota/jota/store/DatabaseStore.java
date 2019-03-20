package org.iota.jota.store;

public abstract class DatabaseStore implements Store {
    
    @Override
    public boolean canWrite() {
        return true;
    }
}
