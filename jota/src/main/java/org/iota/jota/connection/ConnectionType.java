package org.iota.jota.connection;

public enum ConnectionType {
    HTTP("http");
    
    private String type;

    ConnectionType(String type){
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return type;
    }
    
}
