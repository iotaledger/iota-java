package org.iota.jota.dto.request;

import org.iota.jota.IotaAPICommand;

/**
 * This class represents the core api request 'getNodeInfo', 'getNeighbors' and 'interruptAttachToTangle'.
 **/
public class IotaCommandRequest {

    final String command;

    /**
     * Initializes a new instance of the IotaCommandRequest class.
     * 
     * @param command
     */
    protected IotaCommandRequest(IotaAPICommand command) {
        this.command = command.command();
    }

    /**
     * Get information about the node.
     *
     * @return The Node info.
     */
    public static IotaCommandRequest createNodeInfoRequest() {
        return new IotaCommandRequest(IotaAPICommand.GET_NODE_INFO);
    }

    /**
     * Gets the tips of the node.
     *
     * @return The tips of the node.
     */
    public static IotaCommandRequest createGetTipsRequest() {
        return new IotaCommandRequest(IotaAPICommand.GET_TIPS);
    }

    /**
     * Gets the neighbours of the node.
     *
     * @return The list of neighbors.
     */
    public static IotaCommandRequest createGetNeighborsRequest() {
        return new IotaCommandRequest(IotaAPICommand.GET_NEIGHBORS);
    }

    /**
     * Interrupt attaching to the tangle
     *
     * @return The interrupted attach command
     */
    public static IotaCommandRequest createInterruptAttachToTangleRequest() {
        return new IotaCommandRequest(IotaAPICommand.INTERRUPT_ATTACHING_TO_TANGLE);
    }
}
