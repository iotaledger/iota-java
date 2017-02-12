package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaGetInclusionStateRequest}
 **/
public class GetInclusionStateResponse extends AbstractResponse {

    boolean[] states;

    public boolean[] getStates() {
        return states;
    }
}
