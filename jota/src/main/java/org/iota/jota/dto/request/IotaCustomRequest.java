package org.iota.jota.dto.request;

public class IotaCustomRequest extends IotaCommandRequest {

    private String[] args;

    protected IotaCustomRequest(String command) {
        super(command);
    }

    public static IotaCustomRequest createCustomRequest(String module, String[] args) {
        IotaCustomRequest customRequest = new IotaCustomRequest(module);
        customRequest.args = args;
        
        return customRequest;
    }
    
    public String[] getArgs() {
        return args;
    }

}
