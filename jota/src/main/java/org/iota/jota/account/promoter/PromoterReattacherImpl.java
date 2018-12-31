package org.iota.jota.account.promoter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.iota.jota.account.AccountState;
import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.EventTaskService;
import org.iota.jota.account.event.events.PromotionEvent;
import org.iota.jota.account.event.events.ReattachmentEvent;
import org.iota.jota.account.event.events.SendTransferEvent;
import org.iota.jota.account.event.events.TransferConfirmedEvent;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;

import org.iota.jota.utils.thread.UnboundScheduledExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PromoterReattacherImpl implements PromoterReattacher, EventTaskService {

    private static final long PROMOTE_DELAY = 10000;
    
    private UnboundScheduledExecutorService service;

    private EventManager eventManager;
    
    private Map<String, ScheduledFuture<?>> unconfirmedBundles;
    
    public PromoterReattacherImpl(EventManager eventManager) {
        this.eventManager = eventManager;
    }
    
    @Override
    public void load() {
        unconfirmedBundles = new ConcurrentHashMap<>();
    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public void shutdown(){
        service.shutdownNow();
    }
    
    @AccountEvent
    private void onBundleBroadcast(SendTransferEvent event) {
        Runnable r = () -> doTask(event.getBundle());
        unconfirmedBundles.put(
            event.getBundle().getBundleHash(), 
            //scheduler.scheduleWithFixedDelay(r, PROMOTE_DELAY, PROMOTE_DELAY, TimeUnit.MILLISECONDS)
            service.scheduleAtFixedRate(r, PROMOTE_DELAY, PROMOTE_DELAY, TimeUnit.MILLISECONDS)
        );
        System.out.println("Added bundle");
        //service.scheduleAtFixedRate(r, PROMOTE_DELAY, PROMOTE_DELAY, TimeUnit.MILLISECONDS);
    }
    
    @AccountEvent
    private void onConfirmed(TransferConfirmedEvent event) {
        ScheduledFuture<?> runnable = unconfirmedBundles.get(event.getBundle().getBundleHash());
        runnable.cancel(true);
        System.out.println("confirmed bundle");
        unconfirmedBundles.remove(event.getBundle().getBundleHash());
    }

    private void doTask(Bundle bundle) {
        AccountState state = null;
        Bundle pendingBundle;
        System.out.println("doTask bundle");
        while ((pendingBundle = getPendingBundle(state)) != null) {
            Transaction promotableTail = findPromotableTail(pendingBundle);
            if (promotableTail != null) {
                promote(pendingBundle, promotableTail); 
            } else {
                reattach(pendingBundle);
            }
        }
    }

    private void promote(Bundle pendingBundle) {
        this.promote(pendingBundle, pendingBundle.getTransactions().get(0));
    }
    
    private void promote(Bundle pendingBundle, Transaction promotableTail) {
        PromotionEvent event = new PromotionEvent();
        eventManager.emit(event);
    }

    private void reattach(Bundle pendingBundle) {
        Bundle newBundle = createReattachBundle(pendingBundle);
        ReattachmentEvent event = new ReattachmentEvent();
        eventManager.emit(event);
        
        promote(newBundle);
    }

    private Transaction findPromotableTail(Bundle pendingBundle) {
        return null;
    }
    
    private Bundle createReattachBundle(Bundle pendingBundle) {
        return null;
    }

    private Bundle getPendingBundle(AccountState state) {
        return null;
    }
}
