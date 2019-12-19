package org.iota.jota.dto.request;

import java.util.Map;

public class IotaCustomRequest extends IotaCommandRequest {

    private Map<String, Object> args;

    protected IotaCustomRequest(String command) {
        super(command);
    }

    public static IotaCustomRequest createCustomRequest(String module, Map<String, Object> params) {
        IotaCustomRequest customRequest = new IotaCustomRequest(module);
        customRequest.args = params;
        
        return customRequest;
    }
    
    public Map<String, Object> getArgs() {
        return args;
    }

}
