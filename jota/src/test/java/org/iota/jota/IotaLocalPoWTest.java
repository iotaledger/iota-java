package org.iota.jota;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsNull;
import org.iota.jota.dto.response.SendTransferResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Transfer;
import org.iota.jota.pow.pearldiver.PearlDiverLocalPoW;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by pinpong on 01.10.17.
 */
public class IotaLocalPoWTest {

    private static final String TEST_SEED1 = "IHDEENZYITYVYSPKAURUZAQKGVJEREFDJMYTANNXXGPZ9GJWTEOJJ9IPMXOGZNQLSNMFDSQOTZAEETUEA";
    private static final String TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2 = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZC";
    private static final String TEST_MESSAGE = "JUSTANOTHERJOTATEST";
    private static final String TEST_TAG = "JOTASPAM9999999999999999999";
    private static final int MIN_WEIGHT_MAGNITUDE = 14;
    private static final int DEPTH = 9;

    private IotaAPI iotaClient;

    @Before
    public void createApiClientInstance() {
        iotaClient = new IotaAPI.Builder().localPoW(new PearlDiverLocalPoW()).build();
    }

    @Ignore
    @Test
    public void shouldSendTransfer() throws ArgumentException {
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new Transfer(TEST_ADDRESS_WITHOUT_CHECKSUM_SECURITY_LEVEL_2, 0, TEST_MESSAGE, TEST_TAG));
        SendTransferResponse str = iotaClient.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false, false, null);
        assertThat(str.getSuccessfully(), IsNull.notNullValue());
    }
}
