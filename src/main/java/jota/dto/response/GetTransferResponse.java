package jota.dto.response;

import jota.model.Bundle;

/**
 * Response of api request 'getTransfer'
 **/
public class GetTransferResponse extends AbstractResponse {

    private Bundle[] transferBundle;

    public static GetTransferResponse create(Bundle[] transferBundle, long duration) {
        GetTransferResponse res = new GetTransferResponse();
        res.transferBundle = transferBundle;
        return res;
    }

    public Bundle[] getTransfers() {
        return transferBundle;
    }
}
