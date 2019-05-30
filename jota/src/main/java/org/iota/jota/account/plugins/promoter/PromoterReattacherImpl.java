package org.iota.jota.account.plugins.promoter;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.AccountOptions;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.PendingTransfer;
import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.account.event.events.EventPromotion;
import org.iota.jota.account.event.events.EventReattachment;
import org.iota.jota.account.event.events.EventSentTransfer;
import org.iota.jota.account.event.events.EventTransferConfirmed;
import org.iota.jota.account.plugins.AccountPlugin;
import org.iota.jota.dto.response.GetTrytesResponse;
import org.iota.jota.dto.response.ReplayBundleResponse;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trits;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.thread.UnboundScheduledExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PromoterReattacherImpl extends AccountPlugin implements PromoterReattacher {
    
    private static final Logger log = LoggerFactory.getLogger(PromoterReattacherImpl.class);
    
    private static final int APROX_ABOVE_MAX_DEPTH_MIN = 5;
    private static final long PROMOTE_DELAY = 10000;
    
    private EventManager eventManager;

    private IotaAPI api;

    private AccountStateManager manager;

    private AccountOptions options;
    
    private UnboundScheduledExecutorService service;
    
    //TODO: Find a better structure for this, or a different object
    /**
     * Bundle hash -> future check runnable
     */
    private Map<String, ScheduledFuture<?>> unconfirmedBundles;
    
    /**
     * Original tail mapped to its original tail tx and its reattachment tx
     */
    private Map<String, List<Transaction>> bundleTails;
    
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
            // Recreate the bundle
            Bundle bundle = new Bundle();
            for (Trits trits : entry.getValue().getBundleTrits()){
                Transaction tx = new Transaction(Converter.trytes(trits.getTrits()));
                bundle.addTransaction(tx);
            }
            bundle.setLength(entry.getValue().getBundleTrits().size());
            
            // Start 
            addUnconfirmedBundle(bundle, 0);
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
    private void onBundleBroadcast(EventSentTransfer event) {
        addUnconfirmedBundle(event.getBundle(), PROMOTE_DELAY);
    }

    private void addUnconfirmedBundle(Bundle bundle, long initialDelay) {
        addBundleTail(bundle.getTransactions().get(0).getHash(), bundle.getTransactions().get(0));
        Runnable r = () -> doTask(bundle);
        unconfirmedBundles.put(
            bundle.getBundleHash(), 
            service.scheduleAtFixedRate(r, initialDelay, PROMOTE_DELAY, TimeUnit.MILLISECONDS)
        );
    }
    
    @AccountEvent
    private void onConfirmed(EventTransferConfirmed event) {
        String tail = getOriginalTailFromBundle(event.getBundle());
        List<Transaction> allTails = bundleTails.get(tail);
        
        // Remove all possible reattaches/promotes etc
        for (Transaction tx : allTails) {
            ScheduledFuture<?> runnable = unconfirmedBundles.remove(tx.getBundle());
            if (null != runnable && !runnable.isDone()) {
                runnable.cancel(true); 
            }
        }
        
        //Then clear all
        bundleTails.remove(tail);
    }

    private String getOriginalTailFromBundle(Bundle bundle) {
        String tail = bundle.getTransactions().get(0).getHash();
        for (Entry<String, List<Transaction>> entry : bundleTails.entrySet()) {
            if (entry.getKey().equals(tail)) {
                return entry.getKey();
            } else {
                for (Transaction tx : entry.getValue()) {
                    if (tx.getBundle().equals(bundle.getBundleHash())) {
                        return entry.getKey(); 
                    }
                }
            }
        }
        return null;
    }

    private void doTask(Bundle bundle) {
        try {
            PendingTransfer pendingBundle = getPendingTransferForBundle(bundle);
            if (null == pendingBundle) {
                //Was this confirmed in the meantime?
                return;
            }
            
            String promotableTail = findPromotableTail(pendingBundle);
            if (promotableTail != null) {
                promote(bundle, promotableTail); 
            } else {
                reattach(bundle);
            }
        } catch (Exception e) {
            log.error("Failed to run promote task for " + bundle.getBundleHash() + ": " + e.getMessage());
        }
    }
    
    private PendingTransfer getPendingTransferForBundle(Bundle bundle) {
        String hash = bundle.getTransactions().get(0).getHash();
        for (Entry<String, PendingTransfer> entry : manager.getPendingTransfers().entrySet()) {
            if (entry.getKey().equals(hash)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    private String findPromotableTail(PendingTransfer pendingBundle) {
        String tailOrig = pendingBundle.getTailHashes().get(0).getHash();
        for (int i = pendingBundle.getTailHashes().size() - 1; i >= 0; i--) {
            String tail = pendingBundle.getTailHashes().get(i).getHash();
            Transaction tailTransaction = getBundleTail(tailOrig, tail);
            
            if (null == tailTransaction) {
                GetTrytesResponse res = api.getTrytes(tail);
                if (res.getTrytes().length < 1) {
                    // Cant find tail transaction
                    continue;
                }
                tailTransaction = new Transaction(res.getTrytes()[0]);
                addBundleTail(tailOrig, tailTransaction);
            }
            
            if (aboveMaxDepth(tailTransaction.getAttachmentTimestamp())) {
                continue;
            }
            
            return tail;
        }

        return null;
    }
    
    private void addBundleTail(String bundleHash, Transaction tailTransaction) {
        List<Transaction> tails = bundleTails.get(bundleHash);
        if (null == tails) {
            tails = new ArrayList<>();
            bundleTails.put(bundleHash, tails);
        }
         
        tails.add(tailTransaction);
    }

    private Transaction getBundleTail(String originalTail, String tail) {
        List<Transaction> tails = bundleTails.get(originalTail);
        if (null != tails) {
            for (Transaction t : tails) {
                if (t.getHash().equals(tail)) {
                    return t;
                }
            }
        }
        
        return null;
    }

    private boolean aboveMaxDepth(long time) {
        Date now = this.options.getTime().time();
        long res = now.getTime() - time;
        return TimeUnit.MINUTES.convert(res, TimeUnit.MILLISECONDS) > APROX_ABOVE_MAX_DEPTH_MIN;
    }

    private void promote(Bundle pendingBundle) {
        this.promote(pendingBundle, pendingBundle.getTransactions().get(0).getHash());
    }
    
    public void promote(Bundle pendingBundle, String promotableTail) {
        List<Transaction> res = api.promoteTransaction(
                promotableTail, options.getDepth(), options.getMwm(), pendingBundle);

        Collections.reverse(res);
        Bundle promotedBundle = new Bundle(res);
        
        EventPromotion event = new EventPromotion(promotedBundle);
        eventManager.emit(event);
    }

    public void reattach(Bundle pendingBundle) {
        Bundle newBundle = createReattachBundle(pendingBundle);
        Collections.reverse(newBundle.getTransactions());

        manager.addTailHash(
            new Hash(pendingBundle.getTransactions().get(0).getHash()), 
            new Hash(newBundle.getTransactions().get(0).getHash())
        );
        
        EventReattachment event = new EventReattachment(pendingBundle, newBundle);
        eventManager.emit(event);
        
        promote(newBundle);
    }
    
    private Bundle createReattachBundle(Bundle pendingBundle) {
        ReplayBundleResponse ret = api.replayBundle(pendingBundle, options.getDepth(), options.getMwm(), null);
        
        return ret.getNewBundle();
    }

    @Override
    public String name() {
        return "promoter-reattacher";
    }
}
