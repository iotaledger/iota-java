package org.iota.jota.connection;

import java.net.URL;

public interface Connection extends IotaNodeApi {
    
    // version header
    String X_IOTA_API_VERSION_HEADER_NAME = "X-IOTA-API-Version";
    String X_IOTA_API_VERSION_HEADER_VALUE = "1";
    
    /**
     * Attempts to start the node connection
     * 
     * @return <code>true</code> if start was successful, otherwise <code>false</code>
     */
    boolean start();
    
    /**
     * Stops this connection
     */
    void stop();
    
    /**
     * 
     * @return The node url this connection is connected to 
     */
    URL url();
    
    /**
     * A check if we are connected to the same node.
     * Some nodes can be connected to the same, but have different options for sending (Version, timeout)
     * 
     * @param other The other connection
     * @return <code>true</code> if it connects to the same node, otherwise <code>false</code>
     */
    boolean isConnectedToSameNode(Connection other);
}
