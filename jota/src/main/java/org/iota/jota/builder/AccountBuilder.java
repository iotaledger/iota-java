package org.iota.jota.builder;

import org.iota.jota.IotaAPI;
import org.iota.jota.IotaAccount;
import org.iota.jota.account.AccountStore;
import org.iota.jota.account.clock.Clock;
import org.iota.jota.account.clock.SystemClock;
import org.iota.jota.account.seedprovider.SeedProvider;
import org.iota.jota.account.seedprovider.SeedProviderImpl;
import org.iota.jota.config.AccountConfig;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountBuilder extends AbstractBuilder<AccountBuilder, IotaAccount, AccountConfig> implements AccountConfig, AccountBuilderSettings {
    
    private static final Logger log = LoggerFactory.getLogger(AccountBuilder.class);
    
    private AccountStore store;
    private IotaAPI api;

    private SeedProvider seed;
    
    private int mwm, depth, securityLevel;
    
    private Clock clock;
    
    public AccountBuilder(String seed) throws ArgumentException {
        super(log);
        
        if (!InputValidator.isValidSeed(seed)) {
            throw new ArgumentException(Constants.INVALID_SEED_INPUT_ERROR);
        }
        
        this.seed = new SeedProviderImpl(seed);
    }
    
    public AccountBuilder(SeedProvider seed) throws ArgumentException {
        super(log);
        this.seed = seed;
    }
    
    public AccountBuilder mwm(int mwm) {
        if (mwm > 0) {
            this.mwm = mwm;
        } else {
            log.warn(Constants.INVALID_INPUT_ERROR);
        }
        return this;
    }
    
    public AccountBuilder depth(int depth) {
        if (depth > 0) {
            this.depth = depth;
        } else {
            log.warn(Constants.INVALID_INPUT_ERROR);
        }
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
        this.store = store;
        return this;
    }
    
    public AccountBuilder api(IotaAPI api) {
        this.api = api;
        return this;
    }
    
    public AccountBuilder clock(Clock clock) {
        this.clock = clock;
        return this;
    }

    @Override
    public AccountBuilder generate() throws Exception {
        //If a config is specified through ENV, that one will be in the stream, otherwise default config is used
        for (AccountConfig config : getConfigs()) {
            if (config != null) {
                //calculate Account specific values
                
                if (0 == getMwm()) {
                    mwm(config.getMwm());
                }
                
                if (0 == getDepth()) {
                    depth(config.getDepth());
                }
                
                if (0 == getSecurityLevel()) {
                    securityLevel(config.getSecurityLevel());
                }
                
                if (null == store) {
                    store(config.getStore());
                }
                
                if (null == api) {
                    api(new IotaAPI.Builder().build());
                }
                
                if (null == clock) {
                    // TODO: Configify
                    clock(new SystemClock());
                }
            }
        }
        
        return this;
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
}