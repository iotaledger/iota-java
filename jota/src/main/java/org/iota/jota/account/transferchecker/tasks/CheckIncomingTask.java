package org.iota.jota.account.transferchecker.tasks;

import java.util.ArrayList;
import java.util.List;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.event.Event;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.events.EventReceievedMessage;
import org.iota.jota.account.event.events.EventReceivedDeposit;
import org.iota.jota.account.event.events.EventReceivingDeposit;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Address;
import org.iota.jota.utils.BundleValidator;

public class CheckIncomingTask implements Runnable {

    private Address address;
    private IotaAPI api;
    
    //Bundle hash
    private List<String> receivingBundles;
    private List<String> receivedBundles;
    
    private boolean skipFirst;
    private EventManager eventManager;
    
    private AccountStateManager accountManager;

    public CheckIncomingTask(Address address, IotaAPI api, EventManager eventManager, boolean skipFirst, 
            AccountStateManager accountManager) {
        
        this.address = address;
        this.api = api;
        this.eventManager = eventManager;
        
        this.skipFirst = skipFirst;
        
        this.receivedBundles = new ArrayList<>();
        this.accountManager = accountManager;
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
            
            //A transaction we send using inputs (Sweep for example)
            boolean isSpendFromOwnAddr = false;
            boolean isTransferToOwnRemainderAddr = false;
            
            //TODO: Optimize this, very intensive call since it checks all our addresses
            for (Transaction t : bundle.getTransactions()) {
                if (t.getValue() > 0) {
                    isTransferToOwnRemainderAddr = accountManager.isOwnAddress(t.getAddress());
                } else if (t.getValue() < 0) {
                    isSpendFromOwnAddr = accountManager.isOwnAddress(t.getAddress());
                }
            }
            
            if (isTransferToOwnRemainderAddr || isSpendFromOwnAddr) {
                receivedBundles.add(bundle.getBundleHash());
                continue;
            }
            
            // Value, only value messages are approved ('messages' are 0)
            if (isValue(bundle)) {
                if (!receivingBefore(bundle)) {
                    receivingBundles.add(bundle.getBundleHash());
                    emit(new EventReceivingDeposit());
                }
                
                if (isConsistent(bundle)) {
                    // Approved
                    receivedBundles.add(bundle.getBundleHash());
                    emit(new EventReceivedDeposit());
                }
            } else {
                //Message
                receivedBundles.add(bundle.getBundleHash());
                emit(new EventReceievedMessage());
            }
        }
    }

    private boolean isValid(Bundle bundle) {
        return BundleValidator.isBundle(bundle);
    }

    private boolean isConsistent(Bundle bundle) {
        for (Transaction t : bundle.getTransactions()) {
            if (!t.getPersistence()) {
                return false;
            }
        }
        return true;
    }

    private boolean receivedBefore(Bundle bundle) {
        return receivedBundles.contains(bundle.getBundleHash());
    }
    
    private boolean receivingBefore(Bundle bundle) {
        return receivingBundles.contains(bundle.getBundleHash());
    }

    private boolean isValue(Bundle bundle) {
        for (Transaction t : bundle.getTransactions()) {
            if (t.getValue() != 0) {
                return true;
            }
        }
        return false;
    }
    
    private void emit(Event event) {
        if (skipFirst) {
            skipFirst = false;
            return;
        }
        
        eventManager.emit(event);
    }
}
