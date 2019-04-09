package org.iota.jota;

import static org.junit.Assert.assertEquals;

import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;
import org.iota.jota.utils.SeedRandomGenerator;
import org.junit.Test;

/**
 * @author pinpong
 */
public class SeedRandomGeneratorTest {

    @Test
    public void shouldGenerateNewSeed() {
        String generatedSeed = SeedRandomGenerator.generateNewSeed();
        assertEquals(InputValidator.isSeed(generatedSeed), true);
        assertEquals(generatedSeed.length(), Constants.SEED_LENGTH_MAX);
    }
}
