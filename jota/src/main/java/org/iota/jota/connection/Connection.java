package org.iota.jota.connection;

public interface Connection extends IotaNodeApi {
    
    // version header
    String X_IOTA_API_VERSION_HEADER_NAME = "X-IOTA-API-Version";
    String X_IOTA_API_VERSION_HEADER_VALUE = "1";
    
    boolean start();
    
    void stop();
    
    int port();
    String url();
}
