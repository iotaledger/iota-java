package org.iota.jota.account.promoter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.AccountState;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.PendingTransfer;
import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.Plugin;
import org.iota.jota.account.event.events.EventPromotion;
import org.iota.jota.account.event.events.EventReattachment;
import org.iota.jota.account.event.events.EventSendingTransfer;
import org.iota.jota.account.event.events.EventTransferConfirmed;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.dto.response.ReplayBundleResponse;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Hash;
import org.iota.jota.utils.thread.UnboundScheduledExecutorService;

public class PromoterReattacherImpl implements PromoterReattacher, Plugin {

    private static final long PROMOTE_DELAY = 10000;
    
    private EventManager eventManager;

    private IotaAPI api;

    private AccountStateManager manager;

    private AccountOptions options;
    
    
    
    private UnboundScheduledExecutorService service;
    
    private Map<String, ScheduledFuture<?>> unconfirmedBundles;
    
    public PromoterReattacherImpl(EventManager eventManager, IotaAPI api, AccountStateManager manager, AccountOptions options) {
        this.eventManager = eventManager;
        this.api = api;
        this.manager = manager;
        this.options = options;
    }
    
    @Override
    public void load() {
        unconfirmedBundles = new ConcurrentHashMap<>();
        service = new UnboundScheduledExecutorService();
        
        for (Entry<String, PendingTransfer> entry : manager.getPendingTransfers().entrySet()) {
            
        }
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
    private void onBundleBroadcast(EventSendingTransfer event) {
        addUnconfirmedBundle(event.getBundle());
    }

    private void addUnconfirmedBundle(Bundle bundle) {
        Runnable r = () -> doTask(bundle);
        unconfirmedBundles.put(
            bundle.getBundleHash(), 
            service.scheduleAtFixedRate(r, PROMOTE_DELAY, PROMOTE_DELAY, TimeUnit.MILLISECONDS)
        );
    }
    
    @AccountEvent
    private void onConfirmed(EventTransferConfirmed event) {
        ScheduledFuture<?> runnable = unconfirmedBundles.get(event.getBundle().getBundleHash());
        runnable.cancel(true);
        unconfirmedBundles.remove(event.getBundle().getBundleHash());
    }

    private void doTask(Bundle bundle) {
        Bundle pendingBundle;
        while ((pendingBundle = getPendingBundle(bundle)) != null) {
            Transaction promotableTail = findPromotableTail(pendingBundle);
            if (promotableTail != null) {
                promote(pendingBundle, promotableTail); 
            } else {
                reattach(pendingBundle);
            }
        } 
    }
    
    private Bundle getPendingBundle(Bundle bundle) {
        return null;
    }
    
    private Transaction findPromotableTail(Bundle pendingBundle) {
        return null;
    }

    private void promote(Bundle pendingBundle) {
        this.promote(pendingBundle, pendingBundle.getTransactions().get(0));
    }
    
    private void promote(Bundle pendingBundle, Transaction promotableTail) {
        List<Transaction> res = api.promoteTransaction(
                promotableTail.getHash(), options.getDept(), options.getMwm(), pendingBundle);
        
        
        
        Bundle promotedBundle = new Bundle(res);
        
        EventPromotion event = new EventPromotion(promotedBundle);
        eventManager.emit(event);
    }

    private void reattach(Bundle pendingBundle) {
        Bundle newBundle = createReattachBundle(pendingBundle);
        manager.addTailHash(
                new Hash(pendingBundle.getTransactions().get(0).getHash()), 
                new Hash(newBundle.getTransactions().get(0).getHash())
            );
        
        EventReattachment event = new EventReattachment(pendingBundle, newBundle);
        eventManager.emit(event);
        
        promote(newBundle);
    }
    
    private Bundle createReattachBundle(Bundle pendingBundle) {
        ReplayBundleResponse ret = api.replayBundle(pendingBundle, options.getDept(), options.getMwm(), 
                pendingBundle.getTransactions().get(0).getHash());
        
        return ret.getNewBundle();
    }

    @Override
    public String name() {
        return "PromoterReattacherImpl";
    }
}
