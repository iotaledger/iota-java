package jota;

import jota.utils.IotaAPIUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Adrian on 02.12.2016.
 */
public class AddressGenerationTest {
    private static String TEST_SEED = "ZEB99QTOMYDSKIFCXTLTVSWQFKO9CRKQKMRDR9HWVOVSGZMWPFQIMSCXXWUULHD9MZKMFJZAYZHZYA9VZ";
    private static String FIRST_ADDRESS = "LCZXWAQUHBXST9IEPPMJICTWLKJA9HVASXWDIRCVNM9TUAGZY9SRRJLZMZQIZKBAESXXNABFATUAYQYYW";

    @Test
    public void shouldAddChecksum() {
        assertEquals(IotaAPIUtils.getNewAddress(TEST_SEED,0),FIRST_ADDRESS);
    }

}
