package org.iota.jota;

/**
 * Interface for an implementation to perform local PoW.
 */
public interface IotaPoW {
    String performPoW(String trytes, int minWeightMagnitude);
}
