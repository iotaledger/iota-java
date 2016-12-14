package jota.utils;

import java.text.DecimalFormat;

/**
 * Created by Sascha on 30.11.16.
 */
public class IotaUnitConverter {

    public static long convertUnits(long amount, IotaUnits fromUnit, IotaUnits toUnit) {
        long amountInSource = (long) (amount * Math.pow(10, fromUnit.getValue()));
        return convertUnits(amountInSource, toUnit);
    }

    private static long convertUnits(long amount, IotaUnits toUnit) {
        return (long) (amount / Math.pow(10, toUnit.getValue()));
    }

    public static String convertRawIotaAmountToDisplayText(long amount) {
        IotaUnits unit = findOptimalIotaUnitToDisplay(amount);
        double amountInDisplayUnit = convertAmountTo(amount, unit);
        return createAmountWithUnitDisplayText(amountInDisplayUnit, unit);
    }

    public static double convertAmountTo(long amount, IotaUnits target) {
        return amount / Math.pow(10, target.getValue());
    }

    private static String createAmountWithUnitDisplayText(double amountInUnit, IotaUnits unit) {
        String result = createAmountDisplayText(amountInUnit, unit);
        result += " " + unit.getUnit();
        return result;
    }

    public static String createAmountDisplayText(double amountInUnit, IotaUnits unit) {
        DecimalFormat df = new DecimalFormat("##0.##################");
        String result = "";
        // display unit as integer if value is between 1-999 or in decimal format
        result += unit == IotaUnits.IOTA ? (long) amountInUnit : df.format(amountInUnit);
        return result;
    }

    public static IotaUnits findOptimalIotaUnitToDisplay(long amount) {
        int length = String.valueOf(amount).length();

        if (amount < 0) // do not count "-" sign
            length -= 1;

        IotaUnits units = IotaUnits.IOTA;

        if (length >= 1 && length <= 3) {
            units = IotaUnits.IOTA;
        } else if (length > 3 && length <= 6) {
            units = IotaUnits.KILO_IOTA;
        } else if (length > 6 && length <= 9) {
            units = IotaUnits.MEGA_IOTA;
        } else if (length > 9 && length <= 12) {
            units = IotaUnits.GIGA_IOTA;
        } else if (length > 12 && length <= 15) {
            units = IotaUnits.TERA_IOTA;
        } else if (length > 15 && length <= 18) {
            units = IotaUnits.PETA_IOTA;
        }
        return units;
    }

}
