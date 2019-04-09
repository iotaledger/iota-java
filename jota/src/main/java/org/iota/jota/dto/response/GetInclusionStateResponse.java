package org.iota.jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaGetInclusionStateRequest}.
 **/
public class GetInclusionStateResponse extends AbstractResponse {

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
