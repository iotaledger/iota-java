package org.iota.jota.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.iota.jota.IotaAPI;
import org.iota.jota.IotaAccount;
import org.iota.jota.account.AccountOptions;
import org.iota.jota.account.AccountStore;
import org.iota.jota.account.clock.Clock;
import org.iota.jota.account.clock.SystemClock;
import org.iota.jota.account.errors.AccountError;
import org.iota.jota.account.plugins.Plugin;
import org.iota.jota.account.seedprovider.SeedProvider;
import org.iota.jota.account.seedprovider.SeedProviderImpl;
import org.iota.jota.config.options.AccountConfig;
import org.iota.jota.config.options.AccountSettings;
import org.iota.jota.config.types.IotaDefaultConfig;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.error.InternalException;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Builder for the account module.
 * Note: Every option added to {@link AccountSettings} must add the following:
 * <ul>
 * <li>A setter, with the same name the option (excluding get), camelcase. Should be equal to the field</li>
 * <li>A check in the {@link #generate()} method to assign</li>
 * <li>A default option in {@link IotaDefaultConfig}</li>
 * </ul>
 * 
 * TODO: Make a reflection unit test for this
 */
public class AccountBuilder extends AbstractBuilder<AccountBuilder, IotaAccount, AccountConfig> 
    implements AccountSettings {
    
    private static final Logger log = LoggerFactory.getLogger(AccountBuilder.class);
    
    private AccountStore store;
    private IotaAPI api;

    private final SeedProvider seed;
    
    private int mwm;
    private int depth;
    private int securityLevel;
    
    private Clock clock;

    private List<Plugin> plugins;
    
    /**
     * Start of the builder. Every Account needs to be started with at least a seed.
     * 
     * @param seed The seed which we load the account for
     * @throws ArgumentException When an invalid seed is provided
     */
    public AccountBuilder(String seed) {
        super(log);
        
        if (!InputValidator.isValidSeed(seed)) {
            throw new ArgumentException(Constants.INVALID_SEED_INPUT_ERROR);
        }
        
        this.seed = new SeedProviderImpl(seed);
    }
    
    /**
     * Start of the builder. Every Account needs to be started with at least a seed.
     * 
     * @param seed A custom seed provider for maintaining your seed securely elsewhere
     */
    public AccountBuilder(SeedProvider seed) {
        super(log);
        this.seed = seed;
    }
    
    public AccountBuilder mwm(int mwm) {
        this.mwm = setIntValueOrLogAWarning(mwm, "mwm");
        return this;
    }

    private int setIntValueOrLogAWarning(int intValue, String intTypeName){
        int tempInt = 0;
        if (intValue > 0) {
            tempInt = intValue;
        } else {
            log.warn("{} For: {}", Constants.INVALID_INPUT_ERROR, intTypeName);
        }
        return tempInt;
    }
    
    public AccountBuilder depth(int depth) {
        this.depth = setIntValueOrLogAWarning(depth, "depth");
        return this;
    }
    
    public AccountBuilder securityLevel(int securityLevel) {
        if (InputValidator.isValidSecurityLevel(securityLevel)) {
            this.securityLevel = securityLevel;
        } else {
            log.warn(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }
        
        return this;
    }
    
    public AccountBuilder store(AccountStore store) {
        if (null != store) {
            this.store = store;
        } else {
            throw new AccountError("Cannot set store to null");
        }
        return this;
    }
    
    public AccountBuilder api(IotaAPI api) {
        if (null != api) {
            this.api = api;
        } else {
            throw new AccountError("Cannot set api to null");
        }
        return this;
    }
    
    public AccountBuilder clock(Clock clock) {
        if (null != clock) {
            this.clock = clock;
        } else {
            throw new AccountError("Cannot set clock to null");
        }
        return this;
    }
    
    public AccountBuilder plugin(Plugin plugin){
        if (null != plugin) {
            if (plugins == null) {
                plugins = new ArrayList<>();
            }

            plugins.add(plugin);
        } else {
            throw new AccountError("Attempted to add null as a plugin");
        }
        return this;
    }

    @Override
    public AccountBuilder generate() throws InternalException, IOException {
        //If a config is specified through ENV, that one will be in the stream, otherwise default config is used
        for (AccountConfig config : getConfigs()) {
            if (config != null) {
                //calculate Account specific values
                checkMwm(config);
                checkDepth(config);
                checkSecurityLevel(config);
                checkStore(config);
                checkApi();
                checkClock();
            } else {
                log.warn("AccountConfig is null");
            }
        }
        
        return this;
    }

    private void checkClock() {
        if (null == clock) {
            // TODO: Configify
            clock(new SystemClock());
        }
    }

    private void checkApi() {
        if (null == api) {
            api(new IotaAPI.Builder().build());
        }
    }

    private void checkStore(AccountConfig config) {
        if (null == store && config.getStore() != null) {
            store(config.getStore());
        }
    }

    private void checkSecurityLevel(AccountConfig config) {
        if (0 == getSecurityLevel() && config.getSecurityLevel() != 0) {
            securityLevel(config.getSecurityLevel());
        }
    }

    private void checkDepth(AccountConfig config) {
        if (0 == getDepth() && config.getDepth() != 0) {
            depth(config.getDepth());
        }
    }

    private void checkMwm(AccountConfig config) {
        if (0 == getMwm() && config.getMwm() != 0) {
            mwm(config.getMwm());
        }
    }

    @Override
    protected IotaAccount compile(){
        return new IotaAccount(new AccountOptions(this));
    }
    
    @Override
    public SeedProvider getSeed() {
        return seed;
    }

    @Override
    public IotaAPI getApi() {
        return api;
    }

    @Override
    public int getMwm() {
        return mwm;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public int getSecurityLevel() {
        return securityLevel;
    }

    @Override
    public AccountStore getStore() {
        return store;
    }

    @Override
    public Clock getTime() {
        return clock;
    }

    @Override
    public List<Plugin> getPlugins() {
        return plugins;
    }
}