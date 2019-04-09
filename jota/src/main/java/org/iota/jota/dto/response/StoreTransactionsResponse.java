package org.iota.jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaStoreTransactionsRequest}.
 **/
public class StoreTransactionsResponse extends AbstractResponse {

    /**
     * Initializes a new instance of the StoreTransactionsResponse class.
     */
    public StoreTransactionsResponse(long duration) {
        setDuration(duration);
    }
}

