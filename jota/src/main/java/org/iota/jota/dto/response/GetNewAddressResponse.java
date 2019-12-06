package org.iota.jota.dto.response;

import java.util.List;

/**
 * Response of api request 'getNewAddress'.
 **/
public class GetNewAddressResponse extends AbstractResponse {

    /**
     * containing an array of strings with the specified number of addresses
     */
    private List<String> addresses;

    /**
     * Initializes a new instance of the GetNewAddressResponse class.
     */
    public static GetNewAddressResponse create(List<String> addresses, long duration) {
        GetNewAddressResponse res = new GetNewAddressResponse();
        res.addresses = addresses;
        res.setDuration(duration);
        return res;
    }

    /**
     * Gets the addresses.
     *
     * @return The addresses.
     */
    public List<String> getAddresses() {
        return addresses;
    }
    
    /**
     * Gets the first address, for quick access, or <code>null</code> if there is none
     * @return The address
     */
    public String first() {
        return addresses.size() > 0 ? addresses.get(0) : null;
    }
}
