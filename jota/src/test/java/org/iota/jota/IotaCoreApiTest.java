package org.iota.jota;

import java.util.Collections;

import org.hamcrest.core.IsNull;
import org.iota.jota.config.types.FileConfig;
import org.iota.jota.dto.response.*;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.utils.Checksum;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class IotaCoreApiTest {

    private static final String TEST_BUNDLE = "XZKJUUMQOYUQFKMWQZNTFMSS9FKJLOEV9DXXXWPMQRTNCOUSUQNTBIJTVORLOQPLYZOTMLFRHYKMTGZZU";
    private static final String TEST_ADDRESS_UNSPENT = "D9UZTBEAT9DMZKMCPEKSBEOWPAUFWKOXWPO9LOHZVTE9HAVTAKHWAIXCJKDJFGUOBOULUFTJZKWTEKCHDAPJEJXEDD";
    private static final String TEST_ADDRESS_SPENT = "9SEGQNQHFHCAI9QXTVGBGTIZQDV9RSCGCGPQSPLNCNN9DSENFMLTD9SETUSYZCYG9JYPIAMXFHNT9YRFZMMRCMESPB";

    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM = "YJNQ9EQWSXUMLFCIUZDCAJZSAXUQNZSY9AKKVYKKFBAAHRSTKSHUOCCFTQVPPASPGGC9YGNLDQNOUWCAW";
    private static final String TEST_ADDRESS_WITH_CHECKSUM = "YJNQ9EQWSXUMLFCIUZDCAJZSAXUQNZSY9AKKVYKKFBAAHRSTKSHUOCCFTQVPPASPGGC9YGNLDQNOUWCAWGWIJNRJMX";
    
    private static final String TEST_HASH = "OOAARHCXXCPMNZPUEYOOUIUCTWZSQGKNIECIKRBNUUJEVMLJAWGCXREXEQGNJUJKUXXQAWWAZYKB99999";

    private static final String TAG = "IOTA9TAG9999999999999999";
    
    private static IotaAPICore proxy;

    @BeforeEach
    public void createProxyInstance() throws Exception {
        proxy = new IotaAPI.Builder().config(new FileConfig()).build();
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetNodeInfo() throws ArgumentException {
        GetNodeInfoResponse nodeInfo = proxy.getNodeInfo();
        assertThat(nodeInfo.getAppVersion(), IsNull.notNullValue());
        assertThat(nodeInfo.getAppName(), IsNull.notNullValue());
        assertThat(nodeInfo.getJreVersion(), IsNull.notNullValue());
        assertThat(nodeInfo.getJreAvailableProcessors(), IsNull.notNullValue());
        assertThat(nodeInfo.getJreFreeMemory(), IsNull.notNullValue());
        assertThat(nodeInfo.getJreMaxMemory(), IsNull.notNullValue());
        assertThat(nodeInfo.getJreTotalMemory(), IsNull.notNullValue());
        assertThat(nodeInfo.getLatestMilestone(), IsNull.notNullValue());
        assertThat(nodeInfo.getLatestMilestoneIndex(), IsNull.notNullValue());
        assertThat(nodeInfo.getLatestSolidSubtangleMilestone(), IsNull.notNullValue());
        assertThat(nodeInfo.getLatestSolidSubtangleMilestoneIndex(), IsNull.notNullValue());
        assertThat(nodeInfo.getNeighbors(), IsNull.notNullValue());
        assertThat(nodeInfo.getPacketsQueueSize(), IsNull.notNullValue());
        assertThat(nodeInfo.getTime(), IsNull.notNullValue());
        assertThat(nodeInfo.getTips(), IsNull.notNullValue());
        assertThat(nodeInfo.getTransactionsToRequest(), IsNull.notNullValue());
    }

    @Disabled
    @Test //(expected = IllegalAccessError.class)
    @Tag("IntegrationTest")
    public void shouldGetNeighbors() throws ArgumentException {
        //getNeighBors is by default disabled
        GetNeighborsResponse neighbors = proxy.getNeighbors();
        assertThat(neighbors.getNeighbors(), IsNull.notNullValue());
    }

    @Disabled
    @Test //(expected = IllegalAccessError.class)
    @Tag("IntegrationTest")
    public void shouldAddNeighbors() throws ArgumentException {
        AddNeighborsResponse res = proxy.addNeighbors("udp://8.8.8.8:14265");
        assertThat(res, IsNull.notNullValue());
    }

    @Disabled
    @Test //(expected = IllegalAccessError.class)
    @Tag("IntegrationTest")
    public void shouldRemoveNeighbors() throws ArgumentException {
        RemoveNeighborsResponse res = proxy.removeNeighbors("udp://8.8.8.8:14265");
        assertThat(res, IsNull.notNullValue());
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetTips() throws ArgumentException {
        GetTipsResponse tips = proxy.getTips();
        assertThat(tips, IsNull.notNullValue());
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldFindTransactionsByAddresses() throws ArgumentException {
        FindTransactionResponse trans = proxy.findTransactionsByAddresses(TEST_ADDRESS_WITH_CHECKSUM);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldFindTransactionsByApprovees() throws ArgumentException {
        FindTransactionResponse trans = proxy.findTransactionsByApprovees(TEST_ADDRESS_WITHOUT_CHECKSUM);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldFindTransactionsByBundles() throws ArgumentException {
        FindTransactionResponse trans = proxy.findTransactionsByBundles(TEST_HASH);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldFindTransactionsByDigests() throws ArgumentException {
        FindTransactionResponse trans = proxy.findTransactionsByTags(TAG);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetTrytes() throws ArgumentException {
        GetTrytesResponse res = proxy.getTrytes(TEST_HASH);
        assertThat(res.getTrytes(), IsNull.notNullValue());
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldNotGetInclusionStates(){
        ArgumentException argumentException = assertThrows(ArgumentException.class,
                () -> proxy.getInclusionStates(new String[]{TEST_HASH},
                        new String[]{"ZIJGAJ9AADLRPWNCYNNHUHRRAC9QOUDATEDQUMTNOTABUVRPTSTFQDGZKFYUUIE9ZEBIVCCXXXLKX9999"}),
                "Failed to throw error on wrong bundle hash");
        assertTrue(argumentException.getMessage().startsWith("{\"error\":\"One of the tips is absent\",\"duration\":"));
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetInclusionStates() throws ArgumentException {
        System.out.println(proxy.getNodeInfo().getLatestSolidSubtangleMilestone());
        GetInclusionStateResponse res = proxy.getInclusionStates(
                new String[]{TEST_HASH}, 
                new String[]{proxy.getNodeInfo().getLatestSolidSubtangleMilestone()});
        assertThat(res.getStates(), IsNull.notNullValue());
    }

    @Test // very long execution
    @Tag("IntegrationTest")
    public void shouldGetTransactionsToApprove() throws ArgumentException {
        GetTransactionsToApproveResponse res = proxy.getTransactionsToApprove(15, null);
        assertThat(res.getTrunkTransaction(), IsNull.notNullValue());
        assertThat(res.getBranchTransaction(), IsNull.notNullValue());
    }
    
    @Test
    @Tag("IntegrationTest")
    public void shouldInvalidDepth() throws ArgumentException {
        ArgumentException argumentException = assertThrows(ArgumentException.class,
                () -> proxy.getTransactionsToApprove(27),
                "Depth more then 15 is not supported by default");
        assertTrue(argumentException.getMessage().startsWith("{\"error\":\"Invalid depth input\",\"duration\":"));
    }

    @Test
    @Tag("IntegrationTest")
    public void findTransactionsWithValidTags() {
        String test = TEST_BUNDLE;
        FindTransactionResponse resp = proxy.findTransactions(
                new String[]{Checksum.addChecksum(test)},
                new String[]{TAG},
                new String[]{test}, 
                new String[]{test});

        assertNotNull(resp);
    }

    @Test
    @Tag("IntegrationTest")
    public void findTransactionsFailIfInvalidTagIsProvided() {
        String test = TEST_BUNDLE;
        ArgumentException argumentException = assertThrows(ArgumentException.class,
                () -> proxy.findTransactions(
                        new String[]{Checksum.addChecksum(test)}, new String[]{test},
                        new String[]{test}, new String[]{test}));
        assertEquals("[Invalid tag provided.]", argumentException.getMessage());
    }

    @Test
    @Tag("IntegrationTest")
    public void shouldGetBalances() throws ArgumentException {
        GetBalancesResponse res = proxy.getBalances(100, Collections.singletonList(TEST_ADDRESS_WITH_CHECKSUM), null);
        assertThat(res.getReferences(), IsNull.notNullValue());
        assertThat(res.getBalances(), IsNull.notNullValue());
        assertThat(res.getMilestoneIndex(), IsNull.notNullValue());
        assertThat(res.getDuration(), IsNull.notNullValue());
    }
    
    @Test
    @Tag("IntegrationTest")
    public void invalidAddressSpentFrom() throws ArgumentException {
        ArgumentException argumentException = assertThrows(ArgumentException.class,
                () -> proxy.wereAddressesSpentFrom(TEST_ADDRESS_WITHOUT_CHECKSUM),
                "Failed to throw error on wrong address hash");
        assertEquals("Invalid addresses provided.", argumentException.getMessage());
    }
    
    @Test
    @Tag("IntegrationTest")
    public void addressIsSpentFrom() throws ArgumentException {
        WereAddressesSpentFromResponse ret = proxy.wereAddressesSpentFrom(TEST_ADDRESS_SPENT);
        assertTrue(ret.getStates()[0]);
    }
    
    @Test
    @Tag("IntegrationTest")
    public void addressIsNotSpentFrom() throws ArgumentException {
        WereAddressesSpentFromResponse ret = proxy.wereAddressesSpentFrom(TEST_ADDRESS_UNSPENT);
        assertFalse(ret.getStates()[0]);
    }
}
