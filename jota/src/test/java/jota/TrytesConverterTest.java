package jota;

import jota.error.ArgumentException;
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
        try {
            assertEquals(TrytesConverter.trytesToAscii("IC"), "Z");
            assertEquals(TrytesConverter.trytesToAscii("TBYBCCKBEATBYBCCKB"), "JOTA JOTA");
        } catch (ArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldConvertBackAndForth() {
        String str = RandomStringUtils.randomAlphabetic(1000).toUpperCase();
        String back;
        try {
            back = TrytesConverter.trytesToAscii(TrytesConverter.asciiToTrytes(str));
            assertTrue(str.equals(back));
        } catch (ArgumentException e) {
            e.printStackTrace();
        }
        
    }
}