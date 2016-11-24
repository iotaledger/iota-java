package jota.dto.request;

import jota.IotaAPICommands;

public class IotaAttachToTangleRequest extends IotaCommandRequest {

    private String trunkTransaction;
    private String branchTransaction;
    private Integer minWeightMagnitude;
    private String[] trytes;

    private IotaAttachToTangleRequest(final String trunkTransaction, final String branchTransaction, final Integer minWeightMagnitude, final String... trytes) {
        super(IotaAPICommands.ATTACH_TO_TANGLE);
        this.trunkTransaction = trunkTransaction;
        this.branchTransaction = branchTransaction;
        this.minWeightMagnitude = minWeightMagnitude;
        this.trytes = trytes;
    }

    public static IotaAttachToTangleRequest createAttachToTangleRequest(final String trunkTransaction, final String branchTransaction, final Integer minWeightMagnitude, final String... trytes) {
        return new IotaAttachToTangleRequest(trunkTransaction, branchTransaction, minWeightMagnitude, trytes);
    }
}
