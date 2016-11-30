package jota.utils;

/**
 * Created by pinpong on 30.11.16.
 */
public class IotaUnitConverter {

    public static long convertUnits(long amount, IotaUnits fromUnit, IotaUnits toUnit) {
        long amountInSource = (long) (amount * Math.pow(10, fromUnit.getValue()));
        return convertUnits(amountInSource, toUnit);
    }

    private static long convertUnits(long amount, IotaUnits toUnit) {
        return (long) (amount / Math.pow(10, toUnit.getValue()));
    }
}
