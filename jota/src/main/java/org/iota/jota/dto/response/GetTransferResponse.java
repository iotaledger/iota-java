package org.iota.jota.dto.response;

import org.iota.jota.model.Bundle;

/**
 * Response of api request 'getTransfer'.
 **/
public class GetTransferResponse extends AbstractResponse {

    private Bundle[] transferBundle;

    /**
     * Initializes a new instance of the GetTransferResponse class.
     */
    public static GetTransferResponse create(Bundle[] transferBundle, long duration) {
        GetTransferResponse res = new GetTransferResponse();
        res.transferBundle = transferBundle;
        res.setDuration(duration);
        return res;
    }

    /**
     * Gets the transfer bundle.
     *
     * @return The transfer bundle.
     */
    public Bundle[] getTransfers() {
        return transferBundle;
    }
}
