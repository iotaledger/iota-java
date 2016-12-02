package jota;

import jota.utils.Checksum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by pinpong on 02.12.16.
 */
public class ChecksumTest {

    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM = "PNGMCSNRCTRHCHPXYTPKEJYPCOWKOMRXZFHH9N9VDIKMNVAZCMIYRHVJIAZARZTUETJVFDMBEBIQE9QTHBFWDAOEFA";
    private static final String TEST_ADDRESS_WITH_CHECKSUM = "PNGMCSNRCTRHCHPXYTPKEJYPCOWKOMRXZFHH9N9VDIKMNVAZCMIYRHVJIAZARZTUETJVFDMBEBIQE9QTHBFWDAOEFA";

    @Test
    public void shouldAddChecksum() {
        assertEquals(Checksum.addChecksum(TEST_ADDRESS_WITHOUT_CHECKSUM), TEST_ADDRESS_WITH_CHECKSUM);
    }

    @Test
    public void shouldRemoveChecksum() {
        assertEquals(Checksum.removeChecksum(TEST_ADDRESS_WITH_CHECKSUM), TEST_ADDRESS_WITHOUT_CHECKSUM);
    }

    @Test
    public void shouldIsValidChecksum() {
        assertEquals(Checksum.isValidChecksum(TEST_ADDRESS_WITH_CHECKSUM), true);
    }
}
