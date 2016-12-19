package jota;

import jota.utils.TrytesConverter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;

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
    
    @Test
    public void shouldConvertBackAndForth() {
        String str = RandomStringUtils.randomAlphabetic(1000).toUpperCase();
        System.err.println(str);
        String back = TrytesConverter.toString(TrytesConverter.toTrytes(str));
       
        assertTrue(str.equals(back));
    }

}
