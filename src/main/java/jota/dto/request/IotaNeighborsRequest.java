package jota.dto.request;

import jota.IotaAPICommands;

public class IotaNeighborsRequest extends IotaCommandRequest {

    private String[] uris;

    private IotaNeighborsRequest(IotaAPICommands type, final String... uris) {
        super(type);
        this.uris = uris;
    }

    public static IotaNeighborsRequest createAddNeighborsRequest(String... uris) {
        return new IotaNeighborsRequest(IotaAPICommands.ADD_NEIGHBORS, uris);
    }

    public static IotaNeighborsRequest createRemoveNeighborsRequest(String... uris) {
        return new IotaNeighborsRequest(IotaAPICommands.REMOVE_NEIGHBORS, uris);
    }
}

