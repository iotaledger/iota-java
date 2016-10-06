package jota.dto.request;

import jota.IotaAPICommands;

public class IotaGetTransferRequest extends IotaCommandRequest {

    private String seed;
    private String securityLevel;

    private IotaGetTransferRequest(final String seed, final String securityLevel) {
        super(IotaAPICommands.GET_TRANSFER);
        this.seed = seed;
        this.securityLevel = securityLevel;
    }

    public static IotaGetTransferRequest createGetTransferRequest(String seed, Integer securityLevel) {
        return new IotaGetTransferRequest(seed, String.valueOf(securityLevel));
    }
}
