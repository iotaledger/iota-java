package org.iota.jota.dto.response;

import org.iota.jota.dto.request.IotaWereAddressesSpentFromRequest;

/**
 * Response of {@link IotaWereAddressesSpentFromRequest}.
 **/
public class WereAddressesSpentFromResponse extends AbstractResponse {

    private boolean[] states;

    /**
     * Gets the states.
     *
     * @return The states.
     */
    public boolean[] getStates() {
        return states;
    }
}
