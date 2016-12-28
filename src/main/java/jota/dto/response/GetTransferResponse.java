package jota.dto.response;

import jota.model.Bundle;
import jota.model.Transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pinpong on 28.12.16.
 */
public class GetTransferResponse {

    private List<Transfer> transfers = new ArrayList<>();

    public static GetTransferResponse create(Bundle[] transfers) {
        GetTransferResponse res = new GetTransferResponse();
        res.transfers = transfers;
        return res;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }
}
