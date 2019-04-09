package org.iota.jota.account.deposits.methods;

import org.iota.jota.account.deposits.ConditionalDepositAddress;

public interface DepositMethod<T> {

    /**
     * 
     * @param method
     * @return
     */
    ConditionalDepositAddress parse(T method);

    /**
     * 
     * @param conditions
     * @return
     */
    T build(ConditionalDepositAddress conditions);
}
