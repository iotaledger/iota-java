package account.deposits;

import static org.junit.Assert.*;

import org.iota.jota.account.deposits.methods.DepositFactory;
import org.iota.jota.account.deposits.methods.MagnetMethod;
import org.iota.jota.account.deposits.methods.QRMethod;
import org.junit.BeforeClass;
import org.junit.Test;

public class DepositFactoryTest {
    
    DepositFactory factory;
    
    @BeforeClass
    public void before() {
        factory = DepositFactory.get();
        factory.addMethod(new QRMethod());
        factory.addMethod(new MagnetMethod());
    }
    
    @Test
    public void canMakeMagnet() {
        assertNotNull(factory.build(null, MagnetMethod.class));
    }
    
    @Test
    public void canMakeQr() {
        assertNotNull(factory.build(null, QRMethod.class));
    }

}
