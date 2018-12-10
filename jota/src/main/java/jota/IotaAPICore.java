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
 * 
 * This class provides access to the Iota core API
 * Handles direct methods with the connected node(s), and does basic verification
 * 
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
    protected IotaAPICore(Builder<?> builder) {
        protocol = builder.protocol;
        host = builder.host;
        port = builder.port;
        localPoW = builder.localPoW;
        postConstruct();
    }

    protected static <tt> Response<tt> wrapCheckedException(Call<tt> call) throws ArgumentException {
        try {
            final Response<tt> res = call.execute();

            String error = "";

            if (res.errorBody() != null) {
                error = res.errorBody().string();
            }

            if (res.code() == 400) {
                throw new ArgumentException(error);

            } else if (res.code() == 401) {
                throw new IllegalAccessError("401 " + error);
            } else if (res.code() == 500) {
                throw new IllegalAccessError("500 " + error);
            } else if (!res.isSuccessful()) {
                //Timeout most likely
                throw new ArgumentException(res.message());
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
     * Returns information about this node.
     *
     * @return {@link GetNodeInfoResponse}
     * @throws ArgumentException 
     */
    public GetNodeInfoResponse getNodeInfo() throws ArgumentException {
        final Call<GetNodeInfoResponse> res = service.getNodeInfo(IotaCommandRequest.createNodeInfoRequest());
        return wrapCheckedException(res).body();
    }

    /**
     * Returns the set of neighbors you are connected with, as well as their activity statistics (or counters).
     * The activity counters are reset after restarting IRI.
     *
     * @return {@link GetNeighborsResponse}
     * @throws ArgumentException 
     */
    public GetNeighborsResponse getNeighbors() throws ArgumentException {
        final Call<GetNeighborsResponse> res = service.getNeighbors(IotaCommandRequest.createGetNeighborsRequest());
        return wrapCheckedException(res).body();
    }

    /**
     * Temporarily add a list of neighbors to your node.
     * The added neighbors will not be available after restart.
     * Add the neighbors to your config file 
     * or supply them in the <tt>-n</tt> command line option if you want to add them permanently.
     *
     * The URI (Unique Resource Identification) for adding neighbors is:
     * <b>udp://IPADDRESS:PORT</b>
     *
     * @param uris list of neighbors to add
     * @return {@link AddNeighborsResponse}
     * @throws ArgumentException 
     */
    public AddNeighborsResponse addNeighbors(String... uris) throws ArgumentException {
        final Call<AddNeighborsResponse> res = service.addNeighbors(IotaNeighborsRequest.createAddNeighborsRequest(uris));
        return wrapCheckedException(res).body();
    }

    /**
     * Temporarily removes a list of neighbors from your node.
     * The added neighbors will be added again after relaunching IRI.
     * Remove the neighbors from your config file or make sure you don't supply them in the -n command line option if you want to keep them removed after restart.
     *
     * The URI (Unique Resource Identification) for removing neighbors is:
     * <b>udp://IPADDRESS:PORT</b>
     *
     * @param uris The URIs of the neighbors we want to remove.
     * @return {@link RemoveNeighborsResponse}
     * @throws ArgumentException 
     */
    public RemoveNeighborsResponse removeNeighbors(String... uris) throws ArgumentException {
        final Call<RemoveNeighborsResponse> res = service.removeNeighbors(IotaNeighborsRequest.createRemoveNeighborsRequest(uris));
        return wrapCheckedException(res).body();
    }

    /**
     * Returns all tips currently known by this node.
     *
     * @return {@link GetTipsResponse}
     * @throws ArgumentException 
     */
    public GetTipsResponse getTips() throws ArgumentException {
        final Call<GetTipsResponse> res = service.getTips(IotaCommandRequest.createGetTipsRequest());
        return wrapCheckedException(res).body();
    }


    /**
     * <p>
     * Find the transactions which match the specified input and return.
     * All input values are lists, for which a list of return values (transaction hashes), in the same order, is returned for all individual elements.
     * The input fields can either be <tt>bundles</tt>, <tt>addresses</tt>, <tt>tags</tt> or <tt>approvees</tt>.
     * </p>
     * 
     * Using multiple of these input fields returns the intersection of the values.
     * Can error if the node found more transactions than the max transactions send amount
     *
     * @param addresses Array of hashes from addresses
     * @param tags Array of tags
     * @param approvees Array of transaction hashes
     * @param bundles Array of bundle hashes
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException 
     */
    public FindTransactionResponse findTransactions(String[] addresses, String[] tags, String[] approvees, String[] bundles) throws ArgumentException {

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
     * @param addresses An array of addresses.
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException 
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
     * @param bundles An array of bundles.
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException 
     */
    public FindTransactionResponse findTransactionsByBundles(final String... bundles) throws ArgumentException {
        return findTransactions(null, null, null, bundles);
    }

    /**
     * Find the transactions by approvees
     *
     * @param approvees An array of approveess.
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException 
     */
    public FindTransactionResponse findTransactionsByApprovees(final String... approvees) throws ArgumentException {
        return findTransactions(null, null, approvees, null);
    }


    /**
     * Find the transactions by tags
     *
     * @param digests A List of tags.
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException 
     */
    public FindTransactionResponse findTransactionsByDigests(final String... digests) throws ArgumentException {
        return findTransactions(null, digests, null, null);
    }


    /**
     * <p>
     * Get the inclusion states of a set of transactions.
     * This is for determining if a transaction was accepted and confirmed by the network or not.
     * You can search for multiple tips (and thus, milestones) to get past inclusion states of transactions.
     * </p>
     * <p>
     * This API call returns a list of boolean values in the same order as the submitted transactions.
     * Boolean values will be <tt>true</tt> for confirmed transactions, otherwise <tt>false</tt>.
     * </p>
     *
     * @param transactions Array of transactions you want to get the inclusion state for.
     * @param tips Array of tips (including milestones) you want to search for the inclusion state.
     * @return {@link GetInclusionStateResponse}
     * @throws ArgumentException when a transaction hash is invalid
     * @throws ArgumentException when a tip is invalid
     * @throws ArgumentException
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
     * Returns the raw transaction data (trytes) of a specific transaction.
     * These trytes can then be easily converted into the actual transaction object.
     * You can use {@link Transaction#Transaction(String)} for conversion to an object.
     *
     * @param hashes The transaction hashes you want to get trytes from.
     * @return {@link GetTrytesResponse}
     * @throws ArgumentException when a transaction hash is invalid
     * @throws ArgumentException
     */
    public GetTrytesResponse getTrytes(String... hashes) throws ArgumentException {

        if (!InputValidator.isArrayOfHashes(hashes)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }
        
        final Call<GetTrytesResponse> res = service.getTrytes(IotaGetTrytesRequest.createGetTrytesRequest(hashes));
        return wrapCheckedException(res).body();
    }

    /**
     * Tip selection which returns <tt>trunkTransaction</tt> and <tt>branchTransaction</tt>.
     * The input value <tt>depth</tt> determines how many milestones to go back for finding the transactions to approve.
     * The higher your <tt>depth</tt> value, the more work you have to do as you are confirming more transactions.
     * If the <tt>depth</tt> is too large (usually above 15, it depends on the node's configuration) an error will be returned.
     * The <tt>reference</tt> is an optional hash of a transaction you want to approve.
     * If it can't be found at the specified <tt>depth</tt> then an error will be returned.
     *
     * @param depth Number of bundles to go back to determine the transactions for approval.
     * @param reference Hash of transaction to start random-walk from.
     *                  This used to make sure the tips returned reference a given transaction in their past.
     *                  Can be <tt>null</tt>.
     * @return {@link GetTransactionsToApproveResponse}
     * @throws ArgumentException
     */
    public GetTransactionsToApproveResponse getTransactionsToApprove(Integer depth, String reference) throws ArgumentException {

        final Call<GetTransactionsToApproveResponse> res = service.getTransactionsToApprove(IotaGetTransactionsToApproveRequest.createIotaGetTransactionsToApproveRequest(depth, reference));
        return wrapCheckedException(res).body();
    }

    /**
     * Tip selection which returns <tt>trunkTransaction</tt> and <tt>branchTransaction</tt>.
     * The input value <tt>depth</tt> determines how many milestones to go back for finding the transactions to approve.
     * The higher your <tt>depth</tt> value, the more work you have to do as you are confirming more transactions.
     * If the <tt>depth</tt> is too large (usually above 15, it depends on the node's configuration) an error will be returned.
     * The <tt>reference</tt> is an optional hash of a transaction you want to approve.
     * If it can't be found at the specified <tt>depth</tt> then an error will be returned.
     *
     * @param depth Number of bundles to go back to determine the transactions for approval.
     * @return {@link GetTransactionsToApproveResponse}
     */
    public GetTransactionsToApproveResponse getTransactionsToApprove(Integer depth) throws ArgumentException {
        return getTransactionsToApprove(depth, null);
    }

    /**
     * <p>
     * Calculates the confirmed balance, as viewed by the specified <tt>tips</tt>. 
     * If you do not specify the referencing <tt>tips</tt>, 
     * the returned balance is based on the latest confirmed milestone.
     * In addition to the balances, it also returns the referencing <tt>tips</tt> (or milestone), 
     * as well as the index with which the confirmed balance was determined.
     * The balances are returned as a list in the same order as the addresses were provided as input.
     * </p>
     *
     * @param threshold The confirmation threshold between 0 and 100(inclusive). 
     *                  Should be set to 100 for getting balance by counting only confirmed transactions.
     * @param addresses The addresses where we will find the balance for.
     * @param tips The optional tips to find the balance through.
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException The the request was considered wrong in any way by the node
     * @throws ArgumentException
     */
    private GetBalancesResponse getBalances(Integer threshold, String[] addresses, String[] tips) throws ArgumentException {
        final Call<GetBalancesResponse> res = service.getBalances(IotaGetBalancesRequest.createIotaGetBalancesRequest(threshold, addresses, tips));
        return wrapCheckedException(res).body();
    }

    /**
     * <p>
     * Calculates the confirmed balance, as viewed by the specified <tt>tips</tt>. 
     * If you do not specify the referencing <tt>tips</tt>, 
     * the returned balance is based on the latest confirmed milestone.
     * In addition to the balances, it also returns the referencing <tt>tips</tt> (or milestone), 
     * as well as the index with which the confirmed balance was determined.
     * The balances are returned as a list in the same order as the addresses were provided as input.
     * </p>
     *
     * @param threshold The confirmation threshold between 0 and 100(inclusive). 
     *                  Should be set to 100 for getting balance by counting only confirmed transactions.
     * @param addresses The addresses where we will find the balance for.
     * @param tips The tips to find the balance through. Can be <tt>null</tt>
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException The the request was considered wrong in any way by the node
     */
    public GetBalancesResponse getBalances(Integer threshold, List<String> addresses, List<String> tips) throws ArgumentException {

        List<String> addressesWithoutChecksum = new ArrayList<>();

        for (String address : addresses) {
            String addressO = Checksum.removeChecksum(address);
            addressesWithoutChecksum.add(addressO);
        }
        String[] tipsArray = tips != null ? tips.toArray(new String[tips.size()]) : null;
        return getBalances(threshold, addressesWithoutChecksum.toArray(new String[addresses.size()]), tipsArray);
    }
    
    /**
     * <p>
     * Calculates the confirmed balance, as viewed by the latest solid milestone. 
     * In addition to the balances, it also returns the referencing <tt>milestone</tt>, 
     * and the index with which the confirmed balance was determined.
     * The balances are returned as a list in the same order as the addresses were provided as input.
     * </p>
     *
     * @param threshold The confirmation threshold, should be set to 100.
     * @param addresses The list of addresses you want to get the confirmed balance from.
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException
     */
    public GetBalancesResponse getBalances(Integer threshold, List<String> addresses) throws ArgumentException {
        return getBalances(threshold, addresses, null);
    }

    /**
     * Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.
     *
     * @param addresses List of addresses to check if they were ever spent from.
     * @return {@link WereAddressesSpentFromResponse}
     * @throws ArgumentException when an address is invalid
     * @throws ArgumentException
     */
    public WereAddressesSpentFromResponse wereAddressesSpentFrom(String... addresses) throws ArgumentException {
        if (!InputValidator.isAddressesArrayValid(addresses)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        final Call<WereAddressesSpentFromResponse> res = service.wereAddressesSpentFrom(IotaWereAddressesSpentFromRequest.create(addresses));
        return wrapCheckedException(res).body();
    }
    
    /**
     * Checks the consistency of the subtangle formed by the provided tails.
     *
     * @param tails The tails describing the subtangle.
     * @return {@link CheckConsistencyResponse}
     * @throws ArgumentException when a tail hash is invalid
     * @throws ArgumentException
     */
    public CheckConsistencyResponse checkConsistency(String... tails) throws ArgumentException {
        if (!InputValidator.isArrayOfHashes(tails)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        final Call<CheckConsistencyResponse> res = service.checkConsistency(IotaCheckConsistencyRequest.create(tails));
        return wrapCheckedException(res).body();
    }


    /**
     * <p>
     * Prepares the specified transactions (trytes) for attachment to the Tangle by doing Proof of Work.
     * You need to supply <tt>branchTransaction</tt> as well as <tt>trunkTransaction</tt>.
     * These are the tips which you're going to validate and reference with this transaction. 
     * These are obtainable by the <tt>getTransactionsToApprove</tt> API call.
     * </p>
     * <p>
     * The returned value is a different set of tryte values which you can input into 
     * <tt>broadcastTransactions</tt> and <tt>storeTransactions</tt>.
     * </p>
     * 
     * The last 243 trytes of the return value consist of the following:
     * <ul>
     * <li><code>trunkTransaction</code></li>
     * <li><code>branchTransaction</code></li>
     * <li><code>nonce</code></li>
     * </ul>
     * 
     * These are valid trytes which are then accepted by the network.
     * @param trunkTransaction A reference to an external transaction (tip) used as trunk.
     *                         The transaction with index 0 will have this tip in its trunk.
     *                         All other transactions reference the previous transaction in the bundle (Their index-1).
     *                         
     * @param branchTransaction A reference to an external transaction (tip) used as branch.
     *                          Each Transaction in the bundle will have this tip as their branch, except the last.
     *                          The last one will have the branch in its trunk.
     * @param minWeightMagnitude The amount of work we should do to confirm this transaction. 
     *                           Each 0-trit on the end of the transaction represents 1 magnitude. 
     *                           A 9-tryte represents 3 magnitudes, since a 9 is represented by 3 0-trits.
     *                           Transactions with a different minWeightMagnitude are compatible.
     * @param trytes The list of trytes to prepare for network attachment, by doing proof of work.
     * @return {@link GetAttachToTangleResponse}
     * @throws ArgumentException when a trunk or branch hash is invalid
     * @throws ArgumentException when the provided transaction trytes are invalid
     * @throws ArgumentException
     */
    public GetAttachToTangleResponse attachToTangle(String trunkTransaction, String branchTransaction, Integer minWeightMagnitude, String... trytes) throws ArgumentException {

        if (!InputValidator.isHash(trunkTransaction)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        if (!InputValidator.isHash(branchTransaction)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        if (!InputValidator.areTransactionTrytes(trytes)) {
            throw new ArgumentException(INVALID_TRYTES_INPUT_ERROR);
        }

        if (localPoW != null) {
            final String[] resultTrytes = new String[trytes.length];
            String previousTransaction = null;
            for (int i = 0; i < trytes.length; i++) {
                Transaction txn = new Transaction(trytes[i]);
                txn.setTrunkTransaction(previousTransaction == null ? trunkTransaction : previousTransaction);
                txn.setBranchTransaction(previousTransaction == null ? branchTransaction : trunkTransaction);
                if (txn.getTag().isEmpty() || txn.getTag().matches("9*")) {
                    txn.setTag(txn.getObsoleteTag());
                }
                
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
     * Interrupts and completely aborts the <tt>attachToTangle</tt> process.
     * 
     * @return {@link InterruptAttachingToTangleResponse}
     * @throws ArgumentException 
     */
    public InterruptAttachingToTangleResponse interruptAttachingToTangle() throws ArgumentException {
        final Call<InterruptAttachingToTangleResponse> res = service.interruptAttachingToTangle(IotaCommandRequest.createInterruptAttachToTangleRequest());
        return wrapCheckedException(res).body();
    }

    /**
     * Broadcast a list of transactions to all neighbors.
     * The trytes to be used for this call should be valid, attached transaction trytes.
     * These trytes are returned by <tt>attachToTangle</tt>, or by doing proof of work somewhere else.
     * 
     * @param trytes The list of transaction trytes to broadcast
     * @return {@link BroadcastTransactionsResponse}
     * @throws ArgumentException when the provided transaction trytes are invalid
     * @throws ArgumentException 
     */
    public BroadcastTransactionsResponse broadcastTransactions(String... trytes) throws ArgumentException {

        if (!InputValidator.isArrayOfAttachedTrytes(trytes)) {
            throw new ArgumentException(INVALID_ATTACHED_TRYTES_INPUT_ERROR);
        }

        final Call<BroadcastTransactionsResponse> res = service.broadcastTransactions(IotaBroadcastTransactionRequest.createBroadcastTransactionsRequest(trytes));
        return wrapCheckedException(res).body();
    }

    /**
     * Stores transactions in the local storage.
     * The trytes to be used for this call should be valid, attached transaction trytes.
     * These trytes are returned by <tt>attachToTangle</tt>, or by doing proof of work somewhere else.
     *
     * @param trytes Transaction data to be stored.
     * @return {@link StoreTransactionsResponse}
     * @throws ArgumentException 
     */
    public StoreTransactionsResponse storeTransactions(String... trytes) throws ArgumentException {
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

    @SuppressWarnings("unchecked")
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
