package jota;

import jota.utils.TrytesConverter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author pinpong
 */
public class TrytesConverterTest {

    @Test
    public void shouldConvertStringToTrytes() {
        assertEquals(TrytesConverter.asciiToTrytes("Z"), "IC");
        assertEquals(TrytesConverter.asciiToTrytes("JOTA JOTA"), "TBYBCCKBEATBYBCCKB");
    }

    @Test
    public void shouldConvertTrytesToString() {
        assertEquals(TrytesConverter.trytesToAscii("IC"), "Z");
        assertEquals(TrytesConverter.trytesToAscii("TBYBCCKBEATBYBCCKB"), "JOTA JOTA");
    }

    @Test
    public void shouldConvertBackAndForth() {
        String str = RandomStringUtils.randomAlphabetic(1000).toUpperCase();
        String back = TrytesConverter.trytesToAscii(TrytesConverter.asciiToTrytes(str));
        assertTrue(str.equals(back));
    }
}