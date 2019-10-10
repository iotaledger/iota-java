package org.iota.jota.pow.pearldiver;

import org.iota.jota.pow.IotaLocalPoW;
import org.iota.jota.utils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Perform local PoW using Come-from-Beyond's PearlDiver implementation.
 */
public class PearlDiverLocalPoW implements IotaLocalPoW {

    private static final Logger log = LoggerFactory.getLogger(PearlDiverLocalPoW.class);

    private final PearlDiver pearlDiver = new PearlDiver();

    @Override
    public String performPoW(String trytes, int minWeightMagnitude) {
        long startTime = System.currentTimeMillis();

        int[] trits = Converter.trits(trytes);
        if (!pearlDiver.search(trits, minWeightMagnitude, 0)) {
            throw new IllegalStateException("PearlDiver search failed");
        }
        String convertedTrits = Converter.trytes(trits);

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        log.debug("Locale POW took {} ms.", elapsedTime);

        return convertedTrits;
    }
}
