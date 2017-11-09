package jota;

import jota.error.ArgumentException;
import jota.utils.Checksum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author pinpong
 */
public class ChecksumTest {

    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZC";
    private static final String TEST_ADDRESS_WITH_CHECKSUM = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC";

    @Test
    public void shouldAddChecksum() throws ArgumentException {
        assertEquals(Checksum.addChecksum(TEST_ADDRESS_WITHOUT_CHECKSUM), TEST_ADDRESS_WITH_CHECKSUM);
    }

    @Test
    public void shouldRemoveChecksum() throws ArgumentException {
        assertEquals(Checksum.removeChecksum(TEST_ADDRESS_WITH_CHECKSUM), TEST_ADDRESS_WITHOUT_CHECKSUM);
    }

    @Test
    public void shouldIsValidChecksum() throws ArgumentException {
        assertEquals(Checksum.isValidChecksum(TEST_ADDRESS_WITH_CHECKSUM), true);
    }
}
