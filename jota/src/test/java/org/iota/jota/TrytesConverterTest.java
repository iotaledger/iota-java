package org.iota.jota;

import org.apache.commons.lang.RandomStringUtils;
import org.iota.jota.utils.TrytesConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals(str, back);
    }
}