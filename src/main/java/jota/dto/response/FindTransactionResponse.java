package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaFindTransactionsRequest}
 **/
public class FindTransactionResponse extends AbstractResponse {

    String[] hashes;


    /**
     * Gets the hashes.
     *
     * @return hashes trytes The hashes.
     */
    public String[] getHashes() {
        return hashes;
    }
}
