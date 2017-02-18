package jota;

import jota.error.InvalidAddressException;
import jota.utils.Checksum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author pinpong
 */
public class ChecksumTest {

    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM = "RVORZ9SIIP9RCYMREUIXXVPQIPHVCNPQ9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVA";
    private static final String TEST_ADDRESS_WITH_CHECKSUM = "RVORZ9SIIP9RCYMREUIXXVPQIPHVCNPQ9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVAFOXM9MUBX";

    @Test
    public void shouldAddChecksum() throws InvalidAddressException {
        assertEquals(Checksum.addChecksum(TEST_ADDRESS_WITHOUT_CHECKSUM), TEST_ADDRESS_WITH_CHECKSUM);
    }
    
    @Test
    public void shouldRemoveChecksum() throws InvalidAddressException {
        assertEquals(Checksum.removeChecksum(TEST_ADDRESS_WITH_CHECKSUM), TEST_ADDRESS_WITHOUT_CHECKSUM);
    }

    @Test
    public void shouldIsValidChecksum() throws InvalidAddressException {
        assertEquals(Checksum.isValidChecksum(TEST_ADDRESS_WITH_CHECKSUM), true);
    }
}
