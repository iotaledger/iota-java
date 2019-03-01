package org.iota.jota.account.deposits;

import static org.junit.Assert.*;

import java.util.Date;

import org.iota.jota.account.deposits.methods.DepositFactory;
import org.iota.jota.account.deposits.methods.MagnetMethod;
import org.iota.jota.account.deposits.methods.QRMethod;
import org.iota.jota.types.Hash;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DepositFactoryTest extends DepositTest {
    
    private static DepositConditions deposit;
    
    DepositFactory factory;
    
    @BeforeClass
    public static void setUp() {
        deposit = new DepositConditions(
                new DepositRequest(new Date(0), false, 1), 
                new Hash(depositAddress));
    }
    
    @Before
    public void before() {
        factory = DepositFactory.get();
        factory.addMethod(new QRMethod());
        factory.addMethod(new MagnetMethod());
    }
    
    @Test
    public void canMakeMagnet() {
        assertNotNull(factory.build(deposit, MagnetMethod.class));
    }
    
    @Test
    public void canMakeQr() {
        assertNotNull(factory.build(deposit, QRMethod.class));
    }

}
