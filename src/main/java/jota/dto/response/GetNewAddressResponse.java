package jota.dto.response;

import java.util.List;

public class GetNewAddressResponse extends AbstractResponse {

    private List<String> addresses;

    public static GetNewAddressResponse create(List<String> addresses, long duration) {
        GetNewAddressResponse res = new GetNewAddressResponse();
        res.addresses = addresses;
        res.setDuration(duration);
        return res;
    }

    public List<String> getAddresses() {
        return addresses;
    }
}
