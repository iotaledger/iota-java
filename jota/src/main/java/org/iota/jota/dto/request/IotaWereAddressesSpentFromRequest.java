package org.iota.jota.dto.request;

import org.iota.jota.IotaAPICommand;

/**
 * This class represents the core API request 'wereAddressesSpentFrom'.
 *
 * Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.
 **/
public class IotaWereAddressesSpentFromRequest extends IotaCommandRequest {

    private String[] addresses;

    /**
     * Initializes a new instance of the IotaWereAddressesSpentFromRequest class.
     * 
     * @param addresses
     */
    private IotaWereAddressesSpentFromRequest(String... addresses) {
        super(IotaAPICommand.WERE_ADDRESSES_SPENT_FROM);
        this.addresses = addresses;
    }

    /**
     * Initializes a new instance of the IotaWereAddressesSpentFromRequest class.
     * 
     * @param addresses
     * @return the instance
     */
    public static IotaWereAddressesSpentFromRequest create(String... addresses) {
        return new IotaWereAddressesSpentFromRequest(addresses);
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
}
