package org.iota.jota.config.options;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.iota.jota.IotaAPI;
import org.iota.jota.IotaAccount;
import org.iota.jota.account.AccountStore;
import org.iota.jota.account.clock.Clock;
import org.iota.jota.account.seedprovider.SeedProvider;
import org.iota.jota.config.AccountConfig;

public class AccountOptions implements AccountConfig, AccountBuilderSettings {

    private int mwm;
    private int depth;
    private int securityLevel;
    
    private AccountStore store;
    private IotaAPI api;
    
    private SeedProvider seed;
    
    private Clock clock;
    
    public AccountOptions(IotaAccount.Builder builder) {
        mwm = builder.getMwm();
        depth = builder.getDept();
        securityLevel = builder.getSecurityLevel();
        store = builder.getStore();
        api = builder.getApi();
        seed = builder.getSeed();
        clock = builder.getTime();
    }
    
    @Override
    public int getMwm() {
        return mwm;
    }

    @Override
    public int getDept() {
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
    public IotaAPI getApi() {
        return api;
    }

    @Override
    public SeedProvider getSeed() {
        return seed;
    }
    
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

    @Override
    public Clock getTime() {
        return clock;
    }
}
