package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core API request 'getTrytes'.
 **/
public class IotaGetTrytesRequest extends IotaCommandRequest {

    private String[] hashes;

    /**
     * Initializes a new instance of the IotaGetTrytesRequest class.
     */
    private IotaGetTrytesRequest(final String... hashes) {
        super(IotaAPICommands.GET_TRYTES);
        this.hashes = hashes;
    }

    /**
     * Create a new instance of the IotaGetTrytesRequest class.
     */
    public static IotaGetTrytesRequest createGetTrytesRequest(String... hashes) {
        return new IotaGetTrytesRequest(hashes);
    }

    /**
     * Gets the hashes.
     *
     * @return The hashes.
     */
    public String[] getHashes() {
        return hashes;
    }

    /**
     * Sets the hashes.
     *
     * @param hashes The hashes.
     */
    public void setHashes(String[] hashes) {
        this.hashes = hashes;
    }
}
