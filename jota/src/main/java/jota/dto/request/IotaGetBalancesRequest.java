package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core api request 'getBalances'.
 **/
public class IotaGetBalancesRequest extends IotaCommandRequest {

    private String[] addresses;
    private Integer threshold;
    private String[] tips;

    /**
     * Initializes a new instance of the IotaGetBalancesRequest class.
     */
    private IotaGetBalancesRequest(final Integer threshold, final String[] addresses, final String... tips) {
        super(IotaAPICommands.GET_BALANCES);
        this.addresses = addresses;
        this.threshold = threshold;
        this.tips = tips;
    }

    /**
     * Create a new instance of the IotaGetBalancesRequest class.
     */
    public static IotaGetBalancesRequest createIotaGetBalancesRequest(final Integer threshold, final String[] addresses, final String... tips) {
        return new IotaGetBalancesRequest(threshold, addresses, tips);
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

    /**
     * Sets the tips to serve as reference for the balance
     *
     * @param tips
     */
    public void setTips(final String... tips) { this.tips = tips;}

    public String[] getTips() { return tips; }
}

