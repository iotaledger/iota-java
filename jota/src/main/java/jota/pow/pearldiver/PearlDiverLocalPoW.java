package jota.pow.pearldiver;

import jota.IotaLocalPoW;
import jota.utils.Converter;

/**
 * Perform local PoW using Come-from-Beyond's PearlDiver implementation.
 */
public class PearlDiverLocalPoW implements IotaLocalPoW {

    PearlDiver pearlDiver = new PearlDiver();

    @Override
    public String performPoW(String trytes, int minWeightMagnitude) {
        int[] trits = Converter.trits(trytes);
        if (!pearlDiver.search(trits, minWeightMagnitude, 0))
            throw new IllegalStateException("PearlDiver search failed");
        return Converter.trytes(trits);
    }
}
