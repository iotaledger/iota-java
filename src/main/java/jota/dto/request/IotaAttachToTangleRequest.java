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

    public String getTrunkTransaction() {
        return trunkTransaction;
    }

    public void setTrunkTransaction(String trunkTransaction) {
        this.trunkTransaction = trunkTransaction;
    }

    public String getBranchTransaction() {
        return branchTransaction;
    }

    public void setBranchTransaction(String branchTransaction) {
        this.branchTransaction = branchTransaction;
    }

    public Integer getMinWeightMagnitude() {
        return minWeightMagnitude;
    }

    public void setMinWeightMagnitude(Integer minWeightMagnitude) {
        this.minWeightMagnitude = minWeightMagnitude;
    }

    public String[] getTrytes() {
        return trytes;
    }

    public void setTrytes(String[] trytes) {
        this.trytes = trytes;
    }
}
