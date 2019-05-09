package org.iota.jota.account.plugins.transferchecker;

import org.iota.jota.account.plugins.AccountPlugin;
import org.iota.jota.types.Address;

public abstract class TransferCheckerImpl extends AccountPlugin {

    public boolean checkAddressNow(Address address){
        return false;
    }
}
