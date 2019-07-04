package org.iota.jota.dto.request;

import org.iota.jota.IotaAPICommand;

/**
 * This class represents the core API request 'getTrytes'.
 **/
public class IotaGetTrytesRequest extends IotaCommandRequest {

    private String[] hashes;

    /**
     * Initializes a new instance of the IotaGetTrytesRequest class.
     * 
     * @param hashes
     */
    private IotaGetTrytesRequest(final String... hashes) {
        super(IotaAPICommand.GET_TRYTES);
        this.hashes = hashes;
    }

    /**
     * Initializes a new instance of the IotaGetTrytesRequest class.
     * 
     * @param hashes
     * @return the instance
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
