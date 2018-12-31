package org.iota.jota.account;

import org.iota.jota.store.PersistenceAdapter;

public class AccountStateManager {

    private PersistenceAdapter store;
    private AccountState state;

    public AccountStateManager(AccountState state, PersistenceAdapter store) {
        this.store = store;
        this.state = state;
    }
    
    public String nextZeroValueAddress() {
        synchronized (this) {
            for (Integer index : state.getIndexes()) {
                if (index > 0) {
                    // Spent
                } else {
                    // We can use it!
                }
            }
        }
    }

    public AccountState getAccountState() {
        return state;
    }

    public void save() {
        state.save(store);
    }
}
