package jota.dto.request;

import jota.IotaAPICommands;

public class IotaGetTrytesRequest extends IotaCommandRequest {

    private String [] hashes;

    private IotaGetTrytesRequest(final String ... hashes) {
        super(IotaAPICommands.GET_TRYTES);
        this.hashes = hashes;
    }

    public static IotaGetTrytesRequest createGetTrytesRequest(String ... hashes) {
        return new IotaGetTrytesRequest(hashes);
    }
}
