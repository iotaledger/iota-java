package jota;

import jota.utils.Checksum;
import jota.utils.InputValidator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by pinpong on 02.12.16.
 */
public class ChecksumTest {

    private static final String TEST_ADDRESS = "RVORZ9SIIP9RCYMREUIXXVPQIPHVCNPQ9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVAZETAIRPTM";

    @Test
    public void shouldAddChecksum() {
        assertEquals(Checksum.addChecksum(TEST_ADDRESS), true);
    }

    @Test
    public void shouldRemoveChecksum() {
        assertEquals(Checksum.addChecksum(TEST_ADDRESS), Checksum.isValidChecksum(Checksum.addChecksum(TEST_ADDRESS)));
    }
}
