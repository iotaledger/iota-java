package jota;

import jota.utils.IotaUnitConverter;
import jota.utils.IotaUnits;
import org.hamcrest.core.IsNull;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * Created by pinpong on 30.11.16.
 */
public class IotaUnitConverterTest {

    @Test
    public void shouldConvertUnitItoKi() {
        assertThat(IotaUnitConverter.convertUnits(1000, IotaUnits.IOTA, IotaUnits.KILO_IOTA), IsNull.notNullValue());
    }

    @Test
    public void shouldConvertUnitKiToMi() {
        assertThat(IotaUnitConverter.convertUnits(1000, IotaUnits.KILO_IOTA, IotaUnits.MEGA_IOTA), IsNull.notNullValue());
    }

    @Test
    public void shouldConvertUnitMiToGi() {
        assertThat(IotaUnitConverter.convertUnits(1000, IotaUnits.MEGA_IOTA, IotaUnits.GIGA_IOTA), IsNull.notNullValue());
    }

    @Test
    public void shouldConvertUnitGiToTi() {
        assertThat(IotaUnitConverter.convertUnits(1000, IotaUnits.GIGA_IOTA, IotaUnits.TERRA_IOTA), IsNull.notNullValue());
    }

    @Test
    public void shouldConvertUnitTiToPi() {
        assertThat(IotaUnitConverter.convertUnits(1000, IotaUnits.TERRA_IOTA, IotaUnits.PETA_IOTA), IsNull.notNullValue());
    }
}
