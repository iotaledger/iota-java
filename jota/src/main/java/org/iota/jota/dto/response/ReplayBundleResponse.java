package org.iota.jota.dto.response;

import org.iota.jota.model.Bundle;

/**
 * Response of api request 'replayBundle'.
 **/
public class ReplayBundleResponse extends AbstractResponse {

    private Boolean[] successfully;
    private Bundle newBundle;

    /**
     * Initializes a new instance of the ReplayBundleResponse class.
     * @param newBundle 
     */
    public static ReplayBundleResponse create(Bundle newBundle, Boolean[] successfully, long duration) {
        ReplayBundleResponse res = new ReplayBundleResponse();
        res.successfully = successfully;
        res.setDuration(duration);
        res.newBundle = newBundle;
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

    public Bundle getNewBundle() {
        return newBundle;
    }
}
