package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaAttachToTangleRequest}.
 **/
public class GetAttachToTangleResponse extends AbstractResponse {

    private String[] trytes;

    /**
     * Initializes a new instance of the GetAttachToTangleResponse class.
     */
    public GetAttachToTangleResponse(long duration) {
        setDuration(duration);
    }

    /**
     * Gets the rytes.
     *
     * @return The trytes.
     */
    public String[] getTrytes() {
        return trytes;
    }
}
