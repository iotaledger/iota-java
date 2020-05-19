package org.iota.jota.dto.request;

import org.iota.jota.IotaAPICommand;

/**
 * This class represents the core api request 'getBalances'.
 **/
public class IotaGetBalancesRequest extends IotaCommandRequest {

    private String[] addresses;
    private String[] tips;

    /**
     * Initializes a new instance of the IotaGetBalancesRequest class.
     *
     * @param addresses Address for which to get the balance (do not include the checksum)
     * @param tips      The optional tips to find the balance through.
     */
    private IotaGetBalancesRequest(final String[] addresses, final String... tips) {
        super(IotaAPICommand.GET_BALANCES);
        this.addresses = addresses;
        this.tips = tips;
    }

    /**
     * Initializes a new instance of the IotaGetBalancesRequest class.
     *
     * @param threshold The confirmation threshold between 0 and 100(inclusive).
     *                  Should be set to 100 for getting balance by counting only confirmed transactions.
     * @param addresses Address for which to get the balance (do not include the checksum)
     * @param tips      The optional tips to find the balance through.
     * @return The instance
     * @deprecated The threshold parameter is removed from the getBalances endpoint on IRI nodes.
     * Alternative use {@link IotaGetBalancesRequest#createIotaGetBalancesRequest(String[], String...)}
     */
    @Deprecated
    public static IotaGetBalancesRequest createIotaGetBalancesRequest(final Integer threshold, final String[] addresses, final String... tips) {
        return new IotaGetBalancesRequest(addresses, tips);
    }

    /**
     * Initializes a new instance of the IotaGetBalancesRequest class.
     *
     * @param addresses Address for which to get the balance (do not include the checksum)
     * @param tips      The optional tips to find the balance through.
     * @return The instance
     */
    public static IotaGetBalancesRequest createIotaGetBalancesRequest(final String[] addresses, final String... tips) {
        return new IotaGetBalancesRequest(addresses, tips);
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
     * Sets the tips to serve as reference for the balance
     *
     * @param tips The starting points we walk back from to find the balance of the addresses
     */
    public void setTips(final String... tips) {
        this.tips = tips;
    }

    /**
     * Gets the tips
     *
     * @return the tips
     */
    public String[] getTips() {
        return tips;
    }
}

