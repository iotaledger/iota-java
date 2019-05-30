package org.iota.jota;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.iota.jota.pow.Kerl;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.utils.Converter;
import org.junit.jupiter.api.Test;

public class KerlTest {
    
    @Test
    public void shouldCreateValidHash1() {
        int[] trits = Converter.trits("GYOMKVTSNHVJNCNFBBAH9AAMXLPLLLROQY99QN9DLSJUHDPBLCFFAIQXZA9BKMBJCYSFHFPXAHDWZFEIZ");
        Kerl kerl = (Kerl) SpongeFactory.create(SpongeFactory.Mode.KERL);
        kerl.reset();
        kerl.absorb(trits, 0, trits.length);
        final int[] hashTrits = new int[trits.length];
        kerl.squeeze(hashTrits, 0, 243);
        String hash = Converter.trytes(hashTrits);
        assertEquals(hash, "OXJCNFHUNAHWDLKKPELTBFUCVW9KLXKOGWERKTJXQMXTKFKNWNNXYD9DMJJABSEIONOSJTTEVKVDQEWTW");
    }

    @Test
    public void shouldCreateValidHash2() {
        int[] trits = Converter.trits("9MIDYNHBWMBCXVDEFOFWINXTERALUKYYPPHKP9JJFGJEIUY9MUDVNFZHMMWZUYUSWAIOWEVTHNWMHANBH");
        Kerl kerl = (Kerl) SpongeFactory.create(SpongeFactory.Mode.KERL);
        kerl.reset();
        kerl.absorb(trits, 0, trits.length);
        final int[] hashTrits = new int[trits.length * 2];
        kerl.squeeze(hashTrits, 0, 243 * 2);
        String hash = Converter.trytes(hashTrits);
        assertEquals(hash, "G9JYBOMPUXHYHKSNRNMMSSZCSHOFYOYNZRSZMAAYWDYEIMVVOGKPJBVBM9TDPULSFUNMTVXRKFIDOHUXXVYDLFSZYZTWQYTE9SPYYWYTXJYQ9IFGYOLZXWZBKWZN9QOOTBQMWMUBLEWUEEASRHRTNIQWJQNDWRYLCA");
    }

    @Test
    public void shouldCreateValidHash3() {
        int[] trits = Converter.trits("G9JYBOMPUXHYHKSNRNMMSSZCSHOFYOYNZRSZMAAYWDYEIMVVOGKPJBVBM9TDPULSFUNMTVXRKFIDOHUXXVYDLFSZYZTWQYTE9SPYYWYTXJYQ9IFGYOLZXWZBKWZN9QOOTBQMWMUBLEWUEEASRHRTNIQWJQNDWRYLCA");
        Kerl kerl = (Kerl) SpongeFactory.create(SpongeFactory.Mode.KERL);
        kerl.reset();
        kerl.absorb(trits, 0, trits.length);
        final int[] hashTrits = new int[trits.length];
        kerl.squeeze(hashTrits, 0, 243 * 2);
        String hash = Converter.trytes(hashTrits);
        assertEquals(hash, "LUCKQVACOGBFYSPPVSSOXJEKNSQQRQKPZC9NXFSMQNRQCGGUL9OHVVKBDSKEQEBKXRNUJSRXYVHJTXBPDWQGNSCDCBAIRHAQCOWZEBSNHIJIGPZQITIBJQ9LNTDIBTCQ9EUWKHFLGFUVGGUWJONK9GBCDUIMAYMMQX");
    }
}