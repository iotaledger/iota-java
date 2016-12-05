package jota.dto.response;

import java.util.List;

public class GetNewAddressResponse extends AbstractResponse {

    private List<String> addresses;

    public static GetNewAddressResponse create(List<String> addresses) {
        GetNewAddressResponse res = new GetNewAddressResponse();
        res.addresses = addresses;
        return res;
    }

    public List<String> getAddress() {
        return addresses;
    }
}
