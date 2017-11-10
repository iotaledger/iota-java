package jota;

import jota.dto.request.*;
import jota.dto.response.*;
import jota.error.ArgumentException;
import jota.model.Transaction;
import jota.utils.Checksum;
import jota.utils.InputValidator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static jota.utils.Constants.*;

/**
 * This class provides access to the Iota core API
 *
 * @author Adrian
 */
public class IotaAPICore {

    // version header
    private static final String X_IOTA_API_VERSION_HEADER_NAME = "X-IOTA-API-Version";
    private static final String X_IOTA_API_VERSION_HEADER_VALUE = "1";

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

    protected static <T> Response<T> wrapCheckedException(final Call<T> call) {
        try {
            final Response<T> res = call.execute();

            String error = "";

            if (res.errorBody() != null) {
                error = res.errorBody().string();
            }

            if (res.code() == 400) {
                try {
                    throw new ArgumentException(error);
                } catch (ArgumentException e) {
                    e.printStackTrace();
                }

            } else if (res.code() == 401) {
                throw new IllegalAccessError("401 " + error);
            } else if (res.code() == 500) {
                throw new IllegalAccessError("500 " + error);
            }
            return res;
        } catch (IOException e) {
            log.error("Execution of the API call raised exception. IOTA Node not reachable?", e);
            throw new IllegalStateException(e.getMessage());
        }

    }

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
     * added header for IRI
     */
    private void postConstruct() {

        final String nodeUrl = protocol + "://" + host + ":" + port;

        // Create OkHttpBuilder
        final OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest;

                        newRequest = request.newBuilder()
                                .addHeader(X_IOTA_API_VERSION_HEADER_NAME, X_IOTA_API_VERSION_HEADER_VALUE)
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .connectTimeout(5000, TimeUnit.SECONDS)
                .build();

        // use client to create Retrofit service
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(nodeUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(IotaAPIService.class);

        log.debug("Jota-API Java proxy pointing to node url: '{}'", nodeUrl);
    }

    /**
     * Get the node information.
     *
     * @return The information about the node.
     */
    public GetNodeInfoResponse getNodeInfo() {
        final Call<GetNodeInfoResponse> res = service.getNodeInfo(IotaCommandRequest.createNodeInfoRequest());
        return wrapCheckedException(res).body();
    }

    /**
     * Get the list of neighbors from the node.
     *
     * @return The set of neighbors the node is connected with.
     */
    public GetNeighborsResponse getNeighbors() {
        final Call<GetNeighborsResponse> res = service.getNeighbors(IotaCommandRequest.createGetNeighborsRequest());
        return wrapCheckedException(res).body();
    }

    /**
     * Add a list of neighbors to the node.
     *
     * @param uris The list of URI elements.
     */
    public AddNeighborsResponse addNeighbors(String... uris) {
        final Call<AddNeighborsResponse> res = service.addNeighbors(IotaNeighborsRequest.createAddNeighborsRequest(uris));
        return wrapCheckedException(res).body();
    }

    /**
     * Removes a list of neighbors from the node.
     *
     * @param uris The list of URI elements.
     */
    public RemoveNeighborsResponse removeNeighbors(String... uris) {
        final Call<RemoveNeighborsResponse> res = service.removeNeighbors(IotaNeighborsRequest.createRemoveNeighborsRequest(uris));
        return wrapCheckedException(res).body();
    }

    /**
     * Get the list of latest tips (unconfirmed transactions).
     *
     * @return The the list of tips.
     */
    public GetTipsResponse getTips() {
        final Call<GetTipsResponse> res = service.getTips(IotaCommandRequest.createGetTipsRequest());
        return wrapCheckedException(res).body();
    }


    /**
     * Find the transactions which match the specified input
     *
     * @return The transaction hashes which are returned depend on the input.
     */
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

    /**
     * Find the transactions by addresses
     *
     * @param addresses A List of addresses.
     * @return The transaction hashes which are returned depend on the input.
     */
    public FindTransactionResponse findTransactionsByAddresses(final String... addresses) throws ArgumentException {
        List<String> addressesWithoutChecksum = new ArrayList<>();

        for (String address : addresses) {
            String addressO = Checksum.removeChecksum(address);
            addressesWithoutChecksum.add(addressO);
        }

        return findTransactions(addressesWithoutChecksum.toArray(new String[addressesWithoutChecksum.size()]), null, null, null);
    }

    /**
     * Find the transactions by bundles
     *
     * @param bundles A List of bundles.
     * @return The transaction hashes which are returned depend on the input.
     */
    public FindTransactionResponse findTransactionsByBundles(final String... bundles) {
        return findTransactions(null, null, null, bundles);
    }

    /**
     * Find the transactions by approvees
     *
     * @param approvees A List of approvess.
     * @return The transaction hashes which are returned depend on the input.
     */
    public FindTransactionResponse findTransactionsByApprovees(final String... approvees) {
        return findTransactions(null, null, approvees, null);
    }


    /**
     * Find the transactions by digests
     *
     * @param digests A List of digests.
     * @return The transaction hashes which are returned depend on the input.
     */
    public FindTransactionResponse findTransactionsByDigests(final String... digests) {
        return findTransactions(null, digests, null, null);
    }


    /**
     * Get the inclusion states of a set of transactions. This is for determining if a transaction was accepted and confirmed by the network or not.
     * Search for multiple tips (and thus, milestones) to get past inclusion states of transactions.
     *
     * @param transactions The ist of transactions you want to get the inclusion state for.
     * @param tips         ThelList of tips (including milestones) you want to search for the inclusion state.
     * @return The inclusion states of a set of transactions.
     */
    public GetInclusionStateResponse getInclusionStates(String[] transactions, String[] tips) throws ArgumentException {

        if (!InputValidator.isArrayOfHashes(transactions)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        if (!InputValidator.isArrayOfHashes(tips)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }


        final Call<GetInclusionStateResponse> res = service.getInclusionStates(IotaGetInclusionStateRequest
                .createGetInclusionStateRequest(transactions, tips));
        return wrapCheckedException(res).body();
    }

    /**
     * Returns the raw trytes data of a transaction.
     *
     * @param hashes The of transaction hashes of which you want to get trytes from.
     * @return The the raw transaction data (trytes) of a specific transaction.
     */
    public GetTrytesResponse getTrytes(String... hashes) throws ArgumentException {

        if (!InputValidator.isArrayOfHashes(hashes)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        final Call<GetTrytesResponse> res = service.getTrytes(IotaGetTrytesRequest.createGetTrytesRequest(hashes));
        return wrapCheckedException(res).body();
    }

    /**
     * Tip selection which returns trunkTransaction and branchTransaction. The input value is the latest coordinator milestone, as provided through the getNodeInfo API call.
     *
     * @param depth The number of bundles to go back to determine the transactions for approval.
     * @return The Tip selection which returns trunkTransaction and branchTransaction
     */
    public GetTransactionsToApproveResponse getTransactionsToApprove(Integer depth) {
        final Call<GetTransactionsToApproveResponse> res = service.getTransactionsToApprove(IotaGetTransactionsToApproveRequest.createIotaGetTransactionsToApproveRequest(depth));
        return wrapCheckedException(res).body();
    }

    /**
     * Similar to getInclusionStates.
     *
     * @param threshold The confirmation threshold, should be set to 100.
     * @param addresses The array list of addresses you want to get the confirmed balance from.
     * @return The confirmed balance which a list of addresses have at the latest confirmed milestone.
     */
    private GetBalancesResponse getBalances(Integer threshold, String[] addresses) {
        final Call<GetBalancesResponse> res = service.getBalances(IotaGetBalancesRequest.createIotaGetBalancesRequest(threshold, addresses));
        return wrapCheckedException(res).body();
    }

    /**
     * Similar to getInclusionStates.
     *
     * @param threshold The confirmation threshold, should be set to 100.
     * @param addresses The list of addresses you want to get the confirmed balance from.
     * @return The confirmed balance which a list of addresses have at the latest confirmed milestone.
     */
    public GetBalancesResponse getBalances(Integer threshold, List<String> addresses) throws ArgumentException {

        List<String> addressesWithoutChecksum = new ArrayList<>();

        for (String address : addresses) {
            String addressO = Checksum.removeChecksum(address);
            addressesWithoutChecksum.add(addressO);
        }
        return getBalances(threshold, addressesWithoutChecksum.toArray(new String[]{}));
    }

    /**
     * Attaches the specified transactions (trytes) to the Tangle by doing Proof of Work.
     *
     * @param trunkTransaction The trunk transaction to approve.
     * @param branchTransaction The branch transaction to approve.
     * @param minWeightMagnitude The Proof of Work intensity.
     * @param trytes A List of trytes (raw transaction data) to attach to the tangle.
     */
    public GetAttachToTangleResponse attachToTangle(String trunkTransaction, String branchTransaction, Integer minWeightMagnitude, String... trytes) throws ArgumentException {

        if (!InputValidator.isHash(trunkTransaction)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        if (!InputValidator.isHash(branchTransaction)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        if (!InputValidator.isArrayOfTrytes(trytes)) {
            throw new ArgumentException(INVALID_TRYTES_INPUT_ERROR);
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

    /**
     * Interrupts and completely aborts the attachToTangle process.
     */
    public InterruptAttachingToTangleResponse interruptAttachingToTangle() {
        final Call<InterruptAttachingToTangleResponse> res = service.interruptAttachingToTangle(IotaCommandRequest.createInterruptAttachToTangleRequest());
        return wrapCheckedException(res).body();
    }

    /**
     * Broadcast a list of transactions to all neighbors. The input trytes for this call are provided by attachToTangle.
     *
     * @param trytes The list of raw data of transactions to be rebroadcast.
     */
    public BroadcastTransactionsResponse broadcastTransactions(String... trytes) throws ArgumentException {

        if (!InputValidator.isArrayOfAttachedTrytes(trytes)) {
            throw new ArgumentException(INVALID_ATTACHED_TRYTES_INPUT_ERROR);
        }

        final Call<BroadcastTransactionsResponse> res = service.broadcastTransactions(IotaBroadcastTransactionRequest.createBroadcastTransactionsRequest(trytes));
        return wrapCheckedException(res).body();
    }

    /**
     * Store transactions into the local storage. The trytes to be used for this call are returned by attachToTangle.
     *
     * @param trytes The list of raw data of transactions to be rebroadcast.
     */
    public StoreTransactionsResponse storeTransactions(String... trytes) {
        final Call<StoreTransactionsResponse> res = service.storeTransactions(IotaStoreTransactionsRequest.createStoreTransactionsRequest(trytes));
        return wrapCheckedException(res).body();
    }

    /**
     * Gets the protocol.
     *
     * @return The protocol to use when connecting to the remote node.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Gets the host.
     *
     * @return The host you want to connect to.
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets the port.
     *
     * @return The port of the host you want to connect to.
     */
    public String getPort() {
        return port;
    }

    public static class Builder<T extends Builder<T>> {
        String protocol, host, port;
        IotaLocalPoW localPoW;
        private FileReader fileReader = null;
        private BufferedReader bufferedReader = null;
        private Properties nodeConfig = null;

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
                    fileReader = new FileReader("../node_config.properties");

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

        public T host(String host) {
            this.host = host;
            return (T) this;
        }

        public T port(String port) {
            this.port = port;
            return (T) this;
        }

        public T protocol(String protocol) {
            this.protocol = protocol;
            return (T) this;
        }

        public T localPoW(IotaLocalPoW localPoW) {
            this.localPoW = localPoW;
            return (T) this;
        }
    }
}
