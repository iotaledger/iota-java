package org.iota.jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaCheckConsistencyRequest}.
 **/
public class CheckConsistencyResponse extends AbstractResponse {

    private boolean state;
    private String info;

    /**
     * Gets the state.
     *
     * @return The state.
     */
    public boolean getState() {
        return state;
    }

    /**
     * If state is false, this provides information on the cause of the inconsistency.
     * @return the information of the state of the tail transactions
     */
    public String getInfo() {
        return info;
    }
}
