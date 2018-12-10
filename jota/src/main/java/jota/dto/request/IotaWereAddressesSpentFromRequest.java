package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core API request 'wereAddressesSpentFrom'.
 *
 * Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.
 **/
public class IotaWereAddressesSpentFromRequest extends IotaCommandRequest {

    private String[] addresses;

    /**
     * Initializes a new instance of the IotaWereAddressesSpentFromRequest class.
     */
    private IotaWereAddressesSpentFromRequest(String... addresses) {
        super(IotaAPICommands.WERE_ADDRESSES_SPENT_FROM);
        this.addresses = addresses;
    }

    /**
     * Create a new instance of the IotaWereAddressesSpentFromRequest class.
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
