package org.iota.jota;

import static java.nio.charset.Charset.defaultCharset;
import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadler;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractMockServer {

    IotaAPI iotaAPI;
    
    public AbstractMockServer() {
        
    }

    @Before
    public void setUp() {

        initJadler();
        iotaAPI = new IotaAPI.Builder().protocol("http").host("localhost").port(port()).build();
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

        public ApiMock command(IotaAPICommands command) {
            this.command = command;
            return this;
        }

        public ApiMock request(String requestFileName) {
            this.requestFileName = requestFileName;
            return this;
        }

        public ApiMock response(String responseFileName) {
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
