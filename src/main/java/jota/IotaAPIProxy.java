package jota;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jota.dto.request.IotaAttachToTangleRequest;
import jota.dto.request.IotaBroadcastTransactionRequest;
import jota.dto.request.IotaCommandRequest;
import jota.dto.request.IotaFindTransactionsRequest;
import jota.dto.request.IotaGetBalancesRequest;
import jota.dto.request.IotaGetInclusionStateRequest;
import jota.dto.request.IotaGetTransactionsToApproveRequest;
import jota.dto.request.IotaGetTrytesRequest;
import jota.dto.request.IotaNeighborsRequest;
import jota.dto.request.IotaStoreTransactionsRequest;
import jota.dto.response.AddNeighborsResponse;
import jota.dto.response.BroadcastTransactionsResponse;
import jota.dto.response.FindTransactionResponse;
import jota.dto.response.GetAttachToTangleResponse;
import jota.dto.response.GetBalancesAndFormatResponse;
import jota.dto.response.GetBalancesResponse;
import jota.dto.response.GetBundleResponse;
import jota.dto.response.GetInclusionStateResponse;
import jota.dto.response.GetNeighborsResponse;
import jota.dto.response.GetNewAddressResponse;
import jota.dto.response.GetNodeInfoResponse;
import jota.dto.response.GetTipsResponse;
import jota.dto.response.GetTransactionsToApproveResponse;
import jota.dto.response.GetTrytesResponse;
import jota.dto.response.InterruptAttachingToTangleResponse;
import jota.dto.response.RemoveNeighborsResponse;
import jota.dto.response.StoreTransactionsResponse;
import jota.model.Bundle;
import jota.model.Input;
import jota.model.Transaction;
import jota.model.Transfer;
import jota.utils.Converter;
import jota.utils.InputValidator;
import jota.utils.IotaAPIUtils;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    
    public GetBalancesResponse getBalances(Integer threshold, List<String> addresses) {
        return getBalances(threshold, addresses.toArray(new String[] {}));
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

    public BroadcastTransactionsResponse broadcastTransactions(String... trytes) {
        final Call<BroadcastTransactionsResponse> res = service.broadcastTransactions(IotaBroadcastTransactionRequest.createBroadcastTransactionsRequest(trytes));
        return wrapCheckedException(res).body();
    }

    // end of proxied calls.

    /**
     * Generates a new address from a seed and returns the remainderAddress.
     * This is either done deterministically, or by providing the index of the new remainderAddress
     *
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred
     * @param index     Optional (default null). Key index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @param checksum  Optional (default false). Adds 9-tryte address checksum
     * @param total     Optional (default 1)Total number of addresses to generate
     * @param returnAll If true, it returns all addresses which were deterministically generated (until findTransactions returns null)
     * @return an array of strings with the specifed number of addresses
     */
    public GetNewAddressResponse getNewAddress(final String seed, final int index, final boolean checksum, final int total, final boolean returnAll) {

        final List<String> allAddresses = new ArrayList<>();
        
        // If total number of addresses to generate is supplied, simply generate
        // and return the list of all addresses
        if (total != 0) {
            for (int i = index; i < index + total; i++) {
                allAddresses.add(IotaAPIUtils.newAddress(seed, i, checksum));
            }
            return GetNewAddressResponse.create(allAddresses);
        }
        // No total provided: Continue calling findTransactions to see if address was 
        // already created if null, return list of addresses
        for (int i = index; ; i++) {
            
        	final String newAddress = IotaAPIUtils.newAddress(seed, i, checksum);
            final FindTransactionResponse response = findTransactionsByAddresses(new String[]{newAddress});
            
            allAddresses.add(newAddress);
            if (response.getHashes().length == 0) {
                break;
            }
        }

        // If !returnAll return only the last address that was generated
        if (!returnAll) {
        	allAddresses.subList(0, allAddresses.size()-1).clear();
        }
        return GetNewAddressResponse.create(allAddresses);        
    }
    
    /*
     * newAddress
     * broadcastAndStore
     * sendTrytes
     * prepareTransfers
     * getInputs
       
      getTransfers
      sendTransfer
      getBundle
    
      getTransactionsObjects
      findTransactionObjects
      getLatestInclusion
       
      replayBundle
      broadcastBundle
      getAccountData
    */
    
    /**
     * 
     * @param trytes
     * @return a StoreTransactionsResponse
     */
    public StoreTransactionsResponse broadcastAndStore(final String ... trytes) {
        
        try {
            broadcastTransactions(trytes);
        } catch (Exception e) {
            log.error("Impossible to broadcastAndStore, aborting.", e);
            throw new IllegalStateException("BroadcastAndStore Illegal state Exception");
        }
        return storeTransactions(trytes);
    }
    
    /**
     * Facade method: Gets transactions to approve, attaches to Tangle, broadcasts and stores
     * @param {array} trytes
     * @param {int} depth
     * @param {int} minWeightMagnitude
     * @return 
     */
    public List<Transaction> sendTrytes(final String trytes, final int minWeightMagnitude) {
        
        final GetTransactionsToApproveResponse txs = getTransactionsToApprove(minWeightMagnitude);
        
        // attach to tangle - do pow
        final GetAttachToTangleResponse res = attachToTangle(txs.getTrunkTransaction(), txs.getBranchTransaction(), minWeightMagnitude, trytes);
    
        try {
            broadcastAndStore(res.getTrytes());
        } catch (Exception e) {
            log.error("Impossible to sendTrytes, aborting.", e);
            throw new IllegalStateException("sendTrytes Illegal state Exception");
        }
        
        //return Arrays.stream(res.getTrytes()).map(Converter::transactionObject).collect(Collectors.toList());
        final List<Transaction> trx = new ArrayList<>();
        
        for (final String tx : Arrays.asList(res.getTrytes())) {
            trx.add(Converter.transactionObject(tx));
        }
        return trx;
    }
    
    public List<Transaction> findAndGetTxs(final String addresses) {

        final FindTransactionResponse res = findTransactionsByAddresses(addresses);
        return getTransactionsObjects(res.getHashes());
    }
    
    /**
    *   Wrapper function for getTrytes and transactionObjects
    *   gets the trytes and transaction object from a list of transaction hashes
    *
    *   @method getTransactionsObjects
    *   @param {array} hashes
     * @return 
    *   @returns {function} callback
    *   @returns {object} success
    **/
    public List<Transaction> getTransactionsObjects(String ... hashes) {

        if (!InputValidator.isArrayOfHashes(hashes)) {
            throw new IllegalStateException("Not an Array of Hashes: " + Arrays.toString(hashes));
        }

        final GetTrytesResponse trytesResponse = getTrytes(hashes);
        
        final List<Transaction> trxs = new ArrayList<>();
        
        for (final String tryte : trytesResponse.getTrytes()) {
            trxs.add(Converter.transactionObject(tryte));
        }
        return trxs;
    }

    /**
    *   Prepares transfer by generating bundle, finding and signing inputs
    *
    *   @method prepareTransfers
    *   @param {string} seed
    *   @param {object} transfers
    *   @param {object} options
    *       @property {array} inputs Inputs used for signing. Needs to have correct keyIndex and address value
    *       @property {string} address Remainder address
    *   @param {function} callback
     * @return 
    *   @returns {array} trytes Returns bundle trytes
    **/
    public List<String> prepareTransfers(final String seed, final List<Transfer> transfers, String remainder, List<Input> inputs) {

        // Input validation of transfers object
        if (!InputValidator.isTransfersCollectionCorrect(transfers)) {
            throw new IllegalStateException("Invalid Transfer");
        }

        // Create a new bundle
        final Bundle bundle = new Bundle();
        final List<String> signatureFragments = new ArrayList<>();
        
        int totalValue = 0;
        String tag;

        //  Iterate over all transfers, get totalValue
        //  and prepare the signatureFragments, message and tag
        for (final Transfer transfer : transfers) {

            int signatureMessageLength = 1;

            // If message longer than 2187 trytes, increase signatureMessageLength (add 2nd transaction)
            if (transfer.getMessage().length() > 2187) {

                // Get total length, message / maxLength (2187 trytes)
                signatureMessageLength += Math.floor(transfer.getMessage().length() / 2187);

                String msgCopy = new String(transfer.getMessage());

                // While there is still a message, copy it
                while (!msgCopy.isEmpty()) {
                    
                    String fragment = StringUtils.substring(msgCopy, 0, 2187);
                    msgCopy = StringUtils.substring(msgCopy, 2187, msgCopy.length());

                    // Pad remainder of fragment
                    for (int j = 0; fragment.length() < 2187; j++) {
                        fragment += "9";
                    }

                    signatureFragments.add(fragment);
                }
            } else {
                // Else, get single fragment with 2187 of 9's trytes
                String fragment = StringUtils.substring(transfer.getMessage(), 0, 2187);

                for (int j = 0; fragment.length() < 2187; j++) {
                    fragment += '9';
                }

                signatureFragments.add(fragment);
            }

            // get current timestamp in seconds
            long timestamp = (long) Math.floor(Calendar.getInstance().getTimeInMillis() / 1000);

            // If no tag defined, get 27 tryte tag.
            tag = transfer.getTag().isEmpty() ? "999999999999999999999999999" : transfer.getTag();

            // Pad for required 27 tryte length
            for (int j = 0; tag.length() < 27; j++) {
                tag += '9';
            }

            // Add first entry to the bundle
            bundle.addEntry(signatureMessageLength, transfer.getAddress(), transfer.getValue(), tag, timestamp);
            // Sum up total value
            totalValue += transfer.getValue();
        }

        // Get inputs if we are sending tokens
        if (totalValue != 0) {

            //  Case 1: user provided inputs
            //  Validate the inputs by calling getBalances
            if (!inputs.isEmpty()) {

                // Get list if addresses of the provided inputs
                List<String> inputsAddresses = new ArrayList<>();
                for (final Input i : inputs) {
                    inputsAddresses.add(i.getAddress());
                }

                GetBalancesResponse resbalances = getBalances(100, inputsAddresses);
                String[] balances = resbalances.getBalances();
                

                List<Input> confirmedInputs = new ArrayList<>();
                int totalBalance = 0; int i = 0;
                for (String balance : balances) {
                    long thisBalance = Integer.parseInt(balance);
                    totalBalance += thisBalance;

                    // If input has balance, add it to confirmedInputs
                    if (thisBalance > 0) {
                        Input inputEl = inputs.get(i++);
                        inputEl.setBalance(thisBalance);
                        confirmedInputs.add(inputEl);
                    }
                }

                // Return not enough balance error
                if (totalValue > totalBalance) {
                    throw new IllegalStateException("Not enough balance");
                }

                return IotaAPIUtils.signInputsAndReturn(seed, confirmedInputs, bundle, signatureFragments);
            }

            //  Case 2: Get inputs deterministically
            //
            //  If no inputs provided, derive the addresses from the seed and
            //  confirm that the inputs exceed the threshold
            else {

                GetBalancesAndFormatResponse newinputs = getInputs(seed, Collections.EMPTY_LIST, 0, 0, totalValue);
                // If inputs with enough balance
                return IotaAPIUtils.signInputsAndReturn(seed, newinputs.getInput(), bundle, signatureFragments);
            }
        } else {

            // If no input required, don't sign and simply finalize the bundle
            bundle.finalize();
            bundle.addTrytes(signatureFragments);

            List<Transaction> trxb = bundle.getTransactions();
            List<String> bundleTrytes = new ArrayList<>();
            for (Transaction tx : trxb) {
                jota.utils.IotaAPIUtils.transactionTrytes(tx);
            }
            Collections.reverse(bundleTrytes);
            return bundleTrytes;
        }
    }
    
    /**
    *   Gets the inputs of a seed
    *
    *   @method getInputs
    *   @param {string} seed
    *   @param {object} options
    *       @property {int} start Starting key index
    *       @property {int} end Ending key index
    *       @property {int} threshold Min balance required
    *   @param {function} callback
    **/
    public GetBalancesAndFormatResponse getInputs(final String seed, final List<String> balances, int start, int end, int threshold) {

        // validate the seed
        if (!InputValidator.isTrytes(seed, 0)) {
            throw new IllegalStateException("Invalid Seed");
        }

        // If start value bigger than end, return error
        // or if difference between end and start is bigger than 500 keys
        if (start > end || end > (start + 500)) {
            throw new IllegalStateException("Invalid inputs provided");
        }

        //  Case 1: start and end
        //
        //  If start and end is defined by the user, simply iterate through the keys
        //  and call getBalances
        if (end != 0) {

            List<String> allAddresses = new ArrayList<>();

            for (int i = start; i < end; i++) {

                String address = IotaAPIUtils.newAddress(seed, i, false);
                allAddresses.add(address);
            }

            return getBalanceAndFormat(allAddresses, balances, threshold, start, end);
        }
        //  Case 2: iterate till threshold || end
        //
        //  Either start from index: 0 or start (if defined) until threshold is reached.
        //  Calls getNewAddress and deterministically generates and returns all addresses
        //  We then do getBalance, format the output and return it
        else {

            final GetNewAddressResponse res = getNewAddress(seed, start, false, 0, true);
            return getBalanceAndFormat(res.getAddresses(), balances, threshold, start, end);
        }
    }

    //  Calls getBalances and formats the output
    //  returns the final inputsObject then
    public GetBalancesAndFormatResponse getBalanceAndFormat(final List<String> addresses, 
            final List<String> balances, long threshold, int start, int end) {

        GetBalancesResponse bres = getBalances(100, addresses);

        // If threshold defined, keep track of whether reached or not
        // else set default to true
        boolean thresholdReached = threshold != 0 ? false : true; int i = -1;
        
        List<Input> inputs = new ArrayList<>();
        long totalBalance = 0;
        
        for (String address : addresses) {
            
            long balance = Long.parseLong(balances.get(++i));
            
            if (balance > 0) {
                final Input newEntry = new Input(address, balance, start+i);
                                        
                inputs.add(newEntry);
                // Increase totalBalance of all aggregated inputs
                totalBalance += balance;

                if (thresholdReached == false && totalBalance >= threshold) {
                    thresholdReached = true;
                    break;
                }
            }
        }

        if (thresholdReached) {
            return GetBalancesAndFormatResponse.create(inputs, totalBalance);
        } 
        throw new IllegalStateException("Not enough balance");
    }
    
    public GetBundleResponse getBundle(String transaction) {
        return null; //IotaAPIUtils.getBundle(transaction);
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
