package jota.utils;

import java.text.DecimalFormat;

/**
 * Created by Sascha on 30.11.16.
 */
public class IotaUnitConverter {

    /**
     * convert the iota amount
     *
     * @param amount   the amount
     * @param fromUnit the source unit e.g. the unit of amount
     * @param toUnit   the target unit
     * @return the specified amount in the target unit
     **/
    public static long convertUnits(long amount, IotaUnits fromUnit, IotaUnits toUnit) {
        long amountInSource = (long) (amount * Math.pow(10, fromUnit.getValue()));
        return convertUnits(amountInSource, toUnit);
    }

    /**
     * convert unit
     *
     * @param amount the amount
     * @param toUnit the target unit
     * @return the specified amount in the target unit
     **/
    private static long convertUnits(long amount, IotaUnits toUnit) {
        return (long) (amount / Math.pow(10, toUnit.getValue()));
    }

    /**
     * convert the iota amount to text
     *
     * @param amount   the amount
     * @param extended extended length
     * @return the specified amount in the target unit
     **/
    public static String convertRawIotaAmountToDisplayText(long amount, boolean extended) {
        IotaUnits unit = findOptimalIotaUnitToDisplay(amount);
        double amountInDisplayUnit = convertAmountTo(amount, unit);
        return createAmountWithUnitDisplayText(amountInDisplayUnit, unit, extended);
    }

    /**
     * convert amount to target unit
     *
     * @param amount the amount
     * @return the target unit
     **/
    public static double convertAmountTo(long amount, IotaUnits target) {
        return amount / Math.pow(10, target.getValue());
    }

    /**
     * create amount with unit text
     *
     * @param amountInUnit the amount in units
     * @param unit         the unit
     * @param extended     extended length
     * @return the target unit
     **/
    private static String createAmountWithUnitDisplayText(double amountInUnit, IotaUnits unit, boolean extended) {
        String result = createAmountDisplayText(amountInUnit, unit, extended);
        result += " " + unit.getUnit();
        return result;
    }

    /**
     * create amount text
     *
     * @param amountInUnit the amount in units
     * @param unit         the unit
     * @param extended     extended length
     * @return the target unit
     **/
    public static String createAmountDisplayText(double amountInUnit, IotaUnits unit, boolean extended) {
        DecimalFormat df;
        if (extended) df = new DecimalFormat("##0.##################");
        else
            df = new DecimalFormat("##0.##");

        String result = "";
        // display unit as integer if value is between 1-999 or in decimal format
        result += unit == IotaUnits.IOTA ? (long) amountInUnit : df.format(amountInUnit);
        return result;
    }

    /**
     * finds the optimal unit to display the specified amount in
     *
     * @param amount the amount
     * @return the optimal IotaUnit
     **/
    public static IotaUnits findOptimalIotaUnitToDisplay(long amount) {
        int length = String.valueOf(amount).length();

        if (amount < 0) {// do not count "-" sign
            length -= 1;
        }

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
