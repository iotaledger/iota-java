package org.iota.jota.account.plugins.transferchecker;

import java.util.ArrayList;
import java.util.List;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.event.Event;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.events.EventReceivedDeposit;
import org.iota.jota.account.event.events.EventReceivedMessage;
import org.iota.jota.account.event.events.EventReceivingDeposit;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Address;
import org.iota.jota.utils.BundleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncomingTransferCheckerTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(IncomingTransferCheckerTask.class);
    
    private Address address;
    private IotaAPI api;
    
    //Bundle hash
    private List<String> receivingBundles;
    private List<String> receivedBundles;
    
    private List<String> invalidBundles;
    
    private boolean skipFirst;
    private EventManager eventManager;
    
    private AccountStateManager accountManager;

    public IncomingTransferCheckerTask(Address address, IotaAPI api, EventManager eventManager, boolean skipFirst, 
            AccountStateManager accountManager) {
        
        this.address = address;
        this.api = api;
        this.eventManager = eventManager;
        
        this.skipFirst = skipFirst;

        this.receivingBundles = new ArrayList<>();
        this.receivedBundles = new ArrayList<>();
        
        // We cache the invalid bundles for quicker look up
        this.invalidBundles = new ArrayList<>();
        
        this.accountManager = accountManager;
    }

    @Override
    public void run() {
        String addrHash = address.getAddress().getHashCheckSum();
        try {
            Bundle[] bundles = api.bundlesFromAddresses(true, addrHash);
            if (bundles == null) {
                // Thread got interrupted, which means we stopped using this address
                return;
            }
            
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
                    if (t.getValue() != 0) {
                        boolean isOwn = accountManager.isOwnAddress(t.getAddress());
                        if (t.getValue() > 0) {
                            isTransferToOwnRemainderAddr = isOwn;
                        } else {
                            isSpendFromOwnAddr = isOwn;
                        }
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
                        emit(new EventReceivingDeposit(bundle, address));
                    }
                    
                    if (isConsistent(bundle)) {
                        // Approved
                        receivedBundles.add(bundle.getBundleHash());
                        emit(new EventReceivedDeposit(bundle, address));
                    }
                } else {
                    //Message
                    receivedBundles.add(bundle.getBundleHash());
                    emit(new EventReceivedMessage(bundle));
                }
            }

            skipFirst = false;
        } catch (Exception e) {
            // http call closed?, could be a problem so we log, could also be a timeout
            if (!Thread.interrupted()) {
                System.out.println("EHUGFSIF");
                log.warn(e.getMessage(), e);
            }
        }
    }

    private boolean isValid(Bundle bundle) {
        if (invalidBundles.contains(bundle.getBundleHash())) {
            return true;
        } else {
            boolean valid = BundleValidator.isBundle(bundle);
            if (!valid) {
                invalidBundles.add(bundle.getBundleHash());
            }
            return valid;
        }
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
            return;
        }
        eventManager.emit(event);
    }
}
