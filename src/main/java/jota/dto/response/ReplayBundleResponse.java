package jota.dto.response;

/**
 * Response of api request 'replayBundle'
 **/
public class ReplayBundleResponse extends AbstractResponse {

    private Boolean[] successfully;

    /**
     * Initializes a new instance of the ReplayBundleResponse class.
     */
    public static ReplayBundleResponse create(Boolean[] successfully, long duration) {
        ReplayBundleResponse res = new ReplayBundleResponse();
        res.successfully = successfully;
        res.setDuration(duration);
        return res;
    }

    /**
     * Gets the successfully.
     *
     * @return The successfully.
     */
    public Boolean[] getSuccessfully() {
        return successfully;
    }

    /**
     * Sets the successfully.
     *
     * @param successfully The successfully.
     */
    public void setSuccessfully(Boolean[] successfully) {
        this.successfully = successfully;
    }

}
