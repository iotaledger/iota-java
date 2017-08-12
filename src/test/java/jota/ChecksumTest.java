package jota;

import jota.error.InvalidAddressException;
import jota.utils.Checksum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author pinpong
 */
public class ChecksumTest {

    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM = "EUHMAFIYBYZOXAVQQYRQ9RCNMTYX9KNEZFWXYMQIYPSRZRVDOLXDPUEARYPTWSZCAXJLXRYUUQKSHIJYZ";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM2 = "P9UDUZMN9DEXCRQEKLJYSBSBZFCHOBPJSDKMLCCVJDOVOFDWMNBZRIRRZJGINOUMPJBMYYZEGRTIDUABD";
    private static final String TEST_ADDRESS_WITH_CHECKSUM = "EUHMAFIYBYZOXAVQQYRQ9RCNMTYX9KNEZFWXYMQIYPSRZRVDOLXDPUEARYPTWSZCAXJLXRYUUQKSHIJYZICCXCXUHX";
    private static final String TEST_ADDRESS_WITH_CHECKSUM2 = "P9UDUZMN9DEXCRQEKLJYSBSBZFCHOBPJSDKMLCCVJDOVOFDWMNBZRIRRZJGINOUMPJBMYYZEGRTIDUABDODCNSCYJD";

    @Test
    public void shouldAddChecksum() throws InvalidAddressException {
        assertEquals(Checksum.addChecksum(TEST_ADDRESS_WITHOUT_CHECKSUM2), TEST_ADDRESS_WITH_CHECKSUM2);
    }

    @Test
    public void shouldRemoveChecksum() throws InvalidAddressException {
        assertEquals(Checksum.removeChecksum(TEST_ADDRESS_WITH_CHECKSUM2), TEST_ADDRESS_WITHOUT_CHECKSUM2);
    }

    @Test
    public void shouldIsValidChecksum() throws InvalidAddressException {
        assertEquals(Checksum.isValidChecksum(TEST_ADDRESS_WITH_CHECKSUM2), true);
    }
}
