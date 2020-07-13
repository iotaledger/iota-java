package org.iota.jota.dto.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Response of {@link org.iota.jota.dto.request.IotaCustomRequest }
 */
public class IotaCustomResponse extends AbstractResponse {
    private Map<String, Object> ixi = new HashMap<>();
    
    /**
     * Returns the IXI data in a map. 
     * This is not a clone, so changes reflect the response field.
     * 
     * @return A map of IXI response data
     */
    public Map<String, Object> getIxi() {
        return ixi;
    }
    
    /**
     * Checks if the result contains this parameter
     * 
     * @param name Name to check for
     * @return <code>true</code> if it does, otherwise <code>false</code>
     */
    public boolean hasArg(String name) {
        return ixi.containsKey(name);
    }
    
    /**
     * Gets an argument from the IXI response
     * 
     * @param name The name of the argument
     * @return The Object assigned to that name, or <code>null</code> if {@link #hasArg(String)} returns <code>false</code>
     */
    public Object getArg(String name) {
        if (!hasArg(name)) {
            return null;
        }
        
        return ixi.get(name);
    }
}
