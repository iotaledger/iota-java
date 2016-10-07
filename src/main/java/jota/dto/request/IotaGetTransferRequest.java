package jota.dto.request;

import com.google.gson.Gson;
import jota.IotaAPICommands;

public class IotaGetTransferRequest extends IotaCommandRequest {

    private String seed;
    private Integer securityLevel;

    private IotaGetTransferRequest(final String seed, final Integer securityLevel) {
        super(IotaAPICommands.GET_TRANSFER);
        this.seed = seed;
        this.securityLevel = securityLevel;
    }

    public static IotaGetTransferRequest createGetTransferRequest(String seed, Integer securityLevel) {
        return new IotaGetTransferRequest(seed, securityLevel);
    }
}
