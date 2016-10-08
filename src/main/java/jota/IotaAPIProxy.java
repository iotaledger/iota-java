package jota;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import jota.dto.request.*;
import jota.dto.response.*;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * IotaAPIProxy Builder. Usage:
 * 
 * IotaApiProxy api = IotaApiProxy.Builder
 *      .protocol("http")
 *      .nodeAddress("localhost")
 *      .port(12345)
 *      .build();
 * 
 * GetNodeInfoResponse response = api.getNodeInfo();
 * 
 * @author davassi
 */
public class IotaAPIProxy {

    private static final Logger log = LoggerFactory.getLogger(IotaAPIProxy.class);

    private IotaAPIService service;

    private IotaAPIProxy(final Builder builder) {
        protocol = builder.protocol;
        host = builder.host;
        port = builder.port;
        postConstruct();
    }

    private String protocol, host, port;

    private void postConstruct() {

        final String nodeUrl = protocol + "://" + host + ":" + port;

        final OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(nodeUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(IotaAPIService.class);

        log.debug("Jota-API Java proxy pointing to node url: '{}'", nodeUrl);
    }

    public GetNodeInfoResponse getNodeInfo() {
        final Call<GetNodeInfoResponse> res = service.getNodeInfo(IotaCommandRequest.createNodeInfoRequest());
        return wrapCheckedException(res).body();
    }

    public GetMilestoneResponse getMilestone(Integer index) {
        final Call<GetMilestoneResponse> res = service.getMilestone(IotaGetMilestoneRequest.createMilestoneRequest(index));
        return wrapCheckedException(res).body();
    }

    public GetNeighborsResponse getNeighbors() {
        final Call<GetNeighborsResponse> res = service.getNeighbors(IotaCommandRequest.createGetNeighborsRequest());
        return wrapCheckedException(res).body();
    }

    public GetTipsResponse getTips() {
        final Call<GetTipsResponse> res = service.getTips(IotaCommandRequest.createGetTipsRequest());
        return wrapCheckedException(res).body();
    }
    
    public GetTransfersResponse getTransfers(String seed, Integer securityLevel) {
        final IotaGetTransferRequest tr = IotaGetTransferRequest.createGetTransferRequest(seed, securityLevel);
        final Call<GetTransfersResponse> res = service.getTransfers(tr);
        return wrapCheckedException(res).body();
    }

    public FindTransactionResponse findTransactions(String [] addresses, String [] digests, String [] approvees, String [] bundles ) {

        final IotaFindTransactionsRequest findTransRequest = IotaFindTransactionsRequest
                .createFindTransactionRequest()
                .byAddresses(addresses)
                .byDigests(digests)
                .byApprovees(approvees)
                .byBundles(bundles);

        final Call<FindTransactionResponse> res = service.findTransactions(findTransRequest);
        return wrapCheckedException(res).body();
    }

    public FindTransactionResponse findTransactionsByAddresses(final String ... addresses) {
        return findTransactions(addresses, null, null, null);
    }

    public FindTransactionResponse findTransactionsByBundles(final String ... bundles) {
        return findTransactions(null, null, null, bundles);
    }

    public FindTransactionResponse findTransactionsByApprovees(final String ... approvees) {
        return findTransactions(null, null, approvees, null);
    }

    public FindTransactionResponse findTransactionsByDigests(final String ... digests) {
        return findTransactions(null, digests, null, null);
    }

    public GetInclusionStateResponse getInclusionStates(String[] transactions, String[] tips) {
        final Call<GetInclusionStateResponse> res = service.getInclusionStates(IotaGetInclusionStateRequest
                .createGetInclusionStateRequest(transactions, tips));
        return wrapCheckedException(res).body();
    }

    public GetInclusionStateResponse getInclusionStates(Collection<String> transactions, Collection<String> tips) {
        final Call<GetInclusionStateResponse> res = service.getInclusionStates(IotaGetInclusionStateRequest
                .createGetInclusionStateRequest(transactions, tips));
        return wrapCheckedException(res).body();
    }

    public GetBundleResponse getBundle(String transaction) {
        final Call<GetBundleResponse> res = service.getBundle(IotaGetBundleRequest.createIotaGetBundleRequest(transaction));
        return wrapCheckedException(res).body();
    }

    public GetTrytesResponse getTrytes(String ... hashes) {
        final Call<GetTrytesResponse> res = service.getTrytes(IotaGetTrytesRequest.createGetTrytesRequest(hashes));
        return wrapCheckedException(res).body();
    }

    public AnalyzeTransactionResponse analyseTransaction(String ... trytes) {
        final Call<AnalyzeTransactionResponse> res = service.analyzeTransactions(IotaAnalyzeTransactionRequest.createIotaAnalyzeTransactionRequest(trytes));
        return wrapCheckedException(res).body();
    }

    public GetNewAddressResponse getNewAddress(String seed, Integer securityLevel) {
        final Call<GetNewAddressResponse> res = service.getNewAddress(IotaGetNewAddressRequest.createIotaGetNewAddressRequest(seed, securityLevel));
        return wrapCheckedException(res).body();
    }

    public GetTransactionsToApproveResponse getTransactionsToApprove(String milestone) {
        final Call<GetTransactionsToApproveResponse> res = service.getTransactionsToApprove(IotaGetTransactionsToApproveRequest.createIotaGetTransactionsToApproveRequest(milestone));
        return wrapCheckedException(res).body();
    }


    protected static <T> Response<T> wrapCheckedException(final Call<T> call) {
        try {
            final Response<T> res = call.execute();
            if (res.code() == 400) {
                throw new IllegalAccessError(res.errorBody().toString());
            }
            return res;
        } catch (IOException e) {
            log.error("Execution of the API call raised exception. IOTA Node not reachable?", e);
            throw new IllegalStateException(e.getMessage());
        }
    }

    private static final String env(String env, String def) {
        return Optional.ofNullable(System.getenv(env)).orElseGet(() -> {
            log.warn("Enviroment variable '{}' is not defined, and actual value has not been specified. "
                    + "Rolling back to default value: '{}'", env, def);
            return def;
        });
    }

    public static class Builder {

        String protocol, host, port;
        
        public IotaAPIProxy build() {

            if (protocol == null || host == null || port == null) {

                // check properties files.
                if (!checkPropertiesFiles()) {

                    // last resort: best effort on enviroment variable,
                    // before assigning default values.
                    checkEnviromentVariables();
                }
            }

            return new IotaAPIProxy(this);
        }

        private boolean checkPropertiesFiles() {

            try (BufferedReader reader = Files.newBufferedReader(Paths.get("node_config.properties"))) {
                final Properties nodeConfig = new Properties();
                nodeConfig.load(reader);

                Optional.ofNullable(nodeConfig.getProperty("iota.node.protocol"))
                        .filter(v -> protocol == null)
                        .ifPresent(v -> protocol = v);

                Optional.ofNullable(nodeConfig.getProperty("iota.node.host"))
                        .filter(v -> host == null)
                        .ifPresent(v -> host = v);

                Optional.ofNullable(nodeConfig.getProperty("iota.node.port"))
                        .filter(v -> port == null)
                        .ifPresent(v -> port = v);

            } catch (IOException e1) {
                log.debug("node_config.properties not found. Rolling back for another solution...");
            }
            return (port != null && protocol != null && host != null);
        }

        private void checkEnviromentVariables() {

            Optional.of(env("IOTA_NODE_PROTOCOL", "http"))
                    .filter(v -> protocol == null)
                    .ifPresent(v -> protocol = v);

            Optional.ofNullable(env("IOTA_NODE_HOST", "localhost"))
                    .filter(v -> host == null)
                    .ifPresent(v -> host = v);

            Optional.ofNullable(env("IOTA_NODE_PORT", "14265"))
                    .filter(v -> port == null)
                    .ifPresent(v -> port = v);
        }
        
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(String port) {
            this.port = port;
            return this;
        }

        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

    }

}
