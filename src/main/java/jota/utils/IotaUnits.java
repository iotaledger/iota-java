package jota.utils;

/**
 * Created by pinpong on 30.11.16.
 */

/**
 * Table of IOTA units based off of the standard system of Units
 **/
public enum IotaUnits {
    IOTA("i", 0),
    KILO_IOTA("Ki", 3),
    MEGA_IOTA("Mi", 6),
    GIGA_IOTA("Gi", 9),
    TERA_IOTA("Ti", 12),
    PETA_IOTA("Pi", 15);

    private String unit;
    private long value;

    IotaUnits(String unit, long value) {
        this.unit = unit;
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public long getValue() {
        return value;
    }
}
