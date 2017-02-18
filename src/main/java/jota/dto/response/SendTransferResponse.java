package jota.dto.response;

/**
 * Response of api request 'sendTransfer'
 **/
public class SendTransferResponse extends AbstractResponse {

    private Boolean[] successfully;

    /**
     * Initializes a new instance of the SendTransferResponse class.
     */
    public static SendTransferResponse create(Boolean[] successfully, long duration) {
        SendTransferResponse res = new SendTransferResponse();
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
