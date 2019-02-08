package org.iota.jota.account.promoter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.iota.jota.account.event.Plugin;
import org.iota.jota.account.event.events.EventPromotion;
import org.iota.jota.account.event.events.EventReattachment;
import org.iota.jota.account.event.events.EventSendingTransfer;
import org.iota.jota.account.event.events.EventTransferConfirmed;
import org.iota.jota.config.options.AccountOptions;
import org.iota.jota.dto.response.GetTrytesResponse;
import org.iota.jota.dto.response.ReplayBundleResponse;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trits;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.thread.UnboundScheduledExecutorService;

public class PromoterReattacherImpl implements PromoterReattacher, Plugin {

    private static final int APROX_ABOVE_MAX_DEPTH_MIN = 5;
    private static final long PROMOTE_DELAY = 10000;
    
    private EventManager eventManager;

    private IotaAPI api;

    private AccountStateManager manager;

    private AccountOptions options;
    
    private UnboundScheduledExecutorService service;
    
    //TODO: Find a better structure for this, or a different object
    private Map<String, ScheduledFuture<?>> unconfirmedBundles;
    private Map<Hash, List<Transaction>> bundleTails;
    
    public PromoterReattacherImpl(EventManager eventManager, IotaAPI api, AccountStateManager manager, AccountOptions options) {
        this.eventManager = eventManager;
        this.api = api;
        this.manager = manager;
        this.options = options;
    }
    
    @Override
    public void load() {
        unconfirmedBundles = new ConcurrentHashMap<>();
        bundleTails = new ConcurrentHashMap<>();
        
        service = new UnboundScheduledExecutorService();
        
        for (Entry<String, PendingTransfer> entry : manager.getPendingTransfers().entrySet()) {
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
        if (null != runnable) {
            runnable.cancel(true); 
        }
        
        unconfirmedBundles.remove(event.getBundle().getBundleHash());
        bundleTails.remove(new Hash(event.getBundle().getBundleHash()));
    }

    private void doTask(Bundle bundle) {
        PendingTransfer pendingBundle = getPendingBundle(bundle);
        
        if (null == pendingBundle) {
            //Was this confirmed in the meantime?
            return;
        }
        
        Hash bundleHash = new Hash(bundle.getBundleHash());
        Hash promotableTail = findPromotableTail(pendingBundle, bundleHash);
        if (promotableTail != null) {
            promote(bundle, promotableTail); 
        } else {
            reattach(bundle);
        }
    }
    
    private PendingTransfer getPendingBundle(Bundle bundle) {
        for (Entry<String, PendingTransfer> entry : manager.getPendingTransfers().entrySet()) {
            if (entry.getKey().equals(bundle.getBundleHash())) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    private Hash findPromotableTail(PendingTransfer pendingBundle, Hash bundleHash) {
        for (int i =  pendingBundle.getTailHashes().length - 1; i >= 0; i--) {
            Hash tail = pendingBundle.getTailHashes()[i];
            Transaction tailTransaction = getBundleTail(bundleHash, tail);
            
            if (null == tailTransaction) {
                GetTrytesResponse res = api.getTrytes(tail.getHash());
                if (res.getTrytes().length < 1) {
                    // Cant find tail transaction
                    continue;
                }
                tailTransaction = new Transaction(res.getTrytes()[0]);
                addBundleTail(bundleHash, tailTransaction);
            }
            
            if (aboveMaxDepth(tailTransaction.getAttachmentTimestamp())) {
                continue;
            }
            
            return tail;
        }

        return null;
    }
    
    private void addBundleTail(Hash bundleHash, Transaction tailTransaction) {
        List<Transaction> tails = bundleTails.get(bundleHash);
        if (null == tails) {
            tails = new ArrayList<>();
            bundleTails.put(bundleHash, tails);
        }
         
        tails.add(tailTransaction);
    }

    private Transaction getBundleTail(Hash bundleHash, Hash tail) {
        List<Transaction> tails = bundleTails.get(bundleHash);
        
        if (null != tails) {
            for (Transaction t : tails) {
                if (t.getHash().equals(tail.getHash())) {
                    return t;
                }
            }
        }
        
        return null;
    }

    private boolean aboveMaxDepth(long time) {
        Date now = this.options.getTime().time();
        long res = now.getTime() - time;
        return TimeUnit.MINUTES.convert(res, TimeUnit.MILLISECONDS) < APROX_ABOVE_MAX_DEPTH_MIN;
    }

    private void promote(Bundle pendingBundle) {
        this.promote(pendingBundle, new Hash(pendingBundle.getTransactions().get(0).getHash()));
    }
    
    public void promote(Bundle pendingBundle, Hash promotableTail) {
        List<Transaction> res = api.promoteTransaction(
                promotableTail.getHash(), options.getDept(), options.getMwm(), pendingBundle);
        
        Bundle promotedBundle = new Bundle(res);
        
        EventPromotion event = new EventPromotion(promotedBundle);
        eventManager.emit(event);
    }

    public void reattach(Bundle pendingBundle) {
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
        return "promoter-reattacher";
    }
}
