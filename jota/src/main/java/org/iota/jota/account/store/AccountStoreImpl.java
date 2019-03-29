package org.iota.jota.account.store;

import java.util.ArrayList;
import java.util.List;

import org.iota.jota.account.AccountStore;
import org.iota.jota.types.Trits;
import org.iota.jota.types.Trytes;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.thread.TaskService;

public abstract class AccountStoreImpl implements AccountStore, TaskService {

    protected List<Trits> trytesToTrits(Trytes[] bundleTrytes) {
        List<Trits> array = new ArrayList<>();
        for (int i=0; i<bundleTrytes.length; i++) {
            Trytes t = bundleTrytes[i];
            List<Integer> trits = new ArrayList<Integer>();
            for (int trit : Converter.trits(t.toString())) {
                trits.add(trit);
            }
            
            array.add(new Trits(trits));
        }
        
        return array;
    }
}
