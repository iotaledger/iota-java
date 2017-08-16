package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core API request 'getTransactionsToApprove'.
 **/
public class IotaGetTransactionsToApproveRequest extends IotaCommandRequest {

    private Integer depth;

    /**
     * Initializes a new instance of the IotaGetTransactionsToApproveRequest class.
     */
    private IotaGetTransactionsToApproveRequest(final Integer depth) {
        super(IotaAPICommands.GET_TRANSACTIONS_TO_APPROVE);
        this.depth = depth;
    }

    /**
     * Create a new instance of the IotaGetTransactionsToApproveRequest class.
     */
    public static IotaGetTransactionsToApproveRequest createIotaGetTransactionsToApproveRequest(Integer depth) {
        return new IotaGetTransactionsToApproveRequest(depth);
    }

    /**
     * Gets the depth.
     *
     * @return The depth.
     */
    public Integer getDepth() {
        return depth;
    }

    /**
     * Sets the depth.
     *
     * @param depth The depth.
     */
    public void setDepth(Integer depth) {
        this.depth = depth;
    }
}
