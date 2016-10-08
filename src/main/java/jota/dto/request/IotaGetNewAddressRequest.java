package jota.dto.request;

import jota.IotaAPICommands;

public class IotaGetNewAddressRequest extends IotaCommandRequest {

    private String seed;
    private Integer securityLevel;

    private IotaGetNewAddressRequest(final String seed, final Integer securityLevel) {
        super(IotaAPICommands.GET_NEW_ADDRESS);
        this.seed = seed;
        this.securityLevel = securityLevel;
    }

    public static IotaGetNewAddressRequest createIotaGetNewAddressRequest(String seed, Integer securityLevel) {
        return new IotaGetNewAddressRequest(seed, securityLevel);
    }
}
