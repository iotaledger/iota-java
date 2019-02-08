package org.iota.jota.account.transferchecker;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.PendingTransfer;
import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.events.EventSendingTransfer;
import org.iota.jota.account.event.events.EventTransferConfirmed;
import org.iota.jota.dto.response.GetInclusionStateResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trits;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.thread.UnboundScheduledExecutorService;

public class OutgoingTransferCheckerImpl extends TransferCheckerImpl implements OutgoingTransferChecker {

    private static final long CHECK_CONFIRMED_DELAY = 10000;

    private Map<String, ScheduledFuture<?>> unconfirmedBundles;
    
    private UnboundScheduledExecutorService service;
    private EventManager eventManager;

    private IotaAPI api;

    private AccountStateManager accountManager;

    public OutgoingTransferCheckerImpl(EventManager eventManager, IotaAPI api, AccountStateManager accountManager) {
        this.eventManager = eventManager;
        this.api = api;
        this.accountManager = accountManager;
    }

    @Override
    public void load() {
        unconfirmedBundles = new ConcurrentHashMap<>();
        service = new UnboundScheduledExecutorService();
        
        for (Entry<String, PendingTransfer> entry : accountManager.getPendingTransfers().entrySet()) {
            Bundle bundle = new Bundle();
            for (Trits trits : entry.getValue().getBundleTrits()){
                bundle.addTransaction( new Transaction(Converter.trytes(trits.getTrits())));
            }
            
            // Adding it immediately has delay, so also do it right now.
            // This will also get tail tx transactions
            doTask(bundle);
            addUnconfirmedBundle(bundle);
        }
    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public void shutdown() {
        service.shutdownNow();
    }
    
    @AccountEvent
    private void onBundleBroadcast(EventSendingTransfer event) {
        addUnconfirmedBundle(event.getBundle());
    }
    
    private void addUnconfirmedBundle(Bundle bundle) {
        Runnable r = () -> doTask(bundle);
        unconfirmedBundles.put(
            bundle.getBundleHash(), 
            service.scheduleAtFixedRate(r, CHECK_CONFIRMED_DELAY, CHECK_CONFIRMED_DELAY, TimeUnit.MILLISECONDS)
        );
    }

    private void doTask(Bundle bundle) {
        try {
            String hash = bundle.getTransactions().get(0).getHash();
            GetInclusionStateResponse check = api.getLatestInclusion(hash);
            if (check.getStates()[0]) {
                // Restart might not have a runnable
                ScheduledFuture<?> runnable = unconfirmedBundles.get(bundle.getBundleHash());
                if (null != runnable) {
                    runnable.cancel(true);
                }
                
                unconfirmedBundles.remove(bundle.getBundleHash());
                
                accountManager.removePendingTransfer(new Hash(hash));
                
                EventTransferConfirmed event = new EventTransferConfirmed(bundle);
                eventManager.emit(event);
            } else {
                // Still unconfirmed...
            }
        } catch (ArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String name() {
        return "OutgoingTransferChecker";
    }
}
