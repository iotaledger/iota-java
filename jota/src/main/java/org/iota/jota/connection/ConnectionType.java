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
    
    public static ConnectionType byType(String name) {
        for (ConnectionType t : values()) {
            if (t.getType().equals(name)) {
                return t;
            }
        }
        return null;
    }
}
