package jota;

import jota.model.Transaction;

/**
 * Interface for an implementation to perform local PoW.
 */
public interface IotaLocalPoW {
    String performPoW(Transaction transaction);
    String performPoW(Transaction transaction, int minWeightMagnitude);
}
