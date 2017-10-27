package jota;

/**
 * Interface for an implementation to perform local PoW.
 */
public interface IotaLocalPoW {
    String performPoW(String trytes, int minWeightMagnitude);
}
