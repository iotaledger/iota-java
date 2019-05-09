package org.iota.jota.dto.request;

import org.iota.jota.IotaAPICommand;

/**
 * This class represents the core API request 'addNeighbors' and 'removeNeighbors'.
 **/
public class IotaNeighborsRequest extends IotaCommandRequest {

    private String[] uris;

    /**
     * Initializes a new instance of the IotaNeighborsRequest class.
     * 
     * @param type
     * @param uris
     */
    private IotaNeighborsRequest(IotaAPICommand type, final String... uris) {
        super(type);
        this.uris = uris;
    }

    /**
     * Initializes a new instance of the IotaNeighborsRequest class.
     * 
     * @param uris
     * @return the instance
     */
    public static IotaNeighborsRequest createAddNeighborsRequest(String... uris) {
        return new IotaNeighborsRequest(IotaAPICommand.ADD_NEIGHBORS, uris);
    }

    /**
     * Initializes a new instance of the IotaNeighborsRequest class.
     * 
     * @param uris
     * @return the instance
     */
    public static IotaNeighborsRequest createRemoveNeighborsRequest(String... uris) {
        return new IotaNeighborsRequest(IotaAPICommand.REMOVE_NEIGHBORS, uris);
    }

    /**
     * Gets the uris.
     *
     * @return The uris.
     */
    public String[] getUris() {
        return uris;
    }

    /**
     * Sets the uris.
     *
     * @param uris The uris.
     */
    public void setUris(String[] uris) {
        this.uris = uris;
    }
}

