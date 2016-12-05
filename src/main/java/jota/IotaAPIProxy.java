package jota;

import jota.dto.request.*;
import jota.dto.response.*;
import jota.utils.IotaAPIUtils;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * IotaAPIProxy Builder. Usage:
 *
 * IotaApiProxy api = IotaApiProxy.Builder
 * .protocol("http")
 * .nodeAddress("localhost")
 * .port(12345)
 * .build();
 *
 * GetNodeInfoResponse response = api.getNodeInfo();
 *
 * @author davassi
 */
public class IotaAPIProxy {

    private static final Logger log = LoggerFactory.getLogger(IotaAPIProxy.class);

    private IotaAPIService service;
    private String protocol, host, port;

    private IotaAPIProxy(final Builder builder) {
        protocol = builder.protocol;
        host = builder.host;
        port = builder.port;
        postConstruct();
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
        final String value = System.getenv(env);
        if (value == null) {
            log.warn("Environment variable '{}' is not defined, and actual value has not been specified. "
                    + "Rolling back to default value: '{}'", env, def);
            return def;
        }
        return value;
    }

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

    public GetNeighborsResponse getNeighbors() {
        final Call<GetNeighborsResponse> res = service.getNeighbors(IotaCommandRequest.createGetNeighborsRequest());
        return wrapCheckedException(res).body();
    }

    public AddNeighborsResponse addNeighbors(String... uris) {
        final Call<AddNeighborsResponse> res = service.addNeighbors(IotaNeighborsRequest.createAddNeighborsRequest(uris));
        return wrapCheckedException(res).body();
    }

    public RemoveNeighborsResponse removeNeighbors(String... uris) {
        final Call<RemoveNeighborsResponse> res = service.removeNeighbors(IotaNeighborsRequest.createRemoveNeighborsRequest(uris));
        return wrapCheckedException(res).body();
    }

    public GetTipsResponse getTips() {
        final Call<GetTipsResponse> res = service.getTips(IotaCommandRequest.createGetTipsRequest());
        return wrapCheckedException(res).body();
    }

    public FindTransactionResponse findTransactions(String[] addresses, String[] tags, String[] approvees, String[] bundles) {

        final IotaFindTransactionsRequest findTransRequest = IotaFindTransactionsRequest
                .createFindTransactionRequest()
                .byAddresses(addresses)
                .byTags(tags)
                .byApprovees(approvees)
                .byBundles(bundles);

        final Call<FindTransactionResponse> res = service.findTransactions(findTransRequest);
        return wrapCheckedException(res).body();
    }

    public FindTransactionResponse findTransactionsByAddresses(final String... addresses) {
        return findTransactions(addresses, null, null, null);
    }

    public FindTransactionResponse findTransactionsByBundles(final String... bundles) {
        return findTransactions(null, null, null, bundles);
    }

    public FindTransactionResponse findTransactionsByApprovees(final String... approvees) {
        return findTransactions(null, null, approvees, null);
    }

    public FindTransactionResponse findTransactionsByDigests(final String... digests) {
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

    public GetTrytesResponse getTrytes(String... hashes) {
        final Call<GetTrytesResponse> res = service.getTrytes(IotaGetTrytesRequest.createGetTrytesRequest(hashes));
        return wrapCheckedException(res).body();
    }

    public GetTransactionsToApproveResponse getTransactionsToApprove(Integer depth) {
        final Call<GetTransactionsToApproveResponse> res = service.getTransactionsToApprove(IotaGetTransactionsToApproveRequest.createIotaGetTransactionsToApproveRequest(depth));
        return wrapCheckedException(res).body();
    }

    public GetBalancesResponse getBalances(Integer threshold, String[] addresses) {
        final Call<GetBalancesResponse> res = service.getBalances(IotaGetBalancesRequest.createIotaGetBalancesRequest(threshold, addresses));
        return wrapCheckedException(res).body();
    }

    public InterruptAttachingToTangleResponse interruptAttachingToTangle() {
        final Call<InterruptAttachingToTangleResponse> res = service.interruptAttachingToTangle(IotaCommandRequest.createInterruptAttachToTangleRequest());
        return wrapCheckedException(res).body();
    }

    public GetAttachToTangleResponse attachToTangle(String trunkTransaction, String branchTransaction, Integer minWeightMagnitude, String... trytes) {
        final Call<GetAttachToTangleResponse> res = service.attachToTangle(IotaAttachToTangleRequest.createAttachToTangleRequest(trunkTransaction, branchTransaction, minWeightMagnitude, trytes));
        return wrapCheckedException(res).body();
    }

    public StoreTransactionsResponse storeTransactions(String... trytes) {
        final Call<StoreTransactionsResponse> res = service.storeTransactions(IotaStoreTransactionsRequest.createStoreTransactionsRequest(trytes));
        return wrapCheckedException(res).body();
    }

    // end of proxied calls.

    public BroadcastTransactionsResponse broadcastTransactions(String... trytes) {
        final Call<BroadcastTransactionsResponse> res = service.broadcastTransactions(IotaBroadcastTransactionRequest.createBroadcastTransactionsRequest(trytes));
        return wrapCheckedException(res).body();
    }

    public GetBundleResponse getBundle(String transaction) {
        return IotaAPIUtils.getBundle(transaction);
    }

    public GetNewAddressResponse getNewAddress(String seed, Integer index, boolean checksum, int total, boolean returnAll) {
        return IotaAPIUtils.getNewAddress(seed, index, checksum, total, returnAll);
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

            try {

                FileReader fileReader = new FileReader("node_config.properties");
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                final Properties nodeConfig = new Properties();
                nodeConfig.load(bufferedReader);

                if (nodeConfig.getProperty("iota.node.protocol") != null) {
                    protocol = nodeConfig.getProperty("iota.node.protocol");
                }

                if (nodeConfig.getProperty("iota.node.host") != null) {
                    host = nodeConfig.getProperty("iota.node.host");
                }

                if (nodeConfig.getProperty("iota.node.port") != null) {
                    port = nodeConfig.getProperty("iota.node.port");
                }

            } catch (IOException e1) {
                log.debug("node_config.properties not found. Rolling back for another solution...");
            }
            return (port != null && protocol != null && host != null);
        }

        private void checkEnviromentVariables() {
            protocol = env("IOTA_NODE_PROTOCOL", "http");
            host = env("IOTA_NODE_HOST", "localhost");
            port = env("IOTA_NODE_PORT", "14265");
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
