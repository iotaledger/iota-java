package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaCommandRequest}
 **/
public class GetTipsResponse extends AbstractResponse {

    private String[] hashes;

    public String[] getHashes() {
        return hashes;
    }
}
