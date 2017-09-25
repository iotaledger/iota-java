package jota;

import jota.dto.request.*;
import jota.dto.response.*;
import jota.error.InvalidTrytesException;
import jota.model.Transaction;
import jota.utils.InputValidator;
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
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * This class provides access to the Iota core API
 *
 * @author Adrian
 */
public class IotaAPICore {

    private static final Logger log = LoggerFactory.getLogger(IotaAPICore.class);

    private IotaAPIService service;
    private String protocol, host, port;
    private IotaLocalPoW localPoW;

    /**
     * Build the API core.
     *
     * @param builder The builder.
     */
    protected IotaAPICore(final Builder builder) {
        protocol = builder.protocol;
        host = builder.host;
        port = builder.port;
        localPoW = builder.localPoW;
        postConstruct();
    }

    /**
     * @param call
     * @param <T>
     * @return
     */
    protected static <T> Response<T> wrapCheckedException(final Call<T> call) {
        try {
            final Response<T> res = call.execute();
            if (res.code() == 400) {
                throw new IllegalAccessError("400 " + res.errorBody().string());
            } else if (res.code() == 401) {
                throw new IllegalAccessError("401 " + res.errorBody().string());
            } else if (res.code() == 500) {
                throw new IllegalAccessError("500 " + res.errorBody().string());
            }
            return res;
        } catch (IOException e) {
            log.error("Execution of the API call raised exception. IOTA Node not reachable?", e);
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * @param env
     * @param def
     * @return
     */
    private static String env(String env, String def) {
        final String value = System.getenv(env);
        if (value == null) {
            log.warn("Environment variable '{}' is not defined, and actual value has not been specified. "
                    + "Rolling back to default value: '{}'", env, def);
            return def;
        }
        return value;
    }

    /**
     *
     */
    private void postConstruct() {

        final String nodeUrl = protocol + "://" + host + ":" + port;

        final OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.SECONDS)
                .connectTimeout(5000, TimeUnit.SECONDS)
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

    public GetBalancesResponse getBalances(Integer threshold, List<String> addresses) {
        return getBalances(threshold, addresses.toArray(new String[]{}));
    }

    public InterruptAttachingToTangleResponse interruptAttachingToTangle() {
        final Call<InterruptAttachingToTangleResponse> res = service.interruptAttachingToTangle(IotaCommandRequest.createInterruptAttachToTangleRequest());
        return wrapCheckedException(res).body();
    }

    public GetAttachToTangleResponse attachToTangle(String trunkTransaction, String branchTransaction, Integer minWeightMagnitude, String... trytes) throws InvalidTrytesException {
        if (!InputValidator.isArrayOfTrytes(trytes)) {
            throw new InvalidTrytesException();
        }

        if (localPoW != null) {
            final String[] resultTrytes = new String[trytes.length];
            String previousTransaction = null;
            for (int i = 0; i < trytes.length; i++) {
                Transaction txn = new Transaction(trytes[i]);
                txn.setTrunkTransaction(previousTransaction == null ? trunkTransaction : previousTransaction);
                txn.setBranchTransaction(previousTransaction == null ? branchTransaction : trunkTransaction);
                if (txn.getTag().isEmpty() || txn.getTag().matches("9*"))
                    txn.setTag(txn.getObsoleteTag());
                txn.setAttachmentTimestamp(System.currentTimeMillis());
                txn.setAttachmentTimestampLowerBound(0);
                txn.setAttachmentTimestampUpperBound(3_812_798_742_493L);
                resultTrytes[i] = localPoW.performPoW(txn.toTrytes(), minWeightMagnitude);
                previousTransaction = new Transaction(resultTrytes[i]).getHash();
            }
            return new GetAttachToTangleResponse(resultTrytes);
        }

        final Call<GetAttachToTangleResponse> res = service.attachToTangle(IotaAttachToTangleRequest.createAttachToTangleRequest(trunkTransaction, branchTransaction, minWeightMagnitude, trytes));
        return wrapCheckedException(res).body();
    }

    public StoreTransactionsResponse storeTransactions(String... trytes) {
        final Call<StoreTransactionsResponse> res = service.storeTransactions(IotaStoreTransactionsRequest.createStoreTransactionsRequest(trytes));
        return wrapCheckedException(res).body();
    }

    public BroadcastTransactionsResponse broadcastTransactions(String... trytes) {
        final Call<BroadcastTransactionsResponse> res = service.broadcastTransactions(IotaBroadcastTransactionRequest.createBroadcastTransactionsRequest(trytes));
        return wrapCheckedException(res).body();
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends Builder<T>> {
        private FileReader fileReader = null;
        private BufferedReader bufferedReader = null;
        private Properties nodeConfig = null;

        String protocol, host, port;
        IotaLocalPoW localPoW;

        public IotaAPICore build() {
            // resolution order: builder value, configuration file, default value

            if (null == protocol) {
                protocol = getFromConfigurationOrEnvironment("iota.node.protocol", "IOTA_NODE_PROTOCOL", "http");
            }

            if (null == host) {
                host = getFromConfigurationOrEnvironment("iota.node.host", "IOTA_NODE_HOST", "localhost");
            }

            if (null == port) {
                port = getFromConfigurationOrEnvironment("iota.node.port", "IOTA_NODE_PORT", "14265");
            }

            return new IotaAPICore(this);
        }

        private String getFromConfigurationOrEnvironment(String propertyKey, String envName, String defaultValue) {
            if (getNodeConfig().getProperty(propertyKey) != null) {
                return nodeConfig.getProperty(propertyKey);
            } else {
                return env(envName, defaultValue);
            }
        }

        private Properties getNodeConfig() {
            if (null != nodeConfig) {
                return nodeConfig;
            }

            nodeConfig = new Properties();
            if (null == fileReader) {
                try {
                    fileReader = new FileReader("node_config.properties");

                    if (null == bufferedReader) {
                        bufferedReader = new BufferedReader(fileReader);
                    }
                    nodeConfig.load(bufferedReader);
                } catch (IOException e) {
                    log.debug("node_config.properties not found. Rolling back for another solution...");
                }
            }

            return nodeConfig;
        }

        public T config(Properties properties) {
            nodeConfig = properties;
            return (T) this;
        }

        /**
         * @param host
         * @return
         */
        public T host(String host) {
            this.host = host;
            return (T) this;
        }

        /**
         * @param port
         * @return
         */
        public T port(String port) {
            this.port = port;
            return (T) this;
        }

        /**
         * @param protocol
         * @return
         */
        public T protocol(String protocol) {
            this.protocol = protocol;
            return (T) this;
        }

        /**
         * @param localPoW
         * @return
         */
        public T localPoW(IotaLocalPoW localPoW) {
            this.localPoW = localPoW;
            return (T) this;
        }
    }
}
