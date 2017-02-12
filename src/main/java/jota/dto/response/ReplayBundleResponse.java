package jota.dto.response;

/**
 * Response of api request 'replayBundle'
 **/
public class ReplayBundleResponse extends AbstractResponse {

    private Boolean[] successfully;

    public static ReplayBundleResponse create(Boolean[] successfully, long duration) {
        ReplayBundleResponse res = new ReplayBundleResponse();
        res.successfully = successfully;
        res.setDuration(duration);
        return res;
    }

    public Boolean[] getSuccessfully() {
        return successfully;
    }

    public void setSuccessfully(Boolean[] successfully) {
        this.successfully = successfully;
    }

}
