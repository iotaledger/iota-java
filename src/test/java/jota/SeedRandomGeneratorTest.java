package jota;

import jota.utils.Constants;
import jota.utils.InputValidator;
import jota.utils.SeedRandomGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by pinpong on 13.12.16.
 */
public class SeedRandomGeneratorTest {

    @Test
    public void shouldGenerateNewSeed() {

        String generatedSeed = SeedRandomGenerator.generateNewSeed();
        assertEquals(InputValidator.isAddress(generatedSeed), true);
        assertEquals(generatedSeed.length(), Constants.SEED_LENGTH_MAX);
    }
}
