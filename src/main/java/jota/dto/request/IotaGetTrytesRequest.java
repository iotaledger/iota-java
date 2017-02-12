package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core API request 'getTrytes'
 **/
public class IotaGetTrytesRequest extends IotaCommandRequest {

    private String[] hashes;

    private IotaGetTrytesRequest(final String... hashes) {
        super(IotaAPICommands.GET_TRYTES);
        this.hashes = hashes;
    }

    public static IotaGetTrytesRequest createGetTrytesRequest(String... hashes) {
        return new IotaGetTrytesRequest(hashes);
    }

    public String[] getHashes() {
        return hashes;
    }

    public void setHashes(String[] hashes) {
        this.hashes = hashes;
    }
}
