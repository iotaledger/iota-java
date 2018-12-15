package org.iota.jota.utils;

import java.util.Properties;

import org.iota.jota.config.IotaConfig;
import org.iota.jota.config.IotaDefaultConfig;
import org.iota.jota.config.IotaEnvConfig;
import org.iota.jota.config.IotaFileConfig;
import org.iota.jota.config.IotaPropertiesConfig;
import org.iota.jota.store.PropertiesStore;

import org.slf4j.Logger;

/**
 * Naming chosen because of preventing confusion in the code
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBuilder<T, E> {
    
    private Logger log;
    private IotaConfig config;
    
    public AbstractBuilder(Logger log) {
        this.log = log;
    }

    public E build() {
        try {
            generate();
        } catch (Exception e) {
            //You must know that the message comes from creating/building here, so we just log the error
            log.error(e.getMessage());
            
            return null;
        }
        return compile();
    }

    protected abstract E compile();

    protected abstract T generate() throws Exception;
    
    public T config(Properties properties) {
        try {
            setConfig(new IotaPropertiesConfig(new PropertiesStore(properties)));
        } catch (Exception e) {
            // Huh? This can't happen since properties should already be loaded
            log.error(e.getMessage());
        }
        return (T) this;
    }

    public T config(IotaConfig properties) {
        setConfig(properties);
        return (T) this;
    }
    
    protected IotaConfig[] getConfigs() throws Exception{
        IotaEnvConfig env = new IotaEnvConfig();
        
        if (getConfig() == null) {
            String configName = env.getConfigName();
            
            if (configName != null) {
                setConfig(new IotaFileConfig(configName));
            } else {
                setConfig(new IotaFileConfig());
            }
        }
        
        return new IotaConfig[] {
                getConfig(),
                env,
                new IotaDefaultConfig(),
        };
    }

    public IotaConfig getConfig() {
        return config;
    }

    public void setConfig(IotaConfig config) {
        this.config = config;
    }
    
    
}
