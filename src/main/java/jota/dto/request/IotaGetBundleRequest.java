package jota.dto.request;

import jota.IotaAPICommands;

public class IotaGetBundleRequest extends IotaCommandRequest {

    private String transaction;

    private IotaGetBundleRequest(final String transaction) {
        super(IotaAPICommands.GET_BUNDLE);
        this.transaction = transaction;
    }

    public static IotaGetBundleRequest createIotaGetBundleRequest(String transaction) {
        return new IotaGetBundleRequest(transaction);
    }

}
