package jota.dto.request;

import jota.IotaAPICommands;

public class IotaGetBalancesRequest extends IotaCommandRequest {

    private String [] addresses;
    private Integer threshold;

    private IotaGetBalancesRequest(final Integer threshold, final String ... addresses) {
        super(IotaAPICommands.GET_BALANCES);
        this.addresses = addresses;
        this.threshold = threshold;
    }

    public static IotaGetBalancesRequest createIotaGetBalancesRequest(final Integer threshold, final String ... addresses) {
        return new IotaGetBalancesRequest(threshold, addresses);
    }

}

