package org.iota.jota;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.IsNull;
import org.iota.jota.dto.response.SendTransferResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SendMessageTest {

    private static final String TEST_SEED1 = "IHDEENZYITYVYSPKAURUZAQKGVJEREFDJMYTANNXXGPZ9GJWTEOJJ9IPMXOGZNQLSNMFDSQOTZAEETUEA";
    private static final String TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2 = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC";
    private static final String TEST_MESSAGE = "JUSTANOTHERJOTATEST";
    private static final String TEST_TAG = "JOTASPAM9999999999999999999";
    private static final int MIN_WEIGHT_MAGNITUDE = 14;
    private static final int DEPTH = 9;

    private IotaAPI iotaClient;

    @Before
    public void createApiClientInstance() {
        iotaClient = new IotaAPI.Builder().build();
    }

    @Ignore
    @Test
    public void shouldSendMessage() throws ArgumentException {
        List<Transfer> transfers = new ArrayList<>();
        List<Transaction> tips = new ArrayList<>();

        // for each 2187 trytes in a message one transfer is necessary
        transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 0, StringUtils.rightPad(TEST_MESSAGE, 2188, '9'), TEST_TAG));

        SendTransferResponse str = iotaClient.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false, false, tips);
        assertEquals(str.getTransactions().size(), 2);
        assertThat(str.getTransactions(), IsNull.notNullValue());
        assertThat(str.getSuccessfully(), IsNull.notNullValue());
    }
}
