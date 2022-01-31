package org.iota.jota;

import org.iota.jota.config.types.FileConfig;
import org.iota.jota.dto.response.AddNeighborsResponse;
import org.iota.jota.dto.response.FindTransactionResponse;
import org.iota.jota.dto.response.GetBalancesResponse;
import org.iota.jota.dto.response.GetInclusionStateResponse;
import org.iota.jota.dto.response.GetNeighborsResponse;
import org.iota.jota.dto.response.GetNodeInfoResponse;
import org.iota.jota.dto.response.GetTipsResponse;
import org.iota.jota.dto.response.GetTransactionsToApproveResponse;
import org.iota.jota.dto.response.GetTrytesResponse;
import org.iota.jota.dto.response.RemoveNeighborsResponse;
import org.iota.jota.dto.response.WereAddressesSpentFromResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.Checksum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IotaCoreApiTest {

    private static final String TEST_BUNDLE = "XZKJUUMQOYUQFKMWQZNTFMSS9FKJLOEV9DXXXWPMQRTNCOUSUQNTBIJTVORLOQPLYZOTMLFRHYKMTGZZU";
    private static final String TEST_ADDRESS_UNSPENT = "D9UZTBEAT9DMZKMCPEKSBEOWPAUFWKOXWPO9LOHZVTE9HAVTAKHWAIXCJKDJFGUOBOULUFTJZKWTEKCHDAPJEJXEDD";
    private static final String TEST_ADDRESS_SPENT = "9SEGQNQHFHCAI9QXTVGBGTIZQDV9RSCGCGPQSPLNCNN9DSENFMLTD9SETUSYZCYG9JYPIAMXFHNT9YRFZMMRCMESPB";

    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM = "YJNQ9EQWSXUMLFCIUZDCAJZSAXUQNZSY9AKKVYKKFBAAHRSTKSHUOCCFTQVPPASPGGC9YGNLDQNOUWCAW";
    private static final String TEST_ADDRESS_WITH_CHECKSUM = "YJNQ9EQWSXUMLFCIUZDCAJZSAXUQNZSY9AKKVYKKFBAAHRSTKSHUOCCFTQVPPASPGGC9YGNLDQNOUWCAWGWIJNRJMX";

    private static final String TEST_HASH = "OOAARHCXXCPMNZPUEYOOUIUCTWZSQGKNIECIKRBNUUJEVMLJAWGCXREXEQGNJUJKUXXQAWWAZYKB99999";

    private static final String TAG = "IOTA9TAG9999999999999999";

    // TODO this is online available to verify why the disabled test are not running
    private static IotaAPICore proxy;

    @Mock
    private IotaAPI iotaAPIMock;

    // TODO this is only available to verify why the disabled test are not running
    @BeforeEach
    public void createProxyInstance() throws Exception {
        proxy = new IotaAPI.Builder().config(new FileConfig()).build();
    }

    @Test
    public void shouldGetNodeInfo() {
        GetNodeInfoResponse nodeInfoResponse = GetNodeInfoResponse.create("HORNET",
                "0.5.3-rc3", 0, 0, "8", 0, 0,
                "HSPDOTYNLKWIXYWLOCBVMGGKDQYBSC9BZJNJDEGXNIFMQNVJGZMYJNFVXBCVCKNJDOWKQDJXATSDUU999",
                1942896,
                "HSPDOTYNLKWIXYWLOCBVMGGKDQYBSC9BZJNJDEGXNIFMQNVJGZMYJNFVXBCVCKNJDOWKQDJXATSDUU999",
                1942896, 1651535, 3, 0,
                Instant.now().toEpochMilli(), 3, 0, new String[]{"RemotePOW", "WereAddressesSpentFrom"},
                "GYISMBVRKSCEXXTUPBWTIHRCZIKIRPDYAHAYKMNTPZSCSDNADDWAEUNHKUERZCTVAYJCNFXGTNUH9OGTW");

        when(iotaAPIMock.getNodeInfo()).thenReturn(nodeInfoResponse);

        GetNodeInfoResponse nodeInfo = iotaAPIMock.getNodeInfo();

        assertNotNull(nodeInfo.getAppVersion(), "Should have been set");
        assertNotNull(nodeInfo.getAppName(), "Should have been set");
        assertNotNull(nodeInfo.getJreVersion(), "Should have been set");
        assertNotNull(nodeInfo.getLatestMilestone(), "Should have been set");
        assertNotNull(nodeInfo.getLatestSolidSubtangleMilestone(), "Should have been set");
    }

    @Disabled("Connector Forbidden")
    @Test
    public void shouldGetNeighbors() {
        //getNeighBors is by default disabled
        GetNeighborsResponse neighbors = proxy.getNeighbors();
        assertNotNull(neighbors.getNeighbors(), "Should throw an error on failure");
    }

    @Disabled("Connector Forbidden")
    @Test //(expected = IllegalAccessError.class)
    public void shouldAddNeighbors() {
        AddNeighborsResponse neighborsResponse = proxy.addNeighbors("udp://8.8.8.8:14265");
        assertNotNull(neighborsResponse, "Should throw an error on failure");
    }

    @Disabled("Connector Forbidden")
    @Test //(expected = IllegalAccessError.class)
    public void shouldRemoveNeighbors() {
        RemoveNeighborsResponse neighborsResponse = proxy.removeNeighbors("udp://8.8.8.8:14265");
        assertNotNull(neighborsResponse, "Should throw an error on failure");
    }

    @Disabled("ArgumentException: {\"error\":\"command [getTips] is unknown\"}")
    @Test
    public void shouldGetTips() {
        GetTipsResponse tips = proxy.getTips();
        assertNotNull(tips, "Should throw an error on failure");
    }

    @Test
    public void shouldFindTransactionsByAddresses() {
        FindTransactionResponse transactionResponseMock = mock(FindTransactionResponse.class);
        when(iotaAPIMock.findTransactionsByAddresses(TEST_ADDRESS_WITHOUT_CHECKSUM))
                .thenReturn(transactionResponseMock);
        when(transactionResponseMock.getHashes()).thenReturn(new String[]{});

        FindTransactionResponse transactionsByAddresses =
                iotaAPIMock.findTransactionsByAddresses(TEST_ADDRESS_WITHOUT_CHECKSUM);

        assertNotNull(transactionsByAddresses.getHashes(), "Should throw an error on failure");
    }

    @Test
    public void shouldFindTransactionsByApproves() {
        FindTransactionResponse transactionResponseMock = mock(FindTransactionResponse.class);
        when(iotaAPIMock.findTransactionsByApprovees(TEST_ADDRESS_WITHOUT_CHECKSUM))
                .thenReturn(transactionResponseMock);
        when(transactionResponseMock.getHashes()).thenReturn(new String[]{});

        FindTransactionResponse transactionsByApproves =
                iotaAPIMock.findTransactionsByApprovees(TEST_ADDRESS_WITHOUT_CHECKSUM);

        assertNotNull(transactionsByApproves.getHashes(), "Should throw an error on failure");
    }

    @Test
    public void shouldFindTransactionsByBundles() {
        FindTransactionResponse transactionResponseMock = mock(FindTransactionResponse.class);
        when(iotaAPIMock.findTransactionsByBundles(TEST_HASH)).thenReturn(transactionResponseMock);
        when(transactionResponseMock.getHashes()).thenReturn(new String[]{});

        FindTransactionResponse transactionsByBundles = iotaAPIMock.findTransactionsByBundles(TEST_HASH);

        assertNotNull(transactionsByBundles.getHashes(), "Should throw an error on failure");
    }

    @Test
    public void shouldFindTransactionsByDigests() {
        FindTransactionResponse transactionResponseMock = mock(FindTransactionResponse.class);
        when(iotaAPIMock.findTransactionsByTags(TAG)).thenReturn(transactionResponseMock);
        when(transactionResponseMock.getHashes()).thenReturn(new String[]{});

        FindTransactionResponse trans = iotaAPIMock.findTransactionsByTags(TAG);

        assertNotNull(trans.getHashes(), "Should throw an error on failure");
    }

    @Test
    public void shouldGetTrytes() {
        GetTrytesResponse trytesResponseMock = mock(GetTrytesResponse.class);
        when(iotaAPIMock.getTrytes(TEST_HASH)).thenReturn(trytesResponseMock);
        when(trytesResponseMock.getTrytes()).thenReturn(new String[]{});

        GetTrytesResponse trytesResponse = iotaAPIMock.getTrytes(TEST_HASH);

        assertNotNull(trytesResponse.getTrytes(), "Should throw an error on failure");
    }

    @Disabled("Failed to throw error on wrong bundle hash ==> Expected org.iota.jota.error.ArgumentException to be thrown, but nothing was thrown.")
    @Test
    public void shouldNotGetInclusionStates() {
        ArgumentException argumentException = assertThrows(ArgumentException.class,
                () -> proxy.getInclusionStates(new String[]{TEST_HASH}),
                "Failed to throw error on wrong bundle hash");
        assertTrue(argumentException.getMessage().startsWith("{\"error\":\"One of the tips is absent\",\"duration\":"));
    }

    @Test
    public void shouldGetInclusionStates() {
        GetInclusionStateResponse inclusionStateResponseMock = mock(GetInclusionStateResponse.class);

        when(iotaAPIMock.getInclusionStates(TEST_HASH)).thenReturn(inclusionStateResponseMock);

        GetInclusionStateResponse inclusionStateResponse = iotaAPIMock.getInclusionStates(TEST_HASH);

        when(inclusionStateResponse.getStates()).thenReturn(new boolean[]{true});

        assertNotNull(inclusionStateResponse.getStates(), "Should throw an error on failure");
    }

    @Test
    public void shouldGetTransactionsToApprove() {
        GetTransactionsToApproveResponse transactionsToApproveResponseMock =
                mock(GetTransactionsToApproveResponse.class);

        when(iotaAPIMock.getTransactionsToApprove(4, null)).thenReturn(transactionsToApproveResponseMock);

        GetTransactionsToApproveResponse res = iotaAPIMock.getTransactionsToApprove(4, null);

        when(res.getTrunkTransaction()).thenReturn("notNullValue");
        when(res.getBranchTransaction()).thenReturn("notNullValue");

        assertNotNull(res.getTrunkTransaction(), "Should throw an error on failure");
        assertNotNull(res.getBranchTransaction(), "Should throw an error on failure");
    }

    @Disabled("Depth more then 15 is not supported by default ==> Expected org.iota.jota.error.ArgumentException to be thrown, but nothing was thrown.")
    @Test
    public void shouldInvalidDepth() {
        ArgumentException argumentException = assertThrows(ArgumentException.class,
                () -> proxy.getTransactionsToApprove(27),
                "Depth more then 15 is not supported by default");
        assertTrue(argumentException.getMessage().startsWith("{\"error\":\"Invalid depth input\",\"duration\":"));
    }

    @Test
    public void findTransactionsWithValidTags() {
        String[] addresses = {Checksum.addChecksum(TEST_BUNDLE)};
        String[] tags = {TAG};
        String[] approves = {TEST_BUNDLE};
        String[] bundles = {TEST_BUNDLE};

        when(iotaAPIMock.findTransactions(addresses, tags, approves, bundles)).thenReturn(new FindTransactionResponse());

        FindTransactionResponse transactionResponse = iotaAPIMock.findTransactions(addresses, tags, approves, approves);
        assertNotNull(transactionResponse, "Should throw an error on failure");
    }

    @Test
    public void findTransactionsFailIfInvalidTagIsProvided() {
        String test = TEST_BUNDLE;

        when(iotaAPIMock.findTransactions(
                new String[]{Checksum.addChecksum(test)}, new String[]{test},
                new String[]{test}, new String[]{test}))
                .thenThrow(new ArgumentException("Invalid tag provided."));

        ArgumentException argumentException = assertThrows(ArgumentException.class,
                () -> iotaAPIMock.findTransactions(
                        new String[]{Checksum.addChecksum(test)}, new String[]{test},
                        new String[]{test}, new String[]{test}),
                "Invalid tag results in exception");

        assertEquals("Invalid tag provided.", argumentException.getMessage(),
                "Invalid tag results in exception");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldGetBalances() {
        GetBalancesResponse balancesResponseMock = mock(GetBalancesResponse.class);

        when(iotaAPIMock.getBalances(100, Collections.singletonList(TEST_ADDRESS_WITH_CHECKSUM), null))
                .thenReturn(balancesResponseMock);

        GetBalancesResponse balancesResponse = iotaAPIMock.getBalances(100, Collections.singletonList(TEST_ADDRESS_WITH_CHECKSUM), null);

        when(balancesResponseMock.getReferences()).thenReturn(new String[]{});
        when(balancesResponseMock.getBalances()).thenReturn(new String[]{});
        when(balancesResponseMock.getDuration()).thenReturn(0L);

        assertNotNull(balancesResponse.getReferences(), "Should throw an error on failure");
        assertNotNull(balancesResponse.getBalances(), "Should throw an error on failure");
        assertNotNull(balancesResponse.getDuration(), "Should throw an error on failure");
    }

    @Test
    public void invalidAddressSpentFrom() {
        when(iotaAPIMock.wereAddressesSpentFrom(TEST_ADDRESS_WITHOUT_CHECKSUM))
                .thenThrow(new ArgumentException("Invalid addresses provided."));

        ArgumentException argumentException = assertThrows(ArgumentException.class,
                () -> iotaAPIMock.wereAddressesSpentFrom(TEST_ADDRESS_WITHOUT_CHECKSUM),
                "Provide invalid address should throw exception");

        assertEquals("Invalid addresses provided.", argumentException.getMessage());
    }

    @Disabled("expected: <true> but was: <false>")
    @Test
    public void addressIsSpentFrom() {
        WereAddressesSpentFromResponse ret = proxy.wereAddressesSpentFrom(TEST_ADDRESS_SPENT);
        assertTrue(ret.getStates()[0]);
    }

    @Test
    public void addressIsNotSpentFrom() {
        WereAddressesSpentFromResponse wereAddressesSpentFromResponseMock = mock(WereAddressesSpentFromResponse.class);

        when(iotaAPIMock.wereAddressesSpentFrom(TEST_ADDRESS_UNSPENT)).thenReturn(wereAddressesSpentFromResponseMock);
        when(wereAddressesSpentFromResponseMock.getStates()).thenReturn(new boolean[]{false});

        WereAddressesSpentFromResponse addressesSpentFromResponse = iotaAPIMock.wereAddressesSpentFrom(TEST_ADDRESS_UNSPENT);
        assertFalse(addressesSpentFromResponse.getStates()[0], "Response should have state about address spent");
    }
}
