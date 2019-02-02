package org.iota.jota.account.transferchecker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.events.EventSendingTransfer;
import org.iota.jota.account.event.events.EventTransferConfirmed;
import org.iota.jota.dto.response.GetInclusionStateResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.utils.thread.UnboundScheduledExecutorService;

public class OutgoingTransferCheckerImpl extends TransferCheckerImpl implements OutgoingTransferChecker {

    private static final long CHECK_CONFIRMED_DELAY = 10000;

    private Map<String, ScheduledFuture<?>> unconfirmedBundles;
    
    private UnboundScheduledExecutorService service;
    private EventManager eventManager;

    private IotaAPI api;

    public OutgoingTransferCheckerImpl(EventManager eventManager, IotaAPI api) {
        this.eventManager = eventManager;
        this.api = api;
    }

    @Override
    public void load() {
        unconfirmedBundles = new ConcurrentHashMap<>();
        service = new UnboundScheduledExecutorService();
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
        Runnable r = () -> doTask(event.getBundle());
        unconfirmedBundles.put(
            event.getBundle().getBundleHash(), 
            service.scheduleAtFixedRate(r, CHECK_CONFIRMED_DELAY, CHECK_CONFIRMED_DELAY, TimeUnit.MILLISECONDS)
        );
    }

    private void doTask(Bundle bundle) {
        try {
            String hash = bundle.getTransactions().get(0).getHash();
            GetInclusionStateResponse check = api.getLatestInclusion(hash);
            if (check.getStates()[0]) {
                ScheduledFuture<?> runnable = unconfirmedBundles.get(hash);
                runnable.cancel(true);
                unconfirmedBundles.remove(bundle.getBundleHash());
                
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
