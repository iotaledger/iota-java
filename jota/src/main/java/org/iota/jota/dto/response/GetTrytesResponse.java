package org.iota.jota.dto.response;

/**
 * Response of {@link org.iota.jota.dto.request.IotaGetTrytesRequest}.
 */
public class GetTrytesResponse extends AbstractResponse {

    private String[] trytes;

    /**
     * Gets the trytes.
     *
     * @return The trytes.
     */
    public String[] getTrytes() {
        return trytes;
    }
}
