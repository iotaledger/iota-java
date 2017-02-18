package jota.dto.response;

import jota.model.Bundle;

/**
 * Response of api request 'getTransfer'
 **/
public class GetTransferResponse extends AbstractResponse {

    private Bundle[] transferBundle;

    /**
     * Initializes a new instance of the GetTransferResponse class.
     */
    public static GetTransferResponse create(Bundle[] transferBundle, long duration) {
        GetTransferResponse res = new GetTransferResponse();
        res.transferBundle = transferBundle;
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
