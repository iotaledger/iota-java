package jota.dto.response;

/**
 * Created by pinpong on 01.01.17.
 */
public class ReplayBundleResponse extends AbstractResponse {

    private Boolean successfully;

    public static ReplayBundleResponse create(Boolean successfully) {
        ReplayBundleResponse res = new ReplayBundleResponse();
        res.successfully = successfully;
        return res;
    }

    public Boolean getSuccessfully() {
        return successfully;
    }

    public void setSuccessfully(Boolean successfully) {
        this.successfully = successfully;
    }

}
