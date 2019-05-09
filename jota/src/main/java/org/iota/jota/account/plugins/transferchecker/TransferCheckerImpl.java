package org.iota.jota.account.transferchecker;

import org.iota.jota.account.event.Plugin;
import org.iota.jota.types.Address;

public abstract class TransferCheckerImpl implements Plugin {

    public boolean checkAddressNow(Address address){
        return false;
    }
}
