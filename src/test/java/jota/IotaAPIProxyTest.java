package jota;

import static org.junit.Assert.assertThat;

import jota.dto.response.*;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;

/**
 * Let's do some test coverage.
 * 
 * @author davassi
 */
public class IotaAPIProxyTest {

    private static final String TEST_SEED = "AAA999999999999999999999999999999999999999999999999999999999999999999999999999999";
    private static final String TEST_TRANSACTION = "ZJVYUGTDRPDYFGFXMKOTV9ZWSGFK9CFPXTITQLQNLPPG9YNAARMKNKYQO9GSCSBIOTGMLJUFLZWSY9999";
    private static final String TEST_HASH = "OAATQS9VQLSXCLDJVJJVYUGONXAXOFMJOZNSYWRZSWECMXAQQURHQBJNLD9IOFEPGZEPEMPXCIVRX9999";
    private static final Integer TEST_MILESTONE_INDEX = 8059;

    private IotaAPIProxy proxy;

    @Before
    public void createProxyInstance() {
        proxy = new IotaAPIProxy.Builder().build();
    }

    @Test
    public void shouldCreateIotaApiProxyInstanceWithDefaultValues() {
        IotaAPIProxy proxy = new IotaAPIProxy.Builder().build();
        assertThat(proxy, IsNull.notNullValue());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotCreateIotaApiProxyInstanceWithDefaultValues() {
        new IotaAPIProxy.Builder().host("a_very_wrong_ip_address").build().getNodeInfo();
    }

    @Test
    public void shouldGetNodeInfo() {
        GetNodeInfoResponse nodeInfo = proxy.getNodeInfo();
        assertThat(nodeInfo, IsNull.notNullValue());
    }

    @Test
    public void shouldGetMilestone() {
        GetMilestoneResponse milestone = proxy.getMilestone(TEST_MILESTONE_INDEX);
        assertThat(milestone, IsNull.notNullValue());
    }
    
    @Test
    public void shouldGetNeighbors() {
        GetNeighborsResponse neighbors = proxy.getNeighbors();
        assertThat(neighbors, IsNull.notNullValue());
    }

    @Test
    public void shouldGetTips() {
        GetTipsResponse tips = proxy.getTips();
        assertThat(tips, IsNull.notNullValue());
    }

    @Test
    public void shouldFindTransactionsByAddresses() {
        FindTransactionResponse trans = proxy.findTransactionsByAddresses(TEST_TRANSACTION);
        assertThat(trans, IsNull.notNullValue());
    }

    @Test
    public void shouldFindTransactionsByApprovees() {
        FindTransactionResponse trans = proxy.findTransactionsByApprovees(new String[]{"123ABC"});
        assertThat(trans, IsNull.notNullValue());
    }

    @Test
    public void shouldFindTransactionsByBundles() {
        FindTransactionResponse trans = proxy.findTransactionsByBundles(new String[]{"123ABC"});
        assertThat(trans, IsNull.notNullValue());
    }

    @Test
    public void shouldFindTransactionsByDigests() {
        FindTransactionResponse trans = proxy.findTransactionsByDigests(new String[]{"123ABC"});
        assertThat(trans, IsNull.notNullValue());
    }

    @Test
    public void shouldGetTransactions() {
        GetTransfersResponse res = proxy.getTransfers(TEST_SEED, 1);
        assertThat(res, IsNull.notNullValue());
    }

    @Test
    public void shouldGetInclusionStates() {
        GetInclusionStateResponse res = proxy.getInclusionStates(new String[]{TEST_TRANSACTION},
                new String[]{"123"});
        assertThat(res, IsNull.notNullValue());
    }

    @Test
    public void shouldGetBundle() {
        GetBundleResponse res = proxy.getBundle(TEST_TRANSACTION);
        assertThat(res, IsNull.notNullValue());
    }

    @Test
    public void shouldGetTrytes() {
        GetTrytesResponse res = proxy.getTrytes(TEST_HASH);
        assertThat(res, IsNull.notNullValue());
    }
}
