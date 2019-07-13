package org.iota.jota;

import java.util.ArrayList;
import java.util.List;

import org.iota.jota.config.types.FileConfig;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.Converter;
import org.iota.jota.utils.Multisig;
import org.iota.jota.utils.Signing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IotaMultisigTest {

    private static final String TEST_SEED1 = "ABCDFG";
    private static final String TEST_SEED2 = "FDSAG";
    private static final String REMAINDER_ADDRESS = "NZRALDYNVGJWUVLKDWFKJVNYLWQGCWYCURJIIZRLJIKSAIVZSGEYKTZRDBGJLOA9AWYJQB9IPWRAKUC9FBDRZJZXZG";
    private static final String RECEIVE_ADDRESS = "ZGHXPZYDKXPEOSQTAQOIXEEI9K9YKFKCWKYYTYAUWXK9QZAVMJXWAIZABOXHHNNBJIEBEUQRTBWGLYMTXPENVCJZBX";
    private static final String TEST_TAG = "JOTASPAM9999999999999999999";

    private IotaAPI iotaClient;

    @BeforeEach
    public void createApiClientInstance() throws Exception {
        iotaClient = new IotaAPI.Builder().config(new FileConfig()).build();
    }

    @Test
    @Tag("IntegrationTest")
    public void basicMultiSigTest() throws ArgumentException {
        Multisig ms = new Multisig();

        // First co-signer uses security level 3 and index 0 for the private key
        String digestOne = ms.getDigest(TEST_SEED1, 3, 0);

        // We initiate the multisig address generation by absorbing the key digest
        String initiatedMultisigDigests = ms.addAddressDigest(digestOne, "");

        // Second cosigner also uses security level 3 and index 0  for the private key
        String digestTwo = ms.getDigest(TEST_SEED2, 3, 0);

        // Add the multisig by absorbing the second cosigners key digest
        String finalMultisigDigests = ms.addAddressDigest(digestTwo, initiatedMultisigDigests);

        // finally we generate the multisig address itself
        String multiSigAddress = ms.finalizeAddress(finalMultisigDigests);

        System.out.println("MultisigAddress = " + multiSigAddress);

        boolean isValidMultisigAddress = ms.validateAddress(multiSigAddress, new int[][]{Converter.trits(digestOne), Converter.trits(digestTwo)});

        System.out.println("Is a valid multisig address " + isValidMultisigAddress);

        assertTrue(isValidMultisigAddress, "Address is not a valid multisigAddress");
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new Transfer(RECEIVE_ADDRESS, 999, "", TEST_TAG));

        List<Transaction> trxs = iotaClient.initiateTransfer(6, Checksum.addChecksum(multiSigAddress), REMAINDER_ADDRESS, transfers, null, true);

        Bundle bundle = new Bundle(trxs, trxs.size());

        bundle = ms.addSignature(bundle, multiSigAddress, ms.getKey(TEST_SEED1, 0, 3));

        bundle = ms.addSignature(bundle, multiSigAddress, ms.getKey(TEST_SEED2, 0, 3));


        Signing sgn = new Signing(SpongeFactory.create(SpongeFactory.Mode.KERL));

        System.out.println(bundle.getTransactions().get(0).getBundle());
        boolean isValidSignature = sgn.validateSignatures(bundle, multiSigAddress);
        assertTrue(isValidSignature, "MultiSignature not valid");
        System.out.println("Result of multi-signature validation is " + isValidSignature);
    }
}
