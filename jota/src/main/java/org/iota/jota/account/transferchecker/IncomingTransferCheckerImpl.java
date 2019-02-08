package org.iota.jota.account.transferchecker;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.addressgenerator.AddressGeneratorService;
import org.iota.jota.account.deposits.StoredDepositRequest;
import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.events.EventSentTransfer;
import org.iota.jota.account.transferchecker.tasks.CheckIncomingTask;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Address;
import org.iota.jota.utils.thread.UnboundScheduledExecutorService;

public class IncomingTransferCheckerImpl extends TransferCheckerImpl implements IncomingTransferChecker {
    
    private static final long CHECK_INCOMING_DELAY = 10000;
    
    private EventManager eventManager;
    private IotaAPI api;
    private AccountStateManager accountManager;
    
    private ConcurrentHashMap<String, ScheduledFuture<?>> unconfirmedBundles;
    private UnboundScheduledExecutorService service;

    private AddressGeneratorService addressGen;

    public IncomingTransferCheckerImpl(EventManager eventManager, IotaAPI api, AccountStateManager accountManager, 
            AddressGeneratorService addressGen) {
        
        this.addressGen = addressGen;
        this.eventManager = eventManager;
        this.api = api;
        this.accountManager = accountManager;
    }

    @Override
    public void load() {
        unconfirmedBundles = new ConcurrentHashMap<>();
        service = new UnboundScheduledExecutorService();
    }
    
    @Override
    public boolean start() {
        for (Entry<Integer, StoredDepositRequest> entry : accountManager.getDepositRequests().entrySet()) {
            if (entry.getValue().getSecurityLevel() != addressGen.getSecurityLevel()) {
                // Different security level request, ignoring for now
                continue;
            }
            Address address = addressGen.get(entry.getKey());
            
            addUnconfirmedBundle(address);
        }
        return true;
    }
    
    private void addUnconfirmedBundle(Address address) {
        unconfirmedBundles.put(
            address.getAddress().getHash(), 
            service.scheduleAtFixedRate(new CheckIncomingTask(address, api, eventManager), 0, CHECK_INCOMING_DELAY, TimeUnit.MILLISECONDS)
        );
    }

    @AccountEvent
    public void onSpent(EventSentTransfer sentEvent) {
        for (Transaction t : sentEvent.getBundle().getTransactions()) {
            if (t.getValue() < 0) {
                //We spent this address, remove from searching for incoming
                
                ScheduledFuture<?> runnable = unconfirmedBundles.get(t.getAddress());
                if (null != runnable) {
                    runnable.cancel(true);
                    unconfirmedBundles.remove(t.getAddress());
                }
            }
        }
    }
    
    @AccountEvent
    public void addressGenerated() {
        //?
    }
    
    @AccountEvent
    public void depositGenerated() {
        //?
    }
    
    @AccountEvent
    public void inputAddressRequested() {
        //?
    }

    @Override
    public void shutdown() {
        service.shutdownNow();
    }

    @Override
    public String name() {
        return "IncomingTransferChecker";
    }

}
