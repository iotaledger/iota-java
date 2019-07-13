package org.iota.jota;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.iota.jota.builder.ApiBuilderSettings;
import org.iota.jota.config.options.ApiConfig;
import org.iota.jota.connection.Connection;
import org.iota.jota.pow.ICurl;

/**
 * 
 * The current instance of API configuration options based on env, file and defaults
 * Modifications can be made but will not persist throughout restarts
 *
 */
public class ApiOptions implements ApiConfig, ApiBuilderSettings {
    
    private int legacyPort;
    private String legacyProtocol;
    private String legacyHost;

    private ICurl customCurl;
    private IotaPoW localPoW;
    
    //Nodes are not active
    private List<Connection> nodes;
    private int timeout;
    
    public ApiOptions(IotaAPI.Builder builder) {
        localPoW = builder.getLocalPoW();
        customCurl = builder.getCustomCurl();
        legacyProtocol = builder.getProtocol();
        legacyHost = builder.getHost();
        legacyPort =  builder.getPort();
        nodes = builder.getNodes();
    }
    

    public List<Connection> getNodes() {
        return nodes;
    }
    
    @Override
    public boolean hasNodes() {
        return this.nodes != null && this.nodes.size() > 0;
    }


    public void setNodes(List<Connection> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int getLegacyPort() {
        return legacyPort;
    }


    public void setLegacyPort(int legacyPort) {
        this.legacyPort = legacyPort;
    }

    @Override
    public String getLegacyProtocol() {
        return legacyProtocol;
    }


    public void setLegacyProtocol(String legacyProtocol) {
        this.legacyProtocol = legacyProtocol;
    }

    @Override
    public String getLegacyHost() {
        return legacyHost;
    }


    public void setLegacyHost(String legacyHost) {
        this.legacyHost = legacyHost;
    }
    
    @Override
    public ICurl getCustomCurl() {
        return customCurl;
    }


    public void setCustomCurl(ICurl customCurl) {
        this.customCurl = customCurl;
    }

    @Override
    public IotaPoW getLocalPoW() {
        return localPoW;
    }


    public void setLocalPoW(IotaPoW localPoW) {
        this.localPoW = localPoW;
    }
    
    @Override
    public int getConnectionTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
