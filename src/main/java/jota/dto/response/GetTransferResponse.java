package jota.dto.response;

import jota.model.Bundle;

/**
 * Created by pinpong on 28.12.16.
 */
public class GetTransferResponse  extends AbstractResponse {

    private Bundle[] transferBundle;

    public static GetTransferResponse create(Bundle[] transferBundle) {
        GetTransferResponse res = new GetTransferResponse();
        res.transferBundle = transferBundle;
        return res;
    }

    public Bundle[]  getTransfers() {
        return transferBundle;
    }
}
