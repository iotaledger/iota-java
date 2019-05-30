package org.iota.jota;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.IsNull;
import org.iota.jota.config.types.FileConfig;
import org.iota.jota.dto.response.SendTransferResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SendMessageTest {

    private static final String TEST_SEED1 = "IHDEENZYITYVYSPKAURUZAQKGVJEREFDJMYTANNXXGPZ9GJWTEOJJ9IPMXOGZNQLSNMFDSQOTZAEETUEA";
    private static final String TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2 = "LXQHWNY9CQOHPNMKFJFIJHGEPAENAOVFRDIBF99PPHDTWJDCGHLYETXT9NPUVSNKT9XDTDYNJKJCPQMZCCOZVXMTXC";
    private static final String TEST_MESSAGE = "JUSTANOTHERJOTATEST";
    private static final String TEST_TAG = "JOTASPAM9999999999999999999";
    private static final int MIN_WEIGHT_MAGNITUDE = 14;
    private static final int DEPTH = 9;

    private IotaAPI iotaClient;

    @BeforeEach
    public void createApiClientInstance() throws Exception {
        iotaClient = new IotaAPI.Builder().config(new FileConfig()).build();
    }

    @Test
    public void shouldSendMessage() throws ArgumentException {
        List<Transfer> transfers = new ArrayList<>();

        // for each 2187 trytes in a message one transfer is necessary
        transfers.add(new Transfer(TEST_ADDRESS_WITH_CHECKSUM_SECURITY_LEVEL_2, 0, StringUtils.rightPad(TEST_MESSAGE, 2188, '9'), TEST_TAG));

        SendTransferResponse str = iotaClient.sendTransfer(TEST_SEED1, 2, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false, false, null);
        assertEquals(str.getTransactions().size(), 2);
        assertThat(str.getTransactions(), IsNull.notNullValue());
        assertThat(str.getSuccessfully(), IsNull.notNullValue());
    }
}
