package org.iota.jota.account.transferchecker.tasks;

import java.util.ArrayList;
import java.util.List;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Address;
import org.iota.jota.utils.BundleValidator;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;

public class CheckIncomingTask implements Runnable {

    private Address address;
    private IotaAPI api;
    
    //Bundle hash
    private List<String> receivedBundles;

    public CheckIncomingTask(Address address, IotaAPI api, EventManager eventManager) {
        this.address = address;
        this.api = api;
        
        receivedBundles = new ArrayList<>();
    }

    @Override
    public void run() {
        String addrHash = address.getAddress().getHash();
        
        Bundle[] bundles = api.bundlesFromAddresses(true, addrHash);
        
        for (Bundle bundle : bundles) {
            if (receivedBefore(bundle)) {
                continue;
            } else if (!isValid(bundle)) {
                continue;
            }
            
            if (isConsistent(bundle)) {
                
            }
            
            if (isValue(bundle)) {
                
            }
        }
    }

    private boolean isValid(Bundle bundle) {
        return BundleValidator.isBundle(bundle);
    }

    private boolean isConsistent(Bundle bundle) {
        for (Transaction t : bundle.getTransactions()) {
            if (!t.getPersistence()) return false;
        }
        return true;
    }

    private boolean receivedBefore(Bundle bundle) {
        return receivedBundles.contains(bundle.getBundleHash());
    }

    private boolean isValue(Bundle bundle) {
        for (Transaction t : bundle.getTransactions()) {
            if (t.getValue() != 0) return true;
        }
        return false;
    }
}
