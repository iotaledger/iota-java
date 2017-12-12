package jota;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static net.jadler.Jadler.*;

abstract class AbstractMockServer {

    IotaAPI iotaAPI;

    @Before
    public void setUp() {

        initJadler();
        iotaAPI = new IotaAPI.Builder().protocol("http").host("localhost").port(Integer.toString(port())).build();
    }

    @After
    public void tearDown() {
        closeJadler();
    }

    static class ApiMock {

        enum HttpOperation {
            REQUEST, RESPONSE
        }

        private IotaAPICommands command;
        private String requestFileName;
        private String responseFileName;

        ApiMock command(IotaAPICommands command) {
            this.command = command;
            return this;
        }

        ApiMock request(String requestFileName) {
            this.requestFileName = requestFileName;
            return this;
        }

        ApiMock response(String responseFileName) {
            this.responseFileName = responseFileName;
            return this;
        }

        void build() {
            onRequest()
                    .havingMethodEqualTo("POST")
                    .havingBodyEqualTo(getBody(command, requestFileName, HttpOperation.REQUEST))
                    .respond()
                    .withBody(getBody(command, responseFileName, HttpOperation.RESPONSE))
                    .withStatus(200);
        }

        private String getBody(IotaAPICommands command, String file, HttpOperation httpOperation) {
            try {
                String filePath = String.format("/http/%s/%s/%s.json", command.command(), httpOperation.name().toLowerCase(), file);
                return IOUtils.toString(this.getClass().getResourceAsStream(filePath), defaultCharset());
            } catch (IOException e) {
                String msg = String.format("Cannot read HTTP %s body from file [%s] for [%s] command", httpOperation.name().toLowerCase(), file, command);
                throw new IllegalArgumentException(msg);
            }
        }
    }
}
