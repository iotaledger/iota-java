package jota.dto.request;

import jota.IotaAPICommands;

public class IotaGetMilestoneRequest extends IotaCommandRequest {

    private String index;

    private IotaGetMilestoneRequest(final String index) {
        super(IotaAPICommands.GET_MILESTONE);
        this.index = index;
    }

    public static IotaGetMilestoneRequest createMilestoneRequest(Integer index) {
        return new IotaGetMilestoneRequest(String.valueOf(index));
    }
}
