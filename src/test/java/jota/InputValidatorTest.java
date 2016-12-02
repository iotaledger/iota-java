package jota;

import jota.utils.InputValidator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by pinpong on 02.12.16.
 */
public class InputValidatorTest {

    private static final String TEST_ADDRESS = "RVORZ9SIIP9RCYMREUIXXVPQIPHVCNPQ9HZWYKFWYWZRE9JQKG9REPKIASHUUECPSQO9JT9XNMVKWYGVAZETAIRPTM";

    @Test
    public void shouldIsAddress() {
        assertEquals(InputValidator.isAddress(TEST_ADDRESS), true);
    }

    @Test
    public void shouldCheckAddress() {
        assertEquals(InputValidator.checkAddress(TEST_ADDRESS), true);
    }

    @Test
    public void shouldIsTrytes() {
        assertEquals(InputValidator.isTrytes(TEST_ADDRESS, TEST_ADDRESS.length()), true);
    }
}
