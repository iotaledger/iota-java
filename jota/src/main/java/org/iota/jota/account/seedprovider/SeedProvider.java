package org.iota.jota.account.seedprovider;

import org.iota.jota.types.Trytes;

public interface SeedProvider {

    /**
     * Gets the seed from this provider
     * 
     * @return the seed trytes
     */
    Trytes getSeed();
}
