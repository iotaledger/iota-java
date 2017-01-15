package jota.dto.request;

import jota.IotaAPICommands;

public class IotaBroadcastTransactionRequest extends IotaCommandRequest {

    private String[] trytes;

    private IotaBroadcastTransactionRequest(final String... trytes) {
        super(IotaAPICommands.BROADCAST_TRANSACTIONS);
        this.trytes = trytes;
    }

    public static IotaBroadcastTransactionRequest createBroadcastTransactionsRequest(final String... trytes) {
        return new IotaBroadcastTransactionRequest(trytes);
    }

    public String[] getTrytes() {
        return trytes;
    }

    public void setTrytes(String[] trytes) {
        this.trytes = trytes;
    }
}