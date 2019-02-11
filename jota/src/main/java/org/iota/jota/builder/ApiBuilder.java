package org.iota.jota.builder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.iota.jota.IotaAPICore;
import org.iota.jota.IotaLocalPoW;
import org.iota.jota.config.ApiConfig;
import org.iota.jota.connection.Connection;
import org.iota.jota.connection.HttpConnector;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.SpongeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//All casts are to T, and are okay unless you do really weird things.
//Warnings are annoying
@SuppressWarnings("unchecked")
public abstract class ApiBuilder<T extends ApiBuilder<T, E>, E extends IotaAPICore> 
        extends AbstractBuilder<T, E, ApiConfig> 
        implements ApiConfig, ApiBuilderSettings {
    
    private static final Logger log = LoggerFactory.getLogger(ApiBuilder.class);
    
    List<Connection> nodes = new ArrayList<>();
    
    String protocol, host;
    int port;
    
    int timeout = 0;
    
    // If this is null, no local PoW is done, therefor no default value
    IotaLocalPoW localPoW;
    ICurl customCurl = SpongeFactory.create(SpongeFactory.Mode.KERL);
    
    public ApiBuilder() {
        super(log);
    }
    
    /**
     * 
     * @return
     * @throws Exception
     */
    protected T generate() throws Exception {
        for (ApiConfig config : getConfigs()) {
            if (config != null) {
                if (null == protocol) {
                    protocol = config.getLegacyProtocol();
                }

                if (null == host) {
                    host = config.getLegacyHost();
                }

                if (0 == port) {
                    port = config.getLegacyPort();
                }
                
                if (0 == timeout) {
                    timeout = config.getConnectionTimeout();
                }
                
                if (config.hasNodes()) {
                    for (Connection c : config.getNodes()) {
                        nodes.add(c);
                    }
                }
            }
        };
        
        if (!hasNodes()) {
            //Either we have a legacy node defined in the builder, or in the config.
            if (null != host && null != protocol && 0 != port) {
                nodes.add(new HttpConnector(protocol, host, port, timeout));
            } else {
              //Fallback on legacy option from config
                for (ApiConfig config : getConfigs()) {
                    if (config.getLegacyHost() != null
                            && config.getLegacyProtocol() != null
                            && config.getLegacyPort() != 0) {
                        nodes.add(new HttpConnector(
                                config.getLegacyProtocol(), 
                                config.getLegacyHost(), 
                                config.getLegacyPort(),
                                timeout)
                        );
                        
                        break; //If we define one in config, dont check rest, Otherwise we end up using custom & default
                    }
                }
            }
        }
        return (T) this;
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