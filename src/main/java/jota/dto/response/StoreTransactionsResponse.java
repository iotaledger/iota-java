package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaStoreTransactionsRequest}
 **/
public class StoreTransactionsResponse extends AbstractResponse {
    public StoreTransactionsResponse(long duration) {
        setDuration(duration);
    }
}

