package jota;

import jota.utils.TrytesConverter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by pinpong on 01.12.16.
 */
public class TrytesConverterTest {

    @Test
    public void shouldConvertStringToTrytes() {
        assertEquals(TrytesConverter.toTrytes("Z"), "IC");
    }
    @Test
    public void shouldConvertTrytesToString() {
        assertEquals(TrytesConverter.toString("IC"), "Z");
    }

}
