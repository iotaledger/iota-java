package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaGetTrytesRequest}
 **/
public class GetTrytesResponse extends AbstractResponse {

    private String[] trytes;

    /**
     * Gets the trytes.
     *
     * @return trytes The trytes.
     */
    public String[] getTrytes() {
        return trytes;
    }
}
