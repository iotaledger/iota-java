package org.iota.jota.account.plugins.transferchecker;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.AccountBalanceCache;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.addressgenerator.AddressGeneratorService;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.StoredDepositAddress;
import org.iota.jota.account.errors.AccountError;
import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.events.EventNewInput;
import org.iota.jota.account.event.events.EventReceivedDeposit;
import org.iota.jota.account.event.events.EventSentTransfer;
import org.iota.jota.model.Input;
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

    private boolean skipFirst;

    private AccountBalanceCache cache;

    public IncomingTransferCheckerImpl(EventManager eventManager, IotaAPI api, AccountStateManager accountManager, 
            AddressGeneratorService addressGen, AccountBalanceCache cache) {
        this(eventManager, api, accountManager, addressGen, cache, false);
    }
    
    public IncomingTransferCheckerImpl(EventManager eventManager, IotaAPI api, AccountStateManager accountManager, 
            AddressGeneratorService addressGen, AccountBalanceCache cache, boolean skipFirst) {
        
        this.addressGen = addressGen;
        this.eventManager = eventManager;
        this.api = api;
        this.accountManager = accountManager;
        this.cache = cache;
        
        this.skipFirst = skipFirst;
    }

    @Override
    public void load() {
        unconfirmedBundles = new ConcurrentHashMap<>();
        service = new UnboundScheduledExecutorService();
    }
    
    @Override
    public boolean start() {
        for (Entry<Integer, StoredDepositAddress> entry : accountManager.getDepositRequests().entrySet()) {
            if (entry.getValue().getSecurityLevel() != addressGen.getSecurityLevel()) {
                // Different security level request, ignoring for now
                continue;
            }
            Address address = addressGen.get(entry.getKey());
            addUnconfirmedBundle(address);
        }
        
        //Only optionally skip first for those we loaded, not the new ones
        skipFirst = false;
        
        return true;
    }
    
    /**
     * Adds a new {@link IncomingTransferCheckerTask} for each address
     * @param address
     */
    private void addUnconfirmedBundle(Address address) {
        ScheduledFuture<?> task = service.scheduleAtFixedRate(
                new IncomingTransferCheckerTask(address, api, eventManager, skipFirst, accountManager), 
                0, CHECK_INCOMING_DELAY, TimeUnit.MILLISECONDS);
        unconfirmedBundles.put(address.getAddress().getHash(), task);
    }
    
    @AccountEvent
    public void newInput(EventNewInput event) {
        addUnconfirmedBundle(event.getAddress());
    }
    
    @AccountEvent
    public void onReceivedDeposit(EventReceivedDeposit receivedEvent) {
        Entry<Input, DepositRequest> res = cache.getByAddress(receivedEvent.getAddress());
        
        if (null == res) {
            // Received unknown deposit!!, create, SAVE and mark
            // How did we get here though? Should not check any other addresses then the ones we have made
            //cache.addBalance(null, null);
            throw new AccountError("Got a received deposit which is not found in the cache!");
        }
        
        //Update balance
        res.getKey().setBalance(res.getKey().getBalance() + receivedEvent.getAmount());
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

    @Override
    public void shutdown() {
        service.shutdownNow();
    }

    @Override
    public String name() {
        return "IncomingTransferChecker";
    }

}
