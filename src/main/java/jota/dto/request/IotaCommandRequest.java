package jota.dto.request;

import jota.IotaAPICommands;

public class IotaCommandRequest {

    final String command;

    protected IotaCommandRequest(IotaAPICommands command) {
        this.command = command.command();
    }

    public static IotaCommandRequest createNodeInfoRequest() {
        return new IotaCommandRequest(IotaAPICommands.GET_NODE_INFO);
    }

    public static IotaCommandRequest createGetTipsRequest() {
        return new IotaCommandRequest(IotaAPICommands.GET_TIPS);
    }

    public static IotaCommandRequest createGetNeighborsRequest() {
        return new IotaCommandRequest(IotaAPICommands.GET_NEIGHBORS);
    }

    public static IotaCommandRequest createInterruptAttachToTangleRequest() {
        return new IotaCommandRequest(IotaAPICommands.INTERRUPT_ATTACHING_TO_TANGLE);
    }
}
