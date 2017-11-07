package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core api request 'getNodeInfo', 'getNeighbors' and 'interruptAttachToTangle'.
 **/
public class IotaCommandRequest {

    final String command;

    /**
     * Initializes a new instance of the IotaCommandRequest class.
     */
    protected IotaCommandRequest(IotaAPICommands command) {
        this.command = command.command();
    }

    /**
     * Get information about the node.
     *
     * @return The Node info.
     */
    public static IotaCommandRequest createNodeInfoRequest() {
        return new IotaCommandRequest(IotaAPICommands.GET_NODE_INFO);
    }

    /**
     * Gets the tips of the node.
     *
     * @return The tips of the node.
     */
    public static IotaCommandRequest createGetTipsRequest() {
        return new IotaCommandRequest(IotaAPICommands.GET_TIPS);
    }

    /**
     * Gets the neighbours of the node.
     *
     * @return The list of neighbors.
     */
    public static IotaCommandRequest createGetNeighborsRequest() {
        return new IotaCommandRequest(IotaAPICommands.GET_NEIGHBORS);
    }

    /**
     * Interrupt attaching to the tangle
     *
     * @return
     */
    public static IotaCommandRequest createInterruptAttachToTangleRequest() {
        return new IotaCommandRequest(IotaAPICommands.INTERRUPT_ATTACHING_TO_TANGLE);
    }
}
