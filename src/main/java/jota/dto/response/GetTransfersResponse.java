package jota.dto.response;

import jota.model.Transfer;

import java.util.ArrayList;
import java.util.List;

public class GetTransfersResponse extends AbstractResponse {

    private List<Transfer> transfers = new ArrayList<>();

    public List<Transfer> getTransfers() {
        return transfers;
    }
}
