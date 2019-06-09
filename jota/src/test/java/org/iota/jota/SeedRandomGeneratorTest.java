package org.iota.jota;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;
import org.iota.jota.utils.SeedRandomGenerator;
import org.junit.jupiter.api.Test;

/**
 * @author pinpong
 */
public class SeedRandomGeneratorTest {

    @Test
    public void shouldGenerateNewSeed() {
        String generatedSeed = SeedRandomGenerator.generateNewSeed();
        assertTrue(InputValidator.isValidSeed(generatedSeed));
        assertEquals(generatedSeed.length(), Constants.SEED_LENGTH_MAX);
    }
}
