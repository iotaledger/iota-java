package org.iota.jota.account.plugins.transferchecker;

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
import org.iota.jota.account.event.events.EventSentTransfer;
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

    private static final long CHECK_CONFIRMED_DELAY = 30000;

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
    }

    @Override
    public boolean start() {
        for (Entry<String, PendingTransfer> entry : accountManager.getPendingTransfers().entrySet()) {
            Bundle bundle = new Bundle();
            for (Trits trits : entry.getValue().getBundleTrits()){
                bundle.addTransaction( new Transaction(Converter.trytes(trits.getTrits())));
            }
            //TODO: use all tails created from reattach
            addUnconfirmedBundle(bundle);
        }
        return true;
    }

    @Override
    public void shutdown() {
        service.shutdownNow();
    }
    
    @AccountEvent
    private void onBundleBroadcast(EventSentTransfer event) {
        addUnconfirmedBundle(event.getBundle());
    }
    
    private void addUnconfirmedBundle(Bundle bundle) {
        Runnable r = () -> doTask(bundle);
        unconfirmedBundles.put(
            bundle.getBundleHash(), 
            service.scheduleAtFixedRate(r, 0, CHECK_CONFIRMED_DELAY, TimeUnit.MILLISECONDS)
        );
    }

    private void doTask(Bundle bundle) {
        try {
            String hash = bundle.getTransactions().get(0).getHash();
            PendingTransfer pending = accountManager.getPendingTransfers().get(hash);
            
            // Get states of all tails (reattachments incl original)
            GetInclusionStateResponse check = api.getLatestInclusion(
                pending.getTailHashes().stream().map(Hash::getHash).toArray(size -> new String[size])
            );
            
            if (anyTrue(check.getStates())) {
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

    private boolean anyTrue(boolean[] states) {
        for (boolean b : states) {
            if (b) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String name() {
        return "OutgoingTransferChecker";
    }
}
