package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core api request 'getBalances'.
 **/
public class IotaGetBalancesRequest extends IotaCommandRequest {

    private String[] addresses;
    private Integer threshold;

    /**
     * Initializes a new instance of the IotaGetBalancesRequest class.
     */
    private IotaGetBalancesRequest(final Integer threshold, final String... addresses) {
        super(IotaAPICommands.GET_BALANCES);
        this.addresses = addresses;
        this.threshold = threshold;
    }

    /**
     * Create a new instance of the IotaGetBalancesRequest class.
     */
    public static IotaGetBalancesRequest createIotaGetBalancesRequest(final Integer threshold, final String... addresses) {
        return new IotaGetBalancesRequest(threshold, addresses);
    }

    /**
     * Gets the addresses.
     *
     * @return The addresses.
     */
    public String[] getAddresses() {
        return addresses;
    }

    /**
     * Sets the addresses.
     *
     * @param addresses The addresses.
     */
    public void setAddresses(String[] addresses) {
        this.addresses = addresses;
    }

    /**
     * Gets the threshold.
     *
     * @return The threshold.
     */
    public Integer getThreshold() {
        return threshold;
    }

    /**
     * Sets the threshold.
     *
     * @param threshold The threshold.
     */
    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
}

