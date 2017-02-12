package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core api request 'getBalances'
 **/
public class IotaGetBalancesRequest extends IotaCommandRequest {

    private String[] addresses;
    private Integer threshold;

    private IotaGetBalancesRequest(final Integer threshold, final String... addresses) {
        super(IotaAPICommands.GET_BALANCES);
        this.addresses = addresses;
        this.threshold = threshold;
    }

    public static IotaGetBalancesRequest createIotaGetBalancesRequest(final Integer threshold, final String... addresses) {
        return new IotaGetBalancesRequest(threshold, addresses);
    }

    public String[] getAddresses() {
        return addresses;
    }

    public void setAddresses(String[] addresses) {
        this.addresses = addresses;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
}

