package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaFindTransactionsRequest}
 **/
public class FindTransactionResponse extends AbstractResponse {

    String[] hashes;

    public String[] getHashes() {
        return hashes;
    }
}
