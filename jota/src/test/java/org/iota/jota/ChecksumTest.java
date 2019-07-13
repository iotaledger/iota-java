package org.iota.jota;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.Checksum;
import org.junit.jupiter.api.Test;

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
        assertTrue(Checksum.isValidChecksum(TEST_ADDRESS_WITH_CHECKSUM));
    }
}
