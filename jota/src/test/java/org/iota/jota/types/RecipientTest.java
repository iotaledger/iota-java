package org.iota.jota.types;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipientTest {

    private Recipient testClass;

    @BeforeEach
    void setUp() {
        long value = 1L;
        String message = "TestMessage01";
        String tag = "TestTag01";
        String addresses01 = "TestAddresses01";
        testClass = new Recipient(value, message, tag, addresses01);
    }

    @Test
    void testToString() {
        String toStringOutput = testClass.toString();
        assertEquals("Recipient [value=1, message=TestMessage01, tag=TestTag01, address=[TestAddresses01]]", toStringOutput);
    }


    //***********************************************************
    //  hashCode tests
    //***********************************************************
    @Test
    void testHashCode01() {
        assertEquals(-974042593, testClass.hashCode());
    }

    @Test
    void testHashCode02() {
        Recipient otherRecipient = new Recipient(1L, "TestMessage01", "TestTag01", "TestAddresses01");
        assertEquals(testClass.hashCode(), otherRecipient.hashCode());
    }

    @Test
    void testHashCode03_otherValue() {
        Recipient otherRecipient = new Recipient(2L, "TestMessage01", "TestTag01", "TestAddresses01");
        assertNotEquals(testClass.hashCode(), otherRecipient.hashCode());
    }

    @Test
    void testHashCode04_otherMessage() {
        Recipient otherRecipient = new Recipient(1L, "TestMessage02", "TestTag01", "TestAddresses01");
        assertNotEquals(testClass.hashCode(), otherRecipient.hashCode());
    }

    @Test
    void testHashCode05_otherTag() {
        Recipient otherRecipient = new Recipient(1L, "TestMessage01", "TestTag02", "TestAddresses01");
        assertNotEquals(testClass.hashCode(), otherRecipient.hashCode());
    }

    @Test
    void testHashCode06_otherAddress() {
        Recipient otherRecipient = new Recipient(1L, "TestMessage01", "TestTag01", "TestAddresses02");
        assertNotEquals(testClass.hashCode(), otherRecipient.hashCode());
    }

    //***********************************************************
    //  equals tests
    //***********************************************************
    @Test
    void testEquals01() {
        assertTrue(testClass.equals(testClass));
    }

    @Test
    void testEquals02_sameParameterSettings() {
        Recipient otherRecipient = new Recipient(1L, "TestMessage01", "TestTag01", "TestAddresses01");
        assertTrue(testClass.equals(otherRecipient));
    }

    @Test
    void testEquals03_otherValue() {
        Recipient otherRecipient = new Recipient(2L, "TestMessage01", "TestTag01", "TestAddresses01");
        assertFalse(testClass.equals(otherRecipient));
    }

    @Test
    void testEquals04_otherMessage() {
        Recipient otherRecipient = new Recipient(1L, "TestMessage02", "TestTag01", "TestAddresses01");
        assertFalse(testClass.equals(otherRecipient));
    }

    @Test
    void testEquals05_otherTag() {
        Recipient otherRecipient = new Recipient(1L, "TestMessage01", "TestTag02", "TestAddresses01");
        assertFalse(testClass.equals(otherRecipient));
    }

    @Test
    void testEquals06_otherAddress() {
        Recipient otherRecipient = new Recipient(1L, "TestMessage01", "TestTag01", "TestAddresses02");
        assertFalse(testClass.equals(otherRecipient));
    }

    @Test
    void testEquals07_manyAddress01() {
        Recipient otherRecipient = new Recipient(1L, "TestMessage01", "TestTag01", "TestAddresses01", "TestAddresses02");
        assertFalse(testClass.equals(otherRecipient));
    }

    @Test
    void testEquals08_manyAddress02() {
        Recipient otherRecipient = new Recipient(1L, "TestMessage01", "TestTag01", "TestAddresses01", "TestAddresses01");
        assertFalse(testClass.equals(otherRecipient));
    }

    @Test
    void testEquals09_otherMessageIsNull() {
        Recipient otherRecipient = new Recipient(1L, null, "TestTag01", "TestAddresses01");
        assertFalse(testClass.equals(otherRecipient));
    }

    @Test
    void testEquals10_ownClassMessageIsNull() {
        Recipient ownRecipient = new Recipient(1L, null, "TestTag01", "TestAddresses01");
        Recipient otherRecipient = new Recipient(1L, "TestMessage01", "TestTag01", "TestAddresses01");
        assertFalse(ownRecipient.equals(otherRecipient));
    }

    @Test
    void testEquals10_BothMessageIsNull() {
        Recipient ownRecipient = new Recipient(1L, null, "TestTag01", "TestAddresses01");
        Recipient otherRecipient = new Recipient(1L, null, "TestTag01", "TestAddresses01");
        assertTrue(ownRecipient.equals(otherRecipient));
    }

    @Test
    void testEquals11_otherObjectIsNull() {
        Recipient otherRecipient = null;
        assertFalse(testClass.equals(otherRecipient));
    }

    @Test
    void testEquals12_otherClassIsDifferent() {
        String otherRecipient = "testString";
        assertFalse(testClass.equals(otherRecipient));
    }
}