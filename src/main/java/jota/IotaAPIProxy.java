package jota;

import jota.dto.request.*;
import jota.dto.response.*;
import jota.error.ArgumentException;
import jota.error.NotEnoughBalanceException;
import jota.model.*;
import jota.utils.IotaAPIUtils;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * IotaAPIProxy Builder. Usage:
 * <p>
 * IotaApiProxy api = IotaApiProxy.Builder
 * .protocol("http")
 * .nodeAddress("localhost")
 * .port(12345)
 * .build();
 * <p>
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

    public BroadcastTransactionsResponse broadcastTransactions(String... trytes) {
        final Call<BroadcastTransactionsResponse> res = service.broadcastTransactions(IotaBroadcastTransactionRequest.createBroadcastTransactionsRequest(trytes));
        return wrapCheckedException(res).body();
    }

    // end of proxied calls.

    public GetBundleResponse getBundle(String transaction) {
        return IotaAPIUtils.getBundle(transaction);
    }

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
            allAddresses.subList(0, allAddresses.size() - 1).clear();
        }
        return GetNewAddressResponse.create(allAddresses);
    }

    public Transaction[] sendTrytes(String[] trytes, int depth, int minWeightMagnitude) {
        GetTransactionsToApproveResponse transactionsToApproveResponse = getTransactionsToApprove(depth);

        GetAttachToTangleResponse attachToTangleResponse =
                attachToTangle(transactionsToApproveResponse.getTrunkTransaction(),
                        transactionsToApproveResponse.getBranchTransactionToApprove(), minWeightMagnitude, trytes);

        broadcastTransactions(attachToTangleResponse.getTrytes());

        return analyzeTransactions(attachToTangleResponse.getTrytes());
    }

    private Transaction[] analyzeTransactions(String[] trytes) {
        throw new NotImplementedException("MISSING");
    }


    public Inputs getInputs(String seed, Integer start, Integer end, int threshold) throws ArgumentException, NotEnoughBalanceException {
        if (start == null || start < 0)
            start = 0;

        if (end == null || end < 0)
            end = 0;

        // If start value bigger than end, return error
        if (start > end)
            throw new ArgumentException();

        // or if difference between end and start is bigger than 500 keys
        if (end - start > 500)
            throw new ArgumentException();

        //  Case 1: start and end
        //
        //  If start and end is defined by the user, simply iterate through the keys
        //  and call getBalances
        if (end != 0) {
            String[] addresses = new String[end - start];

            for (int i = start; i < end; i++) {
                String address = IotaAPIUtils.newAddress(seed, i, false);
                addresses[i] = address;
            }

            return getBalancesAndFormat(addresses, start, end, threshold);
        }

        //  Case 2: iterate till threshold || end
        //
        //  Either start from index: 0 or start (if defined) until threshold is reached.
        //  Calls getNewAddress and deterministically generates and returns all addresses
        //  We then do getBalance, format the output and return it
        else {
            List<String> addressList = getNewAddress(seed, start, true, 0, true).getAddresses();
            String[] addresses = addressList.toArray(new String[addressList.size()]);
            return getBalancesAndFormat(addresses, start, end, threshold);
        }
    }

    private Inputs getBalancesAndFormat(String[] addresses) throws NotEnoughBalanceException {
        return getBalancesAndFormat(addresses, null, null, null);
    }

    private Inputs getBalancesAndFormat(String[] addresses, Integer start, Integer end, Integer threshold) throws NotEnoughBalanceException {
        GetBalancesResponse getBalancesResponse = getBalances(threshold, addresses);

        String[] balances = getBalancesResponse.getBalances();

        Inputs inputs = new Inputs(new ArrayList<Input>(), 0);

        boolean threshholdReached = false;

        for (int i = 0; i < addresses.length; i++) {
            if (Long.parseLong(balances[i]) > 0) {
                inputs.getInputsList().add(new Input(addresses[i], Long.parseLong(balances[i]), start + i));
                inputs.setTotalBalance(inputs.getTotalBalance() + inputs.getInputsList().get(i).getBalance());

                if (inputs.getTotalBalance() >= threshold) {
                    threshholdReached = true;
                    break;
                }
            }
        }

        if (threshholdReached)
            return inputs;
        else {
            throw new NotEnoughBalanceException();
        }
    }


    public Transaction[] SendTransfer(String seed, int depth, int minWeightMagnitude, Transfer[] transfers, Input[] inputs, String address) throws NotEnoughBalanceException, ArgumentException {
        String[] trytes = prepareTransfers(seed, transfers, inputs, address);
        return sendTrytes(trytes, depth, minWeightMagnitude);
    }

    public String[] prepareTransfers(String seed, Transfer[] transfers, Input[] inputs, String remainderAddress) throws NotEnoughBalanceException, ArgumentException {
        //InputValidator.checkTransferArray(transfers);
        // If message or tag is not supplied, provide it

        for (Transfer transfer : transfers) {

            if (transfer.getAddress() == null)
                transfer.getMessage().isEmpty();
            if (transfer.getTag() == null)
                transfer.getMessage().isEmpty();
        }

        // Create a new bundle
        Bundle bundle = new Bundle();
        long totalValue = 0;
        List signatureFragments = new ArrayList();
        String tag = "";
        //
        //  Iterate over all transfers, get totalValue
        //  and prepare the signatureFragments, message and tag
        //
        for (Transfer transfer : transfers) {
            int signatureMessageLength = 1;


            // If message longer than 2187 trytes, increase signatureMessageLength (add 2nd transaction)
            if (transfer.getMessage().length() > 2187) {
                // Get total length, message / maxLength (2187 trytes)
                signatureMessageLength += (int) Math.floor(((double) transfer.getMessage().length() / 2187));

                String msgCopy = transfer.getMessage();

                // While there is still a message, copy it
                while (msgCopy != null) {
                    String fragment = msgCopy.substring(0, 2187);
                    msgCopy = msgCopy.substring(2187, msgCopy.length());

                    // Pad remainder of fragment
                    for (int j = 0; fragment.length() < 2187; j++) {
                        fragment += '9';
                    }

                    signatureFragments.add(fragment);
                }
            } else {
                // Else, get single fragment with 2187 of 9's trytes
                String fragment = "";

                if (transfer.getMessage() != null) {
                    fragment = transfer.getMessage().substring(0, 2187);
                }

                for (int j = 0; fragment.length() < 2187; j++) {
                    fragment += '9';
                }

                signatureFragments.add(fragment);
            }

            // get current timestamp in seconds
            // var timestamp = Math.floor(Date.now() / 1000);
            long millis = System.currentTimeMillis() / 1000;

            // If no tag defined, get 27 tryte tag.
            tag = transfer.getTag() != null ? transfer.getTag() : "999999999999999999999999999";

            // Pad for required 27 tryte length
            for (int j = 0; tag.length() < 27; j++) {
                tag += '9';
            }

            // Add first entries to the bundle
            // Slice the address in case the user provided a checksummed one
            bundle.addEntry(signatureMessageLength, transfer.getAddress().substring(0, 81), transfer.getValue(), tag, millis);
            // Sum up total value
            totalValue += transfer.getValue();
        }

        // Get inputs if we are sending tokens
        if (totalValue != 0) {
            //  Case 1: user provided inputs
            //
            //  Validate the inputs by calling getBalances
            if (inputs != null) {
                // Get list if addresses of the provided inputs

                List<String> inputAddresses = new ArrayList();
                for (Input input : inputs) {
                    inputAddresses.add(input.getAddress());
                }

                GetBalancesResponse balances = getBalances(100, inputAddresses.toArray(new String[inputAddresses.size()]));

                List<Input> confirmedInputs = new ArrayList<Input>();

                long totalBalance = 0;
                for (int i = 0; i < balances.getBalances().length; i++) {
                    long thisBalance = Long.parseLong(balances.getBalances()[i]);
                    totalBalance += thisBalance;

                    // If input has balance, add it to confirmedInputs
                    if (thisBalance > 0) {
                        Input inputEl = inputs[i];
                        inputEl.setBalance(thisBalance);
                        confirmedInputs.add(inputEl);
                    }
                }

                // Return not enough balance error
                if (totalValue > totalBalance) {
                    //throw new NotEnoughBalanceException(totalBalance, totalValue);
                    throw new NotEnoughBalanceException();
                }

                addRemainder(seed, confirmedInputs, totalValue, bundle, tag, remainderAddress, signatureFragments);
            }

            //  Case 2: Get inputs deterministically
            //
            //  If no inputs provided, derive the addresses from the seed and
            //  confirm that the inputs exceed the threshold
            else {
                Inputs input = getInputs(seed, null, null, (int) totalValue);
                if (input != null && input.getInputsList() != null) {
                    addRemainder(seed, input.getInputsList(), totalValue, bundle, tag, remainderAddress, signatureFragments);
                } else {
                    throw new NotEnoughBalanceException();
                }
            }
        } else {
            // If no input required, don't sign and simply finalize the bundle
            bundle.finalize();
            bundle.addTrytes(signatureFragments);

            List<String> bundleTrytes = new ArrayList<>();

            for (Transaction trx : bundle.getTransactions()) {
                bundleTrytes.add(IotaAPIUtils.transactionTrytes(trx));
            }
            return bundleTrytes.toArray(new String[bundleTrytes.size()]);
        }
        return null;
    }


    private void addRemainder(String seed, List<Input> inputs, long totalValue, Bundle bundle, String tag,
                              String remainderAddress, List<String> signatureFragments) {
        for (Input input : inputs) {
            long thisBalance = input.getBalance();
            long totalTransferValue = totalValue;
            long toSubtract = 0 - thisBalance;
            long timestamp = (new Date()).getTime();

            // Add input as bundle entry
            bundle.addEntry(2, input.getAddress(), toSubtract, tag, timestamp);
            // If there is a remainder value
            // Add extra output to send remaining funds to

            if (thisBalance >= totalTransferValue) {
                long remainder = thisBalance - totalTransferValue;

                // If user has provided remainder address
                // Use it to send remaining funds to
                if (remainder > 0 && remainderAddress != null) {
                    // Remainder bundle entry
                    bundle.addEntry(1, remainderAddress, remainder, tag, timestamp);

                    // Final function for signing inputs
                    IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments);
                } else if (remainder > 0) {
                    // Generate a new Address by calling getNewAddress
                    String address = getNewAddress(seed, 0, false, 0, false).getAddresses().get(0);
                    // Remainder bundle entry
                    bundle.addEntry(1, address, remainder, tag, timestamp);

                    // Final function for signing inputs
                    IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments);
                } else {
                    // If there is no remainder, do not add transaction to bundle
                    // simply sign and return
                    IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments);
                }

                // If multiple inputs provided, subtract the totalTransferValue by
                // the inputs balance
            } else {
                totalTransferValue -= thisBalance;
            }
        }
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