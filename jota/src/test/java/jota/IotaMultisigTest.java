package jota;

import jota.category.IntegrationTest;
import jota.error.ArgumentException;
import jota.model.Bundle;
import jota.model.Transaction;
import jota.model.Transfer;
import jota.pow.SpongeFactory;
import jota.utils.Converter;
import jota.utils.Multisig;
import jota.utils.Signing;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Adrian on 18.02.2017.
 */
public class IotaMultisigTest {

    private static final String TEST_SEED1 = "ABCDFG";
    private static final String TEST_SEED2 = "FDSAG";
    private static final String REMAINDER_ADDRESS = "NZRALDYNVGJWUVLKDWFKJVNYLWQGCWYCURJIIZRLJIKSAIVZSGEYKTZRDBGJLOA9AWYJQB9IPWRAKUC9FBDRZJZXZG";
    private static final String RECEIVE_ADDRESS = "ZGHXPZYDKXPEOSQTAQOIXEEI9K9YKFKCWKYYTYAUWXK9QZAVMJXWAIZABOXHHNNBJIEBEUQRTBWGLYMTX";
    private static final String TEST_TAG = "JOTASPAM9999999999999999999";

    private IotaAPI iotaClient;

    @Before
    public void createApiClientInstance() {
        iotaClient = new IotaAPI.Builder().build();
    }

    @Test
    @Category(IntegrationTest.class)
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

        assertTrue("Address is not a valid multisigAddress", isValidMultisigAddress);
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new Transfer(RECEIVE_ADDRESS, 999, "", TEST_TAG));

        List<Transaction> trxs = iotaClient.initiateTransfer(6, multiSigAddress, REMAINDER_ADDRESS, transfers, true);

        Bundle bundle = new Bundle(trxs, trxs.size());

        bundle = ms.addSignature(bundle, multiSigAddress, ms.getKey(TEST_SEED1, 0, 3));

        bundle = ms.addSignature(bundle, multiSigAddress, ms.getKey(TEST_SEED2, 0, 3));


        Signing sgn = new Signing(SpongeFactory.create(SpongeFactory.Mode.KERL));

        boolean isValidSignature = sgn.validateSignatures(bundle, multiSigAddress);
        assertTrue("MultiSignature not valid", isValidSignature);
        System.out.println("Result of multi-signature validation is " + isValidSignature);
    }
}
