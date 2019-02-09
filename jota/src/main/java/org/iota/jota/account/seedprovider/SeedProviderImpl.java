package org.iota.jota.account.seedprovider;

import org.iota.jota.types.Trytes;

public class SeedProviderImpl implements SeedProvider {

    private Trytes seed;

    public SeedProviderImpl(Trytes seed) {
        this.seed = seed;
    }
    
    public SeedProviderImpl(String seed) {
        this.seed = new Trytes(seed);
    }
    
    @Override
    public Trytes getSeed() {
        return seed;
    }
}
