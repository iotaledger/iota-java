package jota.dto.response;

import java.util.HashMap;
import java.util.Map;

public class IotaCustomResponse extends AbstractResponse {
    private Map<String, Object> ixi = new HashMap<>();
    
    public Map<String, Object> getIxi() {
        return ixi;
    }
    
    public boolean hasArg(String name) {
        return ixi.containsKey(name);
    }
    
    public Object getArg(String name) {
        if (!hasArg(name)) return null;
        
        return ixi.get(name);
    }
}
