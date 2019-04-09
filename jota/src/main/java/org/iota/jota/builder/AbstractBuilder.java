package org.iota.jota.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.iota.jota.config.Config;
import org.iota.jota.config.types.EnvConfig;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.config.types.PropertiesConfig;
import org.iota.jota.store.PropertiesStore;
import org.slf4j.Logger;

/**
 * Naming chosen because of preventing confusion in the code
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBuilder<T, E, F extends Config> {
    
    private Logger log;
    private Config config;
    
    public AbstractBuilder(Logger log) {
        this.log = log;
    }

    public E build() {
        try {
            generate();
        } catch (Exception e) {
            //You must know that the message comes from creating/building here, so we just log the error
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return compile();
    }

    protected abstract E compile();

    protected abstract T generate() throws Exception;
    
    public T config(F config) {
        try {
            setConfig(config);
        } catch (Exception e) {
            // Huh? This can't happen since properties should already be loaded
            log.error(e.getMessage());
        }
        return (T) this;
    }

    public T config(Properties properties) {
        try {
            setConfig(new PropertiesConfig(new PropertiesStore(properties)));
        } catch (Exception e) {
            // Huh? This can't happen since properties should already be loaded
            log.error(e.getMessage());
        }
        return (T) this;
    }

    public T config(PropertiesConfig properties) {
        setConfig(properties);
        return (T) this;
    }
    
    protected List<F> getConfigs() throws Exception{
        EnvConfig env = new EnvConfig();
        ArrayList<F> array = new ArrayList<>();
        array.add((F)env);
        if (config != null) {
            array.add((F) config);
        }
        array.add((F) new IotaDefaultConfig());
        
        return array;
    }

    public Config getConfig() {
        return config;
    }

    private void setConfig(Config config) {
        this.config = config;
    }
}
