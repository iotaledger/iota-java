package org.iota.jota.builder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.iota.jota.IotaAPICore;
import org.iota.jota.IotaLocalPoW;
import org.iota.jota.config.options.ApiConfig;
import org.iota.jota.config.options.ApiSettings;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.connection.Connection;
import org.iota.jota.connection.HttpConnector;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.SpongeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public abstract class ApiBuilder<T extends ApiBuilder<T, E>, E extends IotaAPICore> 
        extends AbstractBuilder<T, E, ApiConfig> 
        implements ApiSettings {
    
    private static final Logger log = LoggerFactory.getLogger(ApiBuilder.class);
    
    List<Connection> nodes = new ArrayList<>();
    
    String protocol, host;
    int port;
    
    int timeout = 0;
    
    // If this is null, no local PoW is done, therefore no default value
    IotaLocalPoW localPoW;
    ICurl customCurl = SpongeFactory.create(SpongeFactory.Mode.KERL);
    
    public ApiBuilder() {
        super(log);
    }
    
    /**
     * Generates values for options which were not assigned through the builder.
     * Starts by checking a optionally set config using {@link #config(org.iota.jota.config.Config)}
     * THen checks Environment, and in the end goes to {@link IotaDefaultConfig} for defaults
     * 
     * @return The builder
     * @throws Exception When we failed to load env configs or a url was malformed
     */
    @SuppressWarnings("deprecation")
    protected T generate() throws Exception {
        for (ApiConfig config : getConfigs()) {
            if (config != null) {
             // Defaults have the node in the nodes list
                // Only load legacy defaults when we used a legacy value
                if (!(config instanceof IotaDefaultConfig) || hasLegacyOptions()) {
                    if (null == protocol) {
                        protocol = config.getLegacyProtocol();
                    }
    
                    if (null == host) {
                        host = config.getLegacyHost();
                    }
    
                    if (0 == port) {
                        port = config.getLegacyPort();
                    } 
                }

                if (0 == timeout) {
                    timeout = config.getConnectionTimeout();
                }
                
                // Now if we had a legacy config node, we wont take the default node
                // BUt if nothing was configured, we add the legacy node from default config
                if (config.hasNodes() && (!(config instanceof IotaDefaultConfig) || !hasLegacyOptions())) {
                    for (Connection c : config.getNodes()) {
                        nodes.add(c);
                    }
                }
            }
        }
        
        if (hasLegacyOptions()) {
            nodes.add(new HttpConnector(protocol, host, port, timeout));
        }
        
        return (T) this;
    }
    
    private boolean hasLegacyOptions() {
        return null != host || null != protocol || 0 != port;
    }

    public T withCustomCurl(ICurl curl) {
        customCurl = curl;
        return (T) this;
    }

    public T host(String host) {
        try {
            // Throws exception if invalid
            InetAddress.getByName(host);
            this.host = host;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        return (T) this;
    }

    public T port(int port) {
        this.port = port;
        return (T) this;
    }

    public T protocol(String protocol) {
        this.protocol = protocol;
        return (T) this;
    }

    public T localPoW(IotaLocalPoW localPoW) {
        this.localPoW = localPoW;
        return (T) this;
    }
    
    public T timeout(int timeout) {
        this.timeout = timeout;
        return (T) this;
    }
    
    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public IotaLocalPoW getLocalPoW() {
        return localPoW;
    }

    public ICurl getCustomCurl() {
        return customCurl;
    }
    
    public T addNode(Connection c) {
        nodes.add(c);
        return (T) this;
    }

    public List<Connection> getNodes() {
        return nodes;
    }
    
    @Override
    public int getConnectionTimeout() {
        return timeout;
    }

    @Override
    public boolean hasNodes() {
        return nodes != null && nodes.size() > 0;
    }

    @Override
    public int getLegacyPort() {
        return getPort();
    }

    @Override
    public String getLegacyProtocol() {
        return getProtocol();
    }

    @Override
    public String getLegacyHost() {
        return getHost();
    }
}