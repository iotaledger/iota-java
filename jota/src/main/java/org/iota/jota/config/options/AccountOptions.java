package org.iota.jota.config.options;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.iota.jota.IotaAPI;
import org.iota.jota.IotaAccount;
import org.iota.jota.config.AccountConfig;
import org.iota.jota.store.PersistenceAdapter;

public class AccountOptions implements AccountConfig, AccountBuilderSettings {

    private int mwm;
    private int depth;
    private int securityLevel;
    
    private PersistenceAdapter store;
    private IotaAPI api;
    
    private String seed;
    
    public AccountOptions(IotaAccount.Builder builder) {
        mwm = builder.getMwm();
        depth = builder.getDept();
        securityLevel = builder.getSecurityLevel();
        store = builder.getStore();
        api = builder.getApi();
        seed = builder.getSeed();
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
        // TODO Auto-generated method stub
        return securityLevel;
    }

    @Override
    public PersistenceAdapter getStore() {
        return store;
    }

    @Override
    public IotaAPI getApi() {
        return api;
    }

    @Override
    public String getSeed() {
        return seed;
    }
    
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

}
