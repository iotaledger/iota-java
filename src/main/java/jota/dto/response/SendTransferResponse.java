package jota.dto.response;

/**
 * Created by pinpong on 28.12.16.
 */
public class SendTransferResponse {

    private Boolean successfully;

    public static SendTransferResponse create(Boolean successfully) {
        SendTransferResponse res = new SendTransferResponse();
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
