package org.iota.jota;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import org.iota.jota.config.IotaConfig;
import org.iota.jota.config.IotaFileConfig;
import org.iota.jota.store.IotaStore;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.AbstractBuilder;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;


public class IotaAccount {
    
    private static final Logger log = LoggerFactory.getLogger(IotaAccount.class);
    
    private IotaStore store;

    private IotaConfig config;

    private IotaAPI api;

    private String seed;
    
    /**
     * 
     * @throws Exception If the config did not load for whatever reason
     */
    protected IotaAccount(Builder builder) {
        this.store = builder.store;
        this.api = builder.api;
        this.config = builder.getConfig();
        this.seed = builder.seed;
        
        log.info(this.toString());
    }
    
    /**
     * Constructs a IotaAPI with a config based on environment variables or default values.
     * If no environment variable is defined, will use {@value org.iota.jota.config.IotaFileConfig#DEFAULT_CONFIG_NAME}
     * The default storage will be at {@value jota.config.IotaFileStore#DEFAULT_STORE}
     * @throws Exception If the config did not load for whatever reason
     */
    public IotaAccount(String seed) throws Exception {
        this(new Builder(seed).generate());
    }
    
    /**
     * Constructs a IotaAPI with a config based on environment variables or default values.
     * If no environment variable is defined, will use {@value org.iota.jota.config.IotaFileConfig#DEFAULT_CONFIG_NAME}
     * @param store The method we use for storing key/value data
     * @throws Exception If the config did not load for whatever reason
     */
    public IotaAccount(String seed, IotaStore store) throws Exception {
        this(new Builder(seed).store(store).generate());
    }
    
    /**
     * Constructs a IotaAPI with a config from String
     * @param store The method we use for storing key/value data
     * @param config The location of the config
     * @throws Exception If the config did not load for whatever reason
     */
    public IotaAccount(String seed, IotaStore store, String config) throws Exception {
        this(new Builder(seed).store(store).config(new IotaFileConfig(config)).generate());
    }

    /**
     * Constructs a IotaAPI with config
     * @param store The method we use for storing key/value data
     * @param iotaConfig The config we load nodes from
     * @throws Exception If the config did not load for whatever reason
     */
    public IotaAccount(String seed, IotaStore store, IotaConfig iotaConfig) throws Exception {
        this(new Builder(seed).store(store).config(iotaConfig).generate());
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("----------------------");
        builder.append(System.getProperty("line.separator"));
        builder.append("iota-java accounts configured with the following: ");
        
        builder.append(System.getProperty("line.separator"));
        builder.append("Seed: " + seed.substring(0, 10) + StringUtils.repeat('X', Constants.SEED_LENGTH_MAX - 10));
        
        builder.append(System.getProperty("line.separator"));
        builder.append("Config file: " + config);
        
        builder.append(System.getProperty("line.separator"));
        builder.append("Storage file: " + store);
        
        builder.append(System.getProperty("line.separator"));
        builder.append(api.toString());
        
        return builder.toString();
    }
    
    public static class Builder extends AbstractBuilder<Builder, IotaAccount>{
        
        private static final Logger log = LoggerFactory.getLogger(IotaAccount.class);
        
        private IotaStore store;
        private IotaAPI api;

        private String seed;
        
        public Builder(String seed) throws ArgumentException {
            super(log);
            
            if (!InputValidator.isValidSeed(seed)) {
                throw new ArgumentException(Constants.INVALID_SEED_INPUT_ERROR);
            }
            
            this.seed = seed;
        }
        
        public Builder store(IotaStore store) {
            this.store = store;
            return this;
        }
        
        public Builder api(IotaAPI api) {
            this.api = api;
            return this;
        }

        @Override
        protected Builder generate() throws Exception {
            //If a config is specified through ENV, that one will be in the stream, otherwise default config is used
            Arrays.stream(getConfigs()).forEachOrdered(config -> {
                if (config != null) {
                    //calculate IotaApi specific values
                    
                    if (null == store) {
                        store = config.getStore();
                    }
                    
                    if (null == api) {
                        api = new IotaAPI.Builder().build();
                    }
                }
            });
            
            return this;
        }
        
        @Override
        protected IotaAccount compile(){
            return new IotaAccount(this);
        }
    }
}
