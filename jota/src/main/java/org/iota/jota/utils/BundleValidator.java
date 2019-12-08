package org.iota.jota.utils;

import static org.iota.jota.pow.JCurl.HASH_LENGTH;
import static org.iota.jota.utils.Constants.INVALID_BUNDLE_ERROR;
import static org.iota.jota.utils.Constants.INVALID_BUNDLE_HASH_ERROR;
import static org.iota.jota.utils.Constants.INVALID_BUNDLE_SUM_ERROR;
import static org.iota.jota.utils.Constants.INVALID_SIGNATURES_ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.SpongeFactory;

public class BundleValidator {
    
    /**
     * Validates all signatures of a bundle
     * @param bundle the bundle
     * @param customCurl
     * @return true if all signatures are valid. Otherwise false
     */
    public static boolean validateSignatures(Bundle bundle, ICurl customCurl) {
        for (int i = 0; i < bundle.getLength(); i++) {
            Transaction tx = bundle.getTransactions().get(i);
            
            // check whether input transaction
            if (tx.getValue() >= 0) {
                continue;
            }
            
            List<String> fragments = new ArrayList<>();
            fragments.add(tx.getSignatureFragments());
            
            // find the subsequent txs containing the remaining signature
            // message fragments for this input transaction
            for (int j = i; j < bundle.getLength()-1; j++ ){
                Transaction otherTx = bundle.getTransactions().get(j+1);
                if (otherTx.getValue() != 0 || !otherTx.getAddress().equals(tx.getAddress())) {
                    continue;
                }

                fragments.add(otherTx.getSignatureFragments());
            }

            boolean valid = new Signing(customCurl).validateSignatures(tx.getAddress(), fragments.toArray(new String[fragments.size()]), tx.getBundle());
            if (!valid) {
                return false;
            }
            
        }
        return true;
    }

    /**
     * Checks if a bundle is syntactically valid. 
     * Validates signatures and overall structure
     * @param bundle the bundle to verify
     * @return true if the bundle is valid. 
     * @throws ArgumentException if there is an error with the bundle
     */
    public static boolean isBundle(Bundle bundle) throws ArgumentException {
        return isBundle(bundle, null);
    }
    
    /**
     * Checks if a bundle is syntactically valid. 
     * Validates signatures and overall structure
     * @param bundle the bundle to verify
     * @param customCurlMode
     * @return true if the bundle is valid. 
     * @throws ArgumentException if there is an error with the bundle
     */
    public static boolean isBundle(Bundle bundle, Optional<SpongeFactory.Mode> customCurlMode) throws ArgumentException {
        ICurl curl = null;
        if (null == customCurlMode || !customCurlMode.isPresent()) {
            curl = SpongeFactory.create(SpongeFactory.Mode.KERL);
        } else {
            curl = SpongeFactory.create(customCurlMode.get());
        }
        
        if (bundle == null) {
            throw new ArgumentException(INVALID_BUNDLE_ERROR);
        }
        
        int totalSum = 0;
        int lastIndex = bundle.getLength() - 1;
        
        for (int i = 0; i < bundle.getLength(); i++) {
            Transaction tx = bundle.getTransactions().get(i);
            totalSum += tx.getValue();

            if (tx.getCurrentIndex() != i ){
                throw new ArgumentException(INVALID_BUNDLE_ERROR);
            }
            if (tx.getLastIndex() != lastIndex ){
                throw new ArgumentException(INVALID_BUNDLE_ERROR);
            }

            int[] txTrits = Converter.trits(tx.toTrytes().substring(2187, 2187 + 162));
            curl.absorb(txTrits);

            // continue if output or signature tx
            if (tx.getValue() >= 0 ){
                continue;
            }

            // here we have an input transaction (negative value)
            List<String> fragments = new ArrayList<>();
            fragments.add(tx.getSignatureFragments());
            
            // find the subsequent txs containing the remaining signature
            // message fragments for this input transaction
            for (int j = i; j < bundle.getLength()-1; j++) {
                Transaction tx2 = bundle.getTransactions().get(j+1);
                
                // check if the tx is part of the input transaction
                if (tx.getAddress().equals(tx2.getAddress()) && tx2.getValue() == 0) {
                    // append the signature message fragment
                    fragments.add(tx2.getSignatureFragments());
                }
            }
            boolean valid = new Signing(curl.clone()).validateSignatures(tx.getAddress(), fragments.toArray(new String[fragments.size()]), tx.getBundle());
            if (!valid) {
                throw new ArgumentException(INVALID_SIGNATURES_ERROR);
            }
        }

        // sum of all transaction must be 0
        if (totalSum != 0) {
            throw new ArgumentException(INVALID_BUNDLE_SUM_ERROR);
        }

        int[] bundleHashTrits = new int[HASH_LENGTH];
        curl.squeeze(bundleHashTrits, 0, HASH_LENGTH);
        String bundleHash = Converter.trytes(bundleHashTrits);

        if (!bundleHash.equals(bundle.getTransactions().get(0).getBundle())) {
            throw new ArgumentException(INVALID_BUNDLE_HASH_ERROR);
        }
        return true;
    }
}
