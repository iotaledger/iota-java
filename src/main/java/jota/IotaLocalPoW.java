package jota;

/**
 * Interface for an implementation to perform local PoW.
 */
public interface IotaLocalPoW {
    public String performPoW(String trytes, int minWeightMagnitude);
}
