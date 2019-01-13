package org.iota.jota.account.deposits;

import static org.junit.Assert.*;

import java.util.Date;

import org.iota.jota.account.deposits.DepositConditions;
import org.iota.jota.account.deposits.DepositRequest;
import org.iota.jota.account.deposits.methods.MagnetMethod;
import org.iota.jota.types.Hash;
import org.junit.Before;
import org.junit.Test;

public class MagnetTest extends DepositTest {
    
    private static final long TIME = 0;
    private static final boolean MULTI = false;
    private static final long AMOUNT = 5;
    
    private static final String MAGNET = "iota://" + DepositTest.depositAddress + "/?"
            + "t=" + TIME + "&"
            + "m=" + MULTI + "&"
            + "am=" + AMOUNT;
    
    private MagnetMethod method;
    
    private DepositConditions conditions;

    @Before
    public void setUp() throws Exception {
        method = new MagnetMethod();
        
        DepositRequest request = new DepositRequest(new Date(TIME), MULTI, AMOUNT);
        conditions = new DepositConditions(request, new Hash(DepositTest.depositAddress));
    }

    @Test
    public void buildMagnet() {
        String magnet = method.build(conditions);
        
        assertEquals(MagnetTest.MAGNET, magnet);
    }
    
    @Test
    public void readMagnet() {
        DepositConditions request = method.parse(MAGNET);
        assertEquals(conditions, request);
    }

}
