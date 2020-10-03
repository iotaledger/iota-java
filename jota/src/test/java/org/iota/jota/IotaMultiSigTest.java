package org.iota.jota;

import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.Multisig;
import org.iota.jota.utils.Signing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IotaMultiSigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IotaMultiSigTest.class);

    private static final String TEST_SEED1 = "ABCDFG";
    private static final String TEST_SEED2 = "FDSAG";
    private static final String REMAINDER_ADDRESS = "NZRALDYNVGJWUVLKDWFKJVNYLWQGCWYCURJIIZRLJIKSAIVZSGEYKTZRDBGJLOA9AWYJQB9IPWRAKUC9FBDRZJZXZG";
    private static final String RECEIVE_ADDRESS_CUT = "ZGHXPZYDKXPEOSQTAQOIXEEI9K9YKFKCWKYYTYAUWXK9QZAVMJXWAIZABOXHHNNBJIEBEUQRTBWGLYMTX";
    private static final String SECOND_RECEIVE_ADDRESS_CUT = "JYQOVXIR9GDQMWXBYVZW9FYVQJWXLCXXCYJHOYRVW9NKBNZEVYITXXSRFANMIUNOSVEGUMETUZC9EAPOX";
    private static final String RECEIVE_ADDRESS = "ZGHXPZYDKXPEOSQTAQOIXEEI9K9YKFKCWKYYTYAUWXK9QZAVMJXWAIZABOXHHNNBJIEBEUQRTBWGLYMTXPENVCJZBX";
    private static final String TEST_TAG = "JOTA9MULTI9SIG9TEST99999999";
    private static final String TEST_TRANSACTION = "999999999999999999999999999999999999999999999999999999999999999999999999999999999";
    private static final long TEST_TIMESTAMP = 999999999;
    private static final String TEST_NONCE = "999999999999999999999999999";
    private static final String TEST_OBSOLETE_TAG = "LZUA9MULTI9SIG9TEST99999999";
    private static final String TEST_BUNDLE = "GFOPYAQYOAEDTLIJAVUBDRFBSLGLFGCKNQDHIDYGPKY9BWJVHPXECHNJJBFEUYTRKBDYG9VLRPUEEYXF9";
    private static final String TEST_BUNDLE_MIN = "HZAVEDUTZKEPKJDPUTGDFXWZYGDZTWLKCSUBHTCFWHHFBK9KSZ9BDPG9YUDDCOHZKJSWZLFOTPLAPVGZD";

    @Mock
    private IotaAPI iotaClient;

    @Test
    public void basicMultiSigTestMaxSec() throws ArgumentException {
        final List<Transaction> transactionsMax = Arrays.asList(
                new Transaction(TEST_TRANSACTION, 0, 7, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, RECEIVE_ADDRESS_CUT, 999,
                        TEST_BUNDLE, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 0, 7, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, RECEIVE_ADDRESS_CUT, 999,
                        TEST_BUNDLE, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 1, 7, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, SECOND_RECEIVE_ADDRESS_CUT, -1000,
                        TEST_BUNDLE, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 2, 7, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, SECOND_RECEIVE_ADDRESS_CUT, 0,
                        TEST_BUNDLE, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 3, 7, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, SECOND_RECEIVE_ADDRESS_CUT, 0,
                        TEST_BUNDLE, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 4, 7, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, SECOND_RECEIVE_ADDRESS_CUT, 0,
                        TEST_BUNDLE, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 5, 7, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, SECOND_RECEIVE_ADDRESS_CUT, 0,
                        TEST_BUNDLE, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 6, 7, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, SECOND_RECEIVE_ADDRESS_CUT, 0,
                        TEST_BUNDLE, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 7, 7, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, REMAINDER_ADDRESS, 1,
                        TEST_BUNDLE, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP));

        when(iotaClient.initiateTransfer(eq(6), any(), eq(REMAINDER_ADDRESS), anyList(), isNull(), eq(true))).thenReturn(transactionsMax);

        Multisig ms = new Multisig();

        // First co-signer uses security level 3 and index 0 for the private key
        String digestOne = ms.getDigest(TEST_SEED1, 3, 0);

        // We initiate the multiSig address generation by absorbing the key digest
        String initiatedMultiSigDigests = ms.addAddressDigest(digestOne, "");

        // Second cosigner also uses security level 3 and index 0  for the private key
        String digestTwo = ms.getDigest(TEST_SEED2, 3, 0);

        // Add the multiSig by absorbing the second cosigners key digest
        String finalMultiSigDigests = ms.addAddressDigest(digestTwo, initiatedMultiSigDigests);

        // finally we generate the multiSig address itself
        String multiSigAddress = ms.finalizeAddress(finalMultiSigDigests);

        LOGGER.debug("MultiSigAddress = {}", multiSigAddress);

        boolean isValidMultiSigAddress = ms.validateAddress(multiSigAddress, new int[][]{Converter.trits(digestOne), Converter.trits(digestTwo)});

        LOGGER.debug("Is a valid multiSig address = {}", isValidMultiSigAddress);

        assertTrue(isValidMultiSigAddress, "Address is not a valid multiSigAddress");
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new Transfer(RECEIVE_ADDRESS, 999, "", TEST_TAG));

        List<Transaction> transactions = iotaClient.initiateTransfer(6, Checksum.addChecksum(multiSigAddress), REMAINDER_ADDRESS, transfers, null, true);

        Bundle bundle = new Bundle(transactions, transactions.size());

        bundle = ms.addSignature(bundle, multiSigAddress, ms.getKey(TEST_SEED1, 0, 3));

        bundle = ms.addSignature(bundle, multiSigAddress, ms.getKey(TEST_SEED2, 0, 3));

        Signing sgn = new Signing(SpongeFactory.create(SpongeFactory.Mode.KERL));

        LOGGER.debug("Bundle from transaction {}", bundle.getTransactions().get(0).getBundle());
        boolean isValidSignature = sgn.validateSignatures(bundle, multiSigAddress);
        assertTrue(isValidSignature, "MultiSignature not valid");
        LOGGER.debug("Result of multi-signature validation is {}", isValidSignature);
    }

    @Test
    public void basicMultiSigTestMinSec() throws ArgumentException {
        final List<Transaction> transactionsMin = Arrays.asList(
                new Transaction(TEST_TRANSACTION, 0, 3, TEST_NONCE, null, "QWTA9MULTI9SIG9TEST99999999",
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, "ZGHXPZYDKXPEOSQTAQOIXEEI9K9YKFKCWKYYTYAUWXK9QZAVMJXWAIZABOXHHNNBJIEBEUQRTBWGLYMTX", 999,
                        TEST_BUNDLE_MIN, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 1, 3, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, "TKHMNTEWMUKX9WZVWGEAXICAYZHHJHAYVTFKSSZRAGSE99CGWXUQVXWIEGELXZCEXWFTQRK9DGZNFERDX", -1000,
                        TEST_BUNDLE_MIN, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 2, 3, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, "TKHMNTEWMUKX9WZVWGEAXICAYZHHJHAYVTFKSSZRAGSE99CGWXUQVXWIEGELXZCEXWFTQRK9DGZNFERDX", 0,
                        TEST_BUNDLE_MIN, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP),
                new Transaction(TEST_TRANSACTION, 3, 3, TEST_NONCE, null, TEST_OBSOLETE_TAG,
                        Instant.now().getEpochSecond(), TEST_TRANSACTION, TEST_TRANSACTION, "NZRALDYNVGJWUVLKDWFKJVNYLWQGCWYCURJIIZRLJIKSAIVZSGEYKTZRDBGJLOA9AWYJQB9IPWRAKUC9FBDRZJZXZG", 1,
                        TEST_BUNDLE_MIN, TEST_TAG, TEST_TIMESTAMP, TEST_TIMESTAMP, TEST_TIMESTAMP));

        when(iotaClient.initiateTransfer(eq(2), any(), eq(REMAINDER_ADDRESS), anyList(), isNull(), eq(true))).thenReturn(transactionsMin);

        Multisig ms = new Multisig();

        // First co-signer uses security level 1 and index 0 for the private key
        String digestOne = ms.getDigest(TEST_SEED1, 1, 0);

        // We initiate the multiSig address generation by absorbing the key digest
        String initiatedMultiSigDigests = ms.addAddressDigest(digestOne, "");

        // Second cosigner also uses security level 1 and index 0  for the private key
        String digestTwo = ms.getDigest(TEST_SEED2, 1, 0);

        // Add the multiSig by absorbing the second cosigners key digest
        String finalMultiSigDigests = ms.addAddressDigest(digestTwo, initiatedMultiSigDigests);

        // finally we generate the multiSig address itself
        String multiSigAddress = ms.finalizeAddress(finalMultiSigDigests);

        LOGGER.debug("MultiSigAddress = {}", multiSigAddress);

        boolean isValidMultiSigAddress = ms.validateAddress(multiSigAddress, new int[][]{Converter.trits(digestOne), Converter.trits(digestTwo)});

        LOGGER.debug("Is a valid multiSig address = {}", isValidMultiSigAddress);

        assertTrue(isValidMultiSigAddress, "Address is not a valid multiSigAddress");
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new Transfer(RECEIVE_ADDRESS, 999, "", TEST_TAG));

        List<Transaction> transactions = iotaClient.initiateTransfer(2, Checksum.addChecksum(multiSigAddress), REMAINDER_ADDRESS, transfers, null, true);

        Bundle bundle = new Bundle(transactions, transactions.size());

        bundle = ms.addSignature(bundle, multiSigAddress, ms.getKey(TEST_SEED1, 0, 1));

        bundle = ms.addSignature(bundle, multiSigAddress, ms.getKey(TEST_SEED2, 0, 1));

        Signing sgn = new Signing(SpongeFactory.create(SpongeFactory.Mode.KERL));

        LOGGER.debug("Bundle from transaction {}", bundle.getTransactions().get(0).getBundle());
        boolean isValidSignature = sgn.validateSignatures(bundle, multiSigAddress);
        assertTrue(isValidSignature, "MultiSignature not valid");
        LOGGER.debug("Result of multi-signature validation is {}", isValidSignature);
    }
}
