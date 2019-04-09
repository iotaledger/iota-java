package org.iota.jota.utils;

/**
 * Table of IOTA units based off of the standard system of Units.
 *
 * @author pinpong
 **/
public enum IotaUnits {

    IOTA("i", 0),
    KILO_IOTA("Ki", 3), // 10^3
    MEGA_IOTA("Mi", 6), // 10^6
    GIGA_IOTA("Gi", 9), // 10^9
    TERA_IOTA("Ti", 12), // 10^12
    PETA_IOTA("Pi", 15); // 10^15

    private String unit;
    private long value;

    /**
     * Initializes a new instance of the IotaUnit class.
     */
    IotaUnits(String unit, long value) {
        this.unit = unit;
        this.value = value;
    }

    /**
     * Gets the unit.
     *
     * @return The IOTA Unit.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Gets the value.
     *
     * @return The value.
     */
    public long getValue() {
        return value;
    }
}
