package jota;

import com.google.gson.Gson;
import jota.dto.response.*;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * @author Adrian
 */
public class IotaCoreApiTest {

    private static final String TEST_BUNDLE = "XZKJUUMQOYUQFKMWQZNTFMSS9FKJLOEV9DXXXWPMQRTNCOUSUQNTBIJTVORLOQPLYZOTMLFRHYKMTGZZU";
    private static final String TEST_ADDRESS_WITH_CHECKSUM = "PNGMCSNRCTRHCHPXYTPKEJYPCOWKOMRXZFHH9N9VDIKMNVAZCMIYRHVJIAZARZTUETJVFDMBEBIQE9QTHBFWDAOEFA";
    private static final String TEST_HASH = "OAATQS9VQLSXCLDJVJJVYUGONXAXOFMJOZNSYWRZSWECMXAQQURHQBJNLD9IOFEPGZEPEMPXCIVRX9999";
    private static IotaAPICore proxy;

    @Before
    public void createProxyInstance() {
        proxy = new IotaAPICore.Builder().build();
    }

    @Test
    public void shouldGetNodeInfo() {
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
    public void shouldGetNeighbors() {
        GetNeighborsResponse neighbors = proxy.getNeighbors();
        assertThat(neighbors.getNeighbors(), IsNull.notNullValue());
    }

    @Test(expected = IllegalAccessError.class)
    public void shouldAddNeighbors() {
        AddNeighborsResponse res = proxy.addNeighbors("udp://8.8.8.8:14265");
        assertThat(res, IsNull.notNullValue());
    }

    @Test(expected = IllegalAccessError.class)
    public void shouldRemoveNeighbors() {
        RemoveNeighborsResponse res = proxy.removeNeighbors("udp://8.8.8.8:14265");
        assertThat(res, IsNull.notNullValue());
    }

    @Test
    public void shouldGetTips() {
        GetTipsResponse tips = proxy.getTips();
        assertThat(tips, IsNull.notNullValue());
    }

    @Test
    public void shouldFindTransactionsByAddresses() {
        FindTransactionResponse trans = proxy.findTransactionsByAddresses(TEST_ADDRESS_WITH_CHECKSUM);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    public void shouldFindTransactionsByApprovees() {
        FindTransactionResponse trans = proxy.findTransactionsByApprovees(TEST_HASH);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    public void shouldFindTransactionsByBundles() {
        FindTransactionResponse trans = proxy.findTransactionsByBundles(TEST_HASH);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    public void shouldFindTransactionsByDigests() {
        FindTransactionResponse trans = proxy.findTransactionsByDigests(TEST_HASH);
        assertThat(trans.getHashes(), IsNull.notNullValue());
    }

    @Test
    public void shouldGetTrytes() {
        GetTrytesResponse res = proxy.getTrytes(TEST_HASH);
        assertThat(res.getTrytes(), IsNull.notNullValue());

    }

    @Test(expected = IllegalAccessError.class)
    public void shouldNotGetInclusionStates() {
        GetInclusionStateResponse res = proxy.getInclusionStates(new String[]{"DBPECSH9YLSSTQDGERUHJBBJTKVUDBMTJLG9WPHBINGHIFOSJMDJLARTVOXXWEFQJLLBINOHCZGYFSMUEXWPPMTOFW"}, new String[]{"EJDQOQHMLJGBMFWB9WJSPRCYIGNPO9WRHDCEQXIMPVPIJ9JV9RJGVHNX9EPGXFOOKBABCVMMAAX999999"});
        assertThat(res.getStates(), IsNull.notNullValue());
    }

    @Test
    public void shouldGetInclusionStates() {
        GetInclusionStateResponse res = proxy.getInclusionStates(new String[]{TEST_ADDRESS_WITH_CHECKSUM}, new String[]{proxy.getNodeInfo().getLatestSolidSubtangleMilestone()});
        assertThat(res.getStates(), IsNull.notNullValue());
    }

    @Test // very long execution
    public void shouldGetTransactionsToApprove() {
        GetTransactionsToApproveResponse res = proxy.getTransactionsToApprove(27);
        assertThat(res.getTrunkTransaction(), IsNull.notNullValue());
        assertThat(res.getBranchTransaction(), IsNull.notNullValue());
    }

    @Test
    public void shouldFindTransactions() {
        String test = TEST_BUNDLE;
        FindTransactionResponse resp = proxy.findTransactions(new String[]{test}, new String[]{test}, new String[]{test}, new String[]{test});
        System.out.println(new Gson().toJson(resp));
    }

    @Test
    public void shouldGetBalances() {
        GetBalancesResponse res = proxy.getBalances(100, new String[]{TEST_ADDRESS_WITH_CHECKSUM});
        assertThat(res.getBalances(), IsNull.notNullValue());
        assertThat(res.getMilestone(), IsNull.notNullValue());
        assertThat(res.getMilestoneIndex(), IsNull.notNullValue());
        assertThat(res.getDuration(), IsNull.notNullValue());
    }
}
