package org.iota.jota.account.deposits.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.iota.jota.account.deposits.ConditionalDepositAddress;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.DepositTest;
import org.iota.jota.account.errors.MagnetError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MagnetTest extends DepositTest {

    private static final long TIME = 0;
    private static final boolean MULTI = false;
    private static final long AMOUNT = 5;

    private static final String MAGNET_CHECKSUM = "UDJPO99SI";

    private static final String MAGNET = "iota://" + DepositTest.depositAddress.getHash() + MAGNET_CHECKSUM + "/?"
            + MagnetMethod.CONDITION_EXPIRES + "=" + TIME + "&"
            + MagnetMethod.CONDITION_MULTI_USE + "=" + MULTI + "&"
            + MagnetMethod.CONDITION_AMOUNT + "=" + AMOUNT;

    private MagnetMethod method;

    private ConditionalDepositAddress conditions;

    @BeforeEach
    public void setUp() throws Exception {
        method = new MagnetMethod();

        DepositRequest request = new DepositRequest(new Date(TIME), MULTI, AMOUNT);
        conditions = new ConditionalDepositAddress(request, DepositTest.depositAddress);
    }

    @Test
    public void buildMagnet() {
        String magnet = method.build(conditions);

        assertEquals(MagnetTest.MAGNET, magnet);
    }
    
    @Test
    public void readGoMagnet() {
        // GO and JS allow 1 instead of true
        String magnet = "iota://BWNYWGULIIAVRYOOFWZTSDFXFPRCFF9YEHGVBOORLGCPCJSKTHU9OKESUGZGWZXZZDLESFPPTGEHVKTTXG9BQLSIGP/?timeout_at=5174418337&multi_use=1&expected_amount=0";
        ConditionalDepositAddress cda = DepositFactory.get().parse(magnet, MagnetMethod.class);
        
        assertEquals(cda.getRequest().getTimeOut().getTime(), 5174418337l);
        assertEquals(cda.getRequest().getExpectedAmount(), 0);
        assertEquals(cda.getRequest().getMultiUse(), true);
    }

    @Test
    public void readMagnet() {
        ConditionalDepositAddress request = method.parse(MAGNET);

        assertEquals(conditions, request);
        assertEquals(conditions.getDepositAddress().getHashCheckSum(), DepositTest.depositAddress.getHashCheckSum());
    }

    @Test
    public void magnetChecksum() {
        String checksum = method.magnetChecksum(DepositTest.depositAddress.getHash(),
                TIME, MULTI, AMOUNT);

        assertEquals(MAGNET_CHECKSUM, checksum, "Checksum should be equal to the pre-generated one");
    }

    @Test
    public void removeAmountDefaultResultsInWrongChecksum() {
        assertThrows(MagnetError.class, () -> method.parse(MAGNET.substring(0, MAGNET.length() - 4)),
                "Invalid magnet should throw exception");
    }
}
