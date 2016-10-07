package jota.dto.request;

import jota.IotaAPICommands;

public class IotaAnalyzeTransactionRequest extends IotaCommandRequest {

    private String [] trytes;

    private IotaAnalyzeTransactionRequest(final String ... trytes) {
        super(IotaAPICommands.ANALYZE_TRANSACTIONS);
        this.trytes = trytes;
    }

    public static IotaAnalyzeTransactionRequest createIotaAnalyzeTransactionRequest(String ... trytes) {
        return new IotaAnalyzeTransactionRequest(trytes);
    }
}
