package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core API request 'addNeighbors' and 'removeNeighbors'.
 **/
public class IotaNeighborsRequest extends IotaCommandRequest {

    private String[] uris;

    /**
     * Initializes a new instance of the IotaNeighborsRequest class.
     */
    private IotaNeighborsRequest(IotaAPICommands type, final String... uris) {
        super(type);
        this.uris = uris;
    }

    /**
     * Create a new instance of the IotaNeighborsRequest class.
     */
    public static IotaNeighborsRequest createAddNeighborsRequest(String... uris) {
        return new IotaNeighborsRequest(IotaAPICommands.ADD_NEIGHBORS, uris);
    }

    /**
     * Create a new instance of the IotaNeighborsRequest class.
     */
    public static IotaNeighborsRequest createRemoveNeighborsRequest(String... uris) {
        return new IotaNeighborsRequest(IotaAPICommands.REMOVE_NEIGHBORS, uris);
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

