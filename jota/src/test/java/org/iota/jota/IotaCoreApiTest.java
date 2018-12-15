package org.iota.jota;

import com.google.gson.Gson;

import org.hamcrest.core.IsNull;
import org.iota.jota.category.IntegrationTest;
import org.iota.jota.dto.response.*;
import org.iota.jota.error.ArgumentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 *
 */
public class IotaCoreApiTest {

    private static final String TEST_BUNDLE = "XZKJUUMQOYUQFKMWQZNTFMSS9FKJLOEV9DXXXWPMQRTNCOUSUQNTBIJTVORLOQPLYZOTMLFRHYKMTGZZU";
    private static final String TEST_ADDRESS_UNSPENT = "D9UZTBEAT9DMZKMCPEKSBEOWPAUFWKOXWPO9LOHZVTE9HAVTAKHWAIXCJKDJFGUOBOULUFTJZKWTEKCHD";
    private static final String TEST_ADDRESS_SPENT = "9SEGQNQHFHCAI9QXTVGBGTIZQDV9RSCGCGPQSPLNCNN9DSENFMLTD9SETUSYZCYG9JYPIAMXFHNT9YRFZ";
    private static final String TEST_ADDRESS_WITH_CHECKSUM = "YJNQ9EQWSXUMLFCIUZDCAJZSAXUQNZSY9AKKVYKKFBAAHRSTKSHUOCCFTQVPPASPGGC9YGNLDQNOUWCAWGWIJNRJMX";
    private static final String TEST_HASH = "OOAARHCXXCPMNZPUEYOOUIUCTWZSQGKNIECIKRBNUUJEVMLJAWGCXREXEQGNJUJKUXXQAWWAZYKB99999";
    private static IotaAPICore proxy;

    @Before
    public void createProxyInstance() {
        proxy = new IotaAPI.Builder().build();
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetNodeInfo() throws ArgumentException {
        GetNodeInfoResponse nodeInfo = proxy.getNodeInfo();
        System.out.println(new Gson().toJson(nodeInfo));
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

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetNeighbors() throws ArgumentException {
        //getNeighBors is by default disabled
        //GetNeighborsResponse neighbors = proxy.getNeighbors();
        //assertThat(neighbors.getNeighbors(), IsNull.notNullValue());
    }

    @Test(expected = IllegalAccessError.class)
    @Category(IntegrationTest.class)
    public void shouldAddNeighbors() throws ArgumentException {
        AddNeighborsResponse res = proxy.addNeighbors("udp://8.8.8.8:14265");
        assertThat(res, IsNull.notNullValue());
    }

    @Test(expected = IllegalAccessError.class)
    @Category(IntegrationTest.class)
    public void shouldRemoveNeighbors() throws ArgumentException {
        RemoveNeighborsResponse res = proxy.removeNeighbors("udp://8.8.8.8:14265");
        assertThat(res, IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetTips() throws ArgumentException {
        GetTipsResponse tips = proxy.getTips();
        assertThat(tips, IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldFindTransactionsByAddresses() throws ArgumentException {
        FindTransactionResponse trans = proxy.findTransactionsByAddresses(TEST_ADDRESS_WITH_CHECKSUM);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldFindTransactionsByApprovees() throws ArgumentException {
        FindTransactionResponse trans = proxy.findTransactionsByApprovees(TEST_HASH);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldFindTransactionsByBundles() throws ArgumentException {
        FindTransactionResponse trans = proxy.findTransactionsByBundles(TEST_HASH);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldFindTransactionsByDigests() throws ArgumentException {
        FindTransactionResponse trans = proxy.findTransactionsByDigests(TEST_HASH);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetTrytes() throws ArgumentException {
        GetTrytesResponse res = proxy.getTrytes(TEST_HASH);
        assertThat(res.getTrytes(), IsNull.notNullValue());
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldNotGetInclusionStates(){
        try {
            proxy.getInclusionStates(new String[]{TEST_HASH}, new String[]{"ZIJGAJ9AADLRPWNCYNNHUHRRAC9QOUDATEDQUMTNOTABUVRPTSTFQDGZKFYUUIE9ZEBIVCCXXXLKX9999"});
            fail("failed to throw error on wrong bundle hash");
        } catch (ArgumentException e) {
          //TODO verify correct error
            //Successs
        }

    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetInclusionStates() throws ArgumentException {
        GetInclusionStateResponse res = proxy.getInclusionStates(new String[]{TEST_HASH}, new String[]{proxy.getNodeInfo().getLatestSolidSubtangleMilestone()});
        assertThat(res.getStates(), IsNull.notNullValue());
    }

    @Test // very long execution
    @Category(IntegrationTest.class)
    public void shouldGetTransactionsToApprove() throws ArgumentException {
        GetTransactionsToApproveResponse res = proxy.getTransactionsToApprove(15, null);
        assertThat(res.getTrunkTransaction(), IsNull.notNullValue());
        assertThat(res.getBranchTransaction(), IsNull.notNullValue());
    }
    
    @Test // very long execution
    @Category(IntegrationTest.class)
    public void shouldInvalidDepth() throws ArgumentException {
        try {
            proxy.getTransactionsToApprove(27);
            fail("Depth more then 15 is not supported by default");
        } catch (ArgumentException e) {
            //TODO verify correct error
            //Good!
        }
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldFindTransactions() throws ArgumentException {
        String test = TEST_BUNDLE;
        FindTransactionResponse resp = proxy.findTransactions(new String[]{test}, new String[]{test}, new String[]{test}, new String[]{test});
        System.out.println(new Gson().toJson(resp));
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldGetBalances() throws ArgumentException {
        GetBalancesResponse res = proxy.getBalances(100, Collections.singletonList(TEST_ADDRESS_WITH_CHECKSUM), null);
        assertThat(res.getReferences(), IsNull.notNullValue());
        assertThat(res.getBalances(), IsNull.notNullValue());
        assertThat(res.getMilestoneIndex(), IsNull.notNullValue());
        assertThat(res.getDuration(), IsNull.notNullValue());
    }
    
    @Test
    @Category(IntegrationTest.class)
    public void invalidAddressSpentFrom() throws ArgumentException {
        try {
            //Addresses with checksum aren't allowed, remove last 9 characters!
            proxy.wereAddressesSpentFrom(TEST_ADDRESS_WITH_CHECKSUM);
            fail("failed to throw error on wrong address hash");
        } catch (ArgumentException e) {
          //TODO verify correct error
            //Success
        }
    }
    
    @Test
    @Category(IntegrationTest.class)
    public void addressIsSpentFrom() throws ArgumentException {
        WereAddressesSpentFromResponse ret = proxy.wereAddressesSpentFrom(TEST_ADDRESS_SPENT);
        assertTrue(ret.getStates()[0]);
    }
    
    @Test
    @Category(IntegrationTest.class)
    public void addressIsNotSpentFrom() throws ArgumentException {
        WereAddressesSpentFromResponse ret = proxy.wereAddressesSpentFrom(TEST_ADDRESS_UNSPENT);
        assertFalse(ret.getStates()[0]);
    }
}
