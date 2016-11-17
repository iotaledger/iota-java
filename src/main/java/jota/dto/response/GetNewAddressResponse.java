package jota.dto.response;

public class GetNewAddressResponse extends AbstractResponse {

    private String address;

    public String getAddress() {
        return address;
    }

    public static GetNewAddressResponse create(String address) {
        GetNewAddressResponse res = new GetNewAddressResponse();
        res.address = address;
        return res;
    }
}
