package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core API request 'attachToTangle'.
 *
 * It is used to attach trytes to the tangle.
 **/
public class IotaAttachToTangleRequest extends IotaCommandRequest {

    private String trunkTransaction;
    private String branchTransaction;
    private Integer minWeightMagnitude;
    private String[] trytes;

    /**
     * Initializes a new instance of the IotaAttachedToTangleRequest class.
     */
    private IotaAttachToTangleRequest(final String trunkTransaction, final String branchTransaction, final Integer minWeightMagnitude, final String... trytes) {
        super(IotaAPICommands.ATTACH_TO_TANGLE);
        this.trunkTransaction = trunkTransaction;
        this.branchTransaction = branchTransaction;
        this.minWeightMagnitude = minWeightMagnitude;
        this.trytes = trytes;
    }

    /**
     * Create a new instance of the IotaAttachedToTangleRequest class.
     */
    public static IotaAttachToTangleRequest createAttachToTangleRequest(final String trunkTransaction, final String branchTransaction, final Integer minWeightMagnitude, final String... trytes) {
        return new IotaAttachToTangleRequest(trunkTransaction, branchTransaction, minWeightMagnitude, trytes);
    }

    /**
     * Gets the trunk transaction.
     *
     * @return The trunk transaction.
     */
    public String getTrunkTransaction() {
        return trunkTransaction;
    }

    /**
     * Sets the trunk transaction.
     *
     * @param trunkTransaction The trunk transaction.
     */
    public void setTrunkTransaction(String trunkTransaction) {
        this.trunkTransaction = trunkTransaction;
    }

    /**
     * Gets the branch transaction.
     *
     * @return The branch transaction.
     */
    public String getBranchTransaction() {
        return branchTransaction;
    }

    /**
     * Sets the branch transaction.
     *
     * @param branchTransaction the branch transaction.
     */
    public void setBranchTransaction(String branchTransaction) {
        this.branchTransaction = branchTransaction;
    }

    /**
     * Gets the min weight magnitude.
     *
     * @return The min weight magnitude.
     */
    public Integer getMinWeightMagnitude() {
        return minWeightMagnitude;
    }

    /**
     * Sets the min weight magnitude.
     *
     * @param minWeightMagnitude The min weight magnitude.
     */
    public void setMinWeightMagnitude(Integer minWeightMagnitude) {
        this.minWeightMagnitude = minWeightMagnitude;
    }

    /**
     * Gets the trytes.
     *
     * @return The trytes.
     */
    public String[] getTrytes() {
        return trytes;
    }

    /**
     * Sets the trytes.
     *
     * @param trytes The trytes.
     */
    public void setTrytes(String[] trytes) {
        this.trytes = trytes;
    }
}
