package org.iota.jota.account.deposits.methods;

import org.iota.jota.account.deposits.DepositConditions;

public interface DepositMethod<T> {

    /**
     * 
     * @param method
     * @return
     */
    DepositConditions parse(T method);

    /**
     * 
     * @param conditions
     * @return
     */
    T build(DepositConditions conditions);
}
