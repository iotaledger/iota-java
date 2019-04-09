package org.iota.jota.account.promoter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.PendingTransfer;
import org.iota.jota.account.event.events.EventPromotion;
import org.iota.jota.account.event.events.EventReattachment;
import org.iota.jota.dto.response.GetTrytesResponse;
import org.iota.jota.dto.response.ReplayBundleResponse;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.types.Hash;

public class PromoterReattacherTask implements Runnable {
    
    IotaAPI api;
    
    Bundle bundle;
    
    public PromoterReattacherTask(Bundle bundle) {
        this.bundle = bundle;
    }
    
    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public void run() {
        try {/*
            System.out.println("Doign task for: " + bundle.getBundleHash());
            PendingTransfer pendingBundle = getPendingTransferForBundle(bundle);
            
            if (null == pendingBundle) {
                //Was this confirmed in the meantime?
                return;
            }
            Hash bundleHash = new Hash(bundle.getBundleHash());
            Hash promotableTail = findPromotableTail(pendingBundle, bundleHash);
            
            System.out.println(bundleHash);
            System.out.println(promotableTail);
            if (promotableTail != null) {
                promote(bundle, promotableTail); 
            } else {
                reattach(bundle);
            }*/
        } catch (Exception e) {
            //log.error("Failed to run promote task for " + bundle.getBundleHash() + ": " + e.getMessage());
        }
    }
    /*
    private PendingTransfer getPendingTransferForBundle(Bundle bundle) {
        String hash = bundle.getTransactions().get(0).getHash();
        for (Entry<String, PendingTransfer> entry : manager.getPendingTransfers().entrySet()) {
            if (entry.getKey().equals(hash)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    private Hash findPromotableTail(PendingTransfer pendingBundle, Hash bundleHash) {
        for (int i = pendingBundle.getTailHashes().size() - 1; i >= 0; i--) {
            Hash tail = pendingBundle.getTailHashes().get(i);
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
                promotableTail.getHash(), options.getDepth(), options.getMwm(), pendingBundle);
        
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
        ReplayBundleResponse ret = api.replayBundle(pendingBundle, options.getDepth(), options.getMwm(), 
                pendingBundle.getTransactions().get(0).getHash());
        
        return ret.getNewBundle();
    }*/
}
