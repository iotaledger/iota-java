package jota.dto.response;

public class GetAttachToTangleResponse extends AbstractResponse {

    private String[] trytes;

    public String[] getTrytes() {
        return trytes;
    }

    public GetAttachToTangleResponse(long duration) {
        setDuration(duration);
    }
}
