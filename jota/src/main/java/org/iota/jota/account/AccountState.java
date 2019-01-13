package org.iota.jota.account;

import java.util.Set;

public interface AccountState {

    Set<Integer> getIndexes();

    String getAddress(Integer index);

    void addAddress(String address, Integer index);

    AccountState clone() throws CloneNotSupportedException;

}
