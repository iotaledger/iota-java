package org.iota.jota.account.store;

import org.iota.jota.account.AccountStore;
import org.iota.jota.types.Trits;
import org.iota.jota.types.Trytes;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.thread.TaskService;

public abstract class AccountStoreImpl implements AccountStore, TaskService {

    public Trits[] trytesToTrits(Trytes[] bundleTrytes) {
        Trits[] trits = new Trits[bundleTrytes.length];
        for (int i=0; i<bundleTrytes.length; i++) {
            Trytes t = bundleTrytes[i];
            trits[i] = new Trits(Converter.trits(t.toString()));
        }
        
        return trits;
    }
}
