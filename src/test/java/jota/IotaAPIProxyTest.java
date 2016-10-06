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
        GetMilestoneResponse milestone = proxy.getMilestone(1000);
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
        FindTransactionResponse trans = proxy.findTransactionsByAddresses(new String[]{"123ABC"});
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
    public void shouldGetInclusionStates() {
        GetInclusionStateResponse res = proxy.getInclusionStates(new String[]{"123ABC"},
                new String[]{"123"});
        assertThat(res, IsNull.notNullValue());
    }
}
