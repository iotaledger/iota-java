package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaAttachToTangleRequest}
 **/
public class GetAttachToTangleResponse extends AbstractResponse {

    private String[] trytes;

    public GetAttachToTangleResponse(long duration) {
        setDuration(duration);
    }

    public String[] getTrytes() {
        return trytes;
    }
}
