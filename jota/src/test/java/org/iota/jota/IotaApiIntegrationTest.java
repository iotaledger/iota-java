package org.iota.jota;

import static org.iota.jota.IotaAPICommands.GET_TRYTES;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.hamcrest.core.IsNull;
import org.iota.jota.dto.response.GetBundleResponse;
import org.iota.jota.error.ArgumentException;
import org.junit.Test;

public class IotaApiIntegrationTest extends AbstractMockServer {

    @Test
    public void shouldGetBundle() throws ArgumentException, IOException {

        // given
        new ApiMock().command(GET_TRYTES).request("requestTx1").response("responseTx1").build();
        new ApiMock().command(GET_TRYTES).request("requestTx2").response("responseTx2").build();
        new ApiMock().command(GET_TRYTES).request("requestTx3").response("responseTx3").build();
        new ApiMock().command(GET_TRYTES).request("requestTx4").response("responseTx4").build();

        // when
        GetBundleResponse gbr = iotaAPI.getBundle("BKBALUPMEECOGEYQU9OHXTFTHV9OKEVUGHAUNNQCNETAQWIRJIKDGWSWXY9RSIMZJBPIPEIQEFEIA9999");

        // then
        assertThat(gbr, IsNull.notNullValue());
    }

}
