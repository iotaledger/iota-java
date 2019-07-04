package org.iota.jota.account.deposits;

import java.util.Date;

import org.iota.jota.account.deposits.methods.DepositFactory;
import org.iota.jota.account.deposits.methods.MagnetMethod;
import org.iota.jota.account.deposits.methods.QRMethod;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DepositFactoryTest extends DepositTest {
    
    private static ConditionalDepositAddress deposit;
    
    DepositFactory factory;
    
    @BeforeAll
    public static void setUp() {
        deposit = new ConditionalDepositAddress(
                new DepositRequest(new Date(0), false, 1), 
                depositAddress);
    }
    
    @BeforeEach
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
