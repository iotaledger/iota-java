package jota.dto.request;

import jota.IotaAPICommands;

public class IotaStoreTransactionsRequest extends IotaCommandRequest {

    private String[] trytes;

    private IotaStoreTransactionsRequest(final String... trytes) {
        super(IotaAPICommands.STORE_TRANSACTIONS);
        this.trytes = trytes;
    }

    public static IotaStoreTransactionsRequest createStoreTransactionsRequest(final String... trytes) {
        return new IotaStoreTransactionsRequest(trytes);
    }
}
