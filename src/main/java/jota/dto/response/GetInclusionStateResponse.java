package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaGetInclusionStateRequest}
 **/
public class GetInclusionStateResponse extends AbstractResponse {

    boolean[] states;

    /**
     * Gets the states.
     *
     * @return states The states.
     */
    public boolean[] getStates() {
        return states;
    }
}
