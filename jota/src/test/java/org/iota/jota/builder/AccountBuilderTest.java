package org.iota.jota.builder;

import org.iota.jota.account.seedprovider.SeedProvider;
import org.iota.jota.account.seedprovider.SeedProviderImpl;
import org.iota.jota.types.Seed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountBuilderTest {

    private AccountBuilder testClass = null;

    @BeforeEach
    void setUp(){
        String tempSeed = "TESTSEEDAAAAAAAAAAABBBBBBBBBBBBCCCCCCCCCCCCDDDDDDDDDDDDEEEEEEEEEEEEEEFFFFFFFGGGG9";
        testClass = new AccountBuilder(tempSeed);
    }

    @Test
    void mwm() {
        AccountBuilder resultAB = testClass.mwm(0);
        assertEquals(0,resultAB.getMwm());

        resultAB = testClass.mwm(25);
        assertEquals(25,resultAB.getMwm());
        assertNotEquals(10,resultAB.getMwm());
    }

    @Test
    void depth() {
        AccountBuilder resultAB = testClass.depth(0);
        assertEquals(0,resultAB.getDepth());

        resultAB = testClass.depth(25);
        assertEquals(25,resultAB.getDepth());
        assertNotEquals(10,resultAB.getDepth());
    }
}