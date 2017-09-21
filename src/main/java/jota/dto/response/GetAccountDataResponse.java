package jota.dto.response;

import jota.model.Bundle;

import java.util.List;

/**
 * Response of api request 'getAccountData'.
 **/
public class GetAccountDataResponse extends AbstractResponse {

    private List<String> addresses;
    private Bundle[] transferBundle;
    private long balance;


    /**
     * Initializes a new instance of the GetAccountDataResponse class.
     */
    public static GetAccountDataResponse create(List<String> addresses, Bundle[] transferBundle, long balance, long duration) {
        GetAccountDataResponse res = new GetAccountDataResponse();
        res.addresses = addresses;
        res.transferBundle = transferBundle;
        res.balance = balance;
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
     * Gets the transfers.
     *
     * @return The transfers.
     */
    public Bundle[] getTransfers() {
        return transferBundle;
    }

    /**
     * Gets the balance.
     *
     * @return The balance.
     */
    public long getBalance() {
        return balance;
    }
}