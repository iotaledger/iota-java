package org.iota.jota;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.iota.jota.utils.IotaUnitConverter;
import org.iota.jota.utils.IotaUnits;
import org.junit.jupiter.api.Test;

/**
 * @author pinpong
 */
public class IotaUnitConverterTest {

    @Test
    public void shouldConvertUnitItoKi() {
        assertEquals(IotaUnitConverter.convertUnits(1000, IotaUnits.IOTA, IotaUnits.KILO_IOTA), 1);
    }

    @Test
    public void shouldConvertUnitKiToMi() {
        assertEquals(IotaUnitConverter.convertUnits(1000, IotaUnits.KILO_IOTA, IotaUnits.MEGA_IOTA), 1);
    }

    @Test
    public void shouldConvertUnitMiToGi() {
        assertEquals(IotaUnitConverter.convertUnits(1000, IotaUnits.MEGA_IOTA, IotaUnits.GIGA_IOTA), 1);
    }

    @Test
    public void shouldConvertUnitGiToTi() {
        assertEquals(IotaUnitConverter.convertUnits(1000, IotaUnits.GIGA_IOTA, IotaUnits.TERA_IOTA), 1);
    }

    @Test
    public void shouldConvertUnitTiToPi() {
        assertEquals(IotaUnitConverter.convertUnits(1000, IotaUnits.TERA_IOTA, IotaUnits.PETA_IOTA), 1);
    }

    @Test
    public void shouldFindOptimizeUnitToDisplay() {
        assertEquals(IotaUnitConverter.findOptimalIotaUnitToDisplay(1), IotaUnits.IOTA);
        assertEquals(IotaUnitConverter.findOptimalIotaUnitToDisplay(1000), IotaUnits.KILO_IOTA);
        assertEquals(IotaUnitConverter.findOptimalIotaUnitToDisplay(1000000), IotaUnits.MEGA_IOTA);
        assertEquals(IotaUnitConverter.findOptimalIotaUnitToDisplay(1000000000), IotaUnits.GIGA_IOTA);
        assertEquals(IotaUnitConverter.findOptimalIotaUnitToDisplay(1000000000000L), IotaUnits.TERA_IOTA);
        assertEquals(IotaUnitConverter.findOptimalIotaUnitToDisplay(1000000000000000L), IotaUnits.PETA_IOTA);
    }

    @Test
    public void shouldConvertRawIotaAmountToDisplayText() {
        assertEquals(IotaUnitConverter.convertRawIotaAmountToDisplayText(1, false), "1 i");
        assertEquals(IotaUnitConverter.convertRawIotaAmountToDisplayText(1000, false), "1 Ki");
        assertEquals(IotaUnitConverter.convertRawIotaAmountToDisplayText(1000000, false), "1 Mi");
        assertEquals(IotaUnitConverter.convertRawIotaAmountToDisplayText(1000000000, false), "1 Gi");
        assertEquals(IotaUnitConverter.convertRawIotaAmountToDisplayText(1000000000000L, false), "1 Ti");
        assertEquals(IotaUnitConverter.convertRawIotaAmountToDisplayText(1000000000000000L, false), "1 Pi");
    }
}
