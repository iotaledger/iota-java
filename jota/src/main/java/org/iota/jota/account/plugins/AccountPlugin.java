package org.iota.jota.account.plugins;

import org.iota.jota.account.Account;

public abstract class AccountPlugin implements Plugin {

    private Account account;

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void load() throws Exception {

    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public void shutdown() {

    }
}
