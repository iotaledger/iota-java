package org.iota.jota;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.TrytesConverter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.RandomStringUtils;

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