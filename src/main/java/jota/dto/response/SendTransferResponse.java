package jota.dto.response;

/**
 * Created by pinpong on 28.12.16.
 */
public class SendTransferResponse extends AbstractResponse {

    private Boolean[] successfully;

    public static SendTransferResponse create(Boolean[] successfully, long duration) {
        SendTransferResponse res = new SendTransferResponse();
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
