package jota;

import jota.dto.request.*;
import jota.dto.response.*;
import jota.error.ArgumentException;
import jota.error.InvalidBundleException;
import jota.error.InvalidSignatureException;
import jota.model.*;
import jota.pow.Curl;
import jota.utils.Converter;
import jota.utils.InputValidator;
import jota.utils.IotaAPIUtils;
import jota.utils.Signing;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
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
        return getBalances(threshold, addresses.toArray(new String[]{}));
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

        List<String> allAddresses = new ArrayList<>();

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
            final FindTransactionResponse response = findTransactionsByAddresses(newAddress);

            allAddresses.add(newAddress);
            if (response.getHashes().length == 0) {
                break;
            }
        }

        // If !returnAll return only the last address that was generated
        if (!returnAll) {

            //allAddresses = allAddresses.subList(allAddresses.size() - 2, allAddresses.size() - 1);
            allAddresses = allAddresses.subList(allAddresses.size() - 1, allAddresses.size());
        }
        return GetNewAddressResponse.create(allAddresses);
    }

    /**
     * @param {string}   seed
     * @param {object}   options
     * @param {function} callback
     * @method getTransfers
     * @property {int} start Starting key index
     * @property {int} end Ending key index
     * @property {bool} inclusionStates returns confirmation status of all transactions
     * @returns {object} success
     **/
    public GetTransferResponse getTransfers(String seed, Integer start, Integer end, Boolean inclusionStates) throws ArgumentException, InvalidBundleException, InvalidSignatureException {
        // validate & if needed pad seed
        if ( (seed = InputValidator.validateSeed(seed)) == null) {
            throw new IllegalStateException("Invalid Seed");
        }

        start = start != null ? 0 : start;
        end = end == null ? null : end;
        inclusionStates = inclusionStates != null ? inclusionStates : null;

        if (start > end || end > (start + 500)) {
            throw new ArgumentException();
        }
        StopWatch sw = new StopWatch();
        sw.start();
        System.out.println("GetTransfer started");
        GetNewAddressResponse gnr = getNewAddress(seed, start, false, end == null ? end - start : end, true);
        if (gnr != null && gnr.getAddresses() != null) {
            System.out.println("GetTransfers after getNewAddresses " + sw.getTime() + " ms");
            Bundle[] bundles = bundlesFromAddresses(gnr.getAddresses().toArray(new String[gnr.getAddresses().size()]), inclusionStates);
            System.out.println("GetTransfers after bundlesFromAddresses " + sw.getTime() + " ms");
            sw.stop();

            return GetTransferResponse.create(bundles);
        }
        sw.stop();
        return null;
    }

    public Bundle[] bundlesFromAddresses(String[] addresses, Boolean inclusionStates) throws ArgumentException, InvalidBundleException, InvalidSignatureException {

        List<Transaction> trxs = findTransactionObjects(addresses);
        // set of tail transactions
        List<String> tailTransactions = new ArrayList<>();
        List<String> nonTailBundleHashes = new ArrayList<>();

        for (Transaction trx : trxs) {
            // Sort tail and nonTails
            if (Long.parseLong(trx.getCurrentIndex()) == 0) {
                tailTransactions.add(trx.getHash());
            } else {
                if (nonTailBundleHashes.indexOf(trx.getBundle()) == -1) {
                    nonTailBundleHashes.add(trx.getBundle());
                }
            }
        }

        List<Transaction> bundleObjects = findTransactionObjectsByBundle(nonTailBundleHashes.toArray(new String[nonTailBundleHashes.size()]));
        for (Transaction trx : bundleObjects) {
            // Sort tail and nonTails
            if (Long.parseLong(trx.getCurrentIndex()) == 0) {
                if (tailTransactions.indexOf(trx.getHash()) == -1) {
                    tailTransactions.add(trx.getHash());
                }
            }
        }

        List<Bundle> finalBundles = new ArrayList<>();
        String[] tailTxArray = tailTransactions.toArray(new String[tailTransactions.size()]);

        // If inclusionStates, get the confirmation status
        // of the tail transactions, and thus the bundles
        GetInclusionStateResponse gisr = null;
        if (inclusionStates) {
            try {
                gisr = getLatestInclusion(tailTxArray);
            } catch (IllegalAccessError e) {

            }
            if (gisr == null || gisr.getStates() == null || gisr.getStates().length == 0) return null;
        }
        for (String trx : tailTxArray) {
            try {
                GetBundleResponse bundleResponse = getBundle(trx);
                // TODO: review possibly dirty WA
                if (bundleResponse == null) continue;
                Bundle gbr = new Bundle(bundleResponse.getTransactions(), bundleResponse.getTransactions().size());
                if (gbr != null && gbr.getTransactions() != null) {
                    if (inclusionStates) {
                        boolean thisInclusion = gisr.getStates()[Arrays.asList(tailTxArray).indexOf(trx)];
                        for (Transaction t : gbr.getTransactions()) {
                            t.setPersistence(thisInclusion);
                        }
                    }
                    finalBundles.add(gbr);
                }
                // If error returned from getBundle, simply ignore it because the bundle was most likely incorrect
            } catch (InvalidBundleException | ArgumentException | InvalidSignatureException e) {
                log.warn("GetBundleError: ", e);
            }

        }

        Collections.sort(finalBundles, new Comparator<Bundle>() {
            public int compare(Bundle c1, Bundle c2) {
                if (Long.parseLong(c1.getTransactions().get(0).getTimestamp()) < Long.parseLong(c2.getTransactions().get(0).getTimestamp()))
                    return -1;
                if (Long.parseLong(c1.getTransactions().get(0).getTimestamp()) > Long.parseLong(c2.getTransactions().get(0).getTimestamp()))
                    return 1;
                return 0;
            }
        });
        Bundle[] returnValue = new Bundle[finalBundles.size()];
        for (int i = 0; i < finalBundles.size(); i++) {
            returnValue[i] = new Bundle(finalBundles.get(i).getTransactions(), finalBundles.get(i).getTransactions().size());
        }
        return returnValue;
    }

    /**
     * @param trytes
     * @return a StoreTransactionsResponse
     */
    public StoreTransactionsResponse broadcastAndStore(final String... trytes) {

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
     *
     * @param {array} trytes
     * @param {int}   depth
     * @param {int}   minWeightMagnitude
     * @return
     */
    public List<Transaction> sendTrytes(final String[] trytes, final int depth, final int minWeightMagnitude) {
        final GetTransactionsToApproveResponse txs = getTransactionsToApprove(depth);

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

    /**
     * Wrapper function for getTrytes and transactionObjects
     * gets the trytes and transaction object from a list of transaction hashes
     *
     * @param {array} hashes
     * @return
     * @method getTransactionsObjects
     * @returns {function} callback
     * @returns {object} success
     **/
    public List<Transaction> getTransactionsObjects(String[] hashes) {

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
     * Wrapper function for findTransactions, getTrytes and transactionObjects
     * Returns the transactionObject of a transaction hash. The input can be a valid
     * findTransactions input
     *
     * @param {object} input
     * @method getTransactionsObjects
     * @returns {function} callback
     * @returns {object} success
     **/
    public List<Transaction> findTransactionObjects(String[] input) {
        FindTransactionResponse ftr = findTransactions(input, null, null, null);
        if (ftr == null || ftr.getHashes() == null)
            return null;

        // get the transaction objects of the transactions
        return getTransactionsObjects(ftr.getHashes());
    }

    /**
     * Wrapper function for findTransactions, getTrytes and transactionObjects
     * Returns the transactionObject of a transaction hash. The input can be a valid
     * findTransactions input
     *
     * @param {object} input
     * @method getTransactionsObjects
     * @returns {function} callback
     * @returns {object} success
     **/
    public List<Transaction> findTransactionObjectsByBundle(String[] input) {
        FindTransactionResponse ftr = findTransactions(null, null, null, input);
        if (ftr == null || ftr.getHashes() == null)
            return null;

        // get the transaction objects of the transactions
        return getTransactionsObjects(ftr.getHashes());
    }

    /**
     * Prepares transfer by generating bundle, finding and signing inputs
     *
     * @param {string}   seed
     * @param {object}   transfers
     * @param {object}   options
     * @param {function} callback
     * @return
     * @method prepareTransfers
     * @property {array} inputs Inputs used for signing. Needs to have correct keyIndex and address value
     * @property {string} address Remainder address
     * @returns {array} trytes Returns bundle trytes
     **/
    public List<String> prepareTransfers(String seed, final List<Transfer> transfers, String remainder, List<Input> inputs) {

        // Input validation of transfers object
        if (!InputValidator.isTransfersCollectionCorrect(transfers)) {
            throw new IllegalStateException("Invalid Transfer");
        }

        // validate & if needed pad seed
        if ( (seed = InputValidator.validateSeed(seed)) == null) {
            throw new IllegalStateException("Invalid Seed");
        }


        // Create a new bundle
        final Bundle bundle = new Bundle();
        final List<String> signatureFragments = new ArrayList<>();

        int totalValue = 0;
        String tag = "";

        //  Iterate over all transfers, get totalValue
        //  and prepare the signatureFragments, message and tag
        for (final Transfer transfer : transfers) {

            int signatureMessageLength = 1;

            // If message longer than 2187 trytes, increase signatureMessageLength (add 2nd transaction)
            if (transfer.getMessage().length() > 2187) {

                // Get total length, message / maxLength (2187 trytes)
                signatureMessageLength += Math.floor(transfer.getMessage().length() / 2187);

                String msgCopy = transfer.getMessage();

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
            if (inputs != null && !inputs.isEmpty()) {

                // Get list if addresses of the provided inputs
                List<String> inputsAddresses = new ArrayList<>();
                for (final Input i : inputs) {
                    inputsAddresses.add(i.getAddress());
                }

                GetBalancesResponse balancesResponse = getBalances(100, inputsAddresses);
                String[] balances = balancesResponse.getBalances();

                List<Input> confirmedInputs = new ArrayList<>();
                int totalBalance = 0;
                int i = 0;
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

                return addRemainder(seed, confirmedInputs, bundle, tag, totalValue, null, signatureFragments);
            }

            //  Case 2: Get inputs deterministically
            //
            //  If no inputs provided, derive the addresses from the seed and
            //  confirm that the inputs exceed the threshold
            else {

                GetBalancesAndFormatResponse newinputs = getInputs(seed, Collections.EMPTY_LIST, 0, 0, totalValue);
                // If inputs with enough balance
                return addRemainder(seed, newinputs.getInput(), bundle, tag, totalValue, null, signatureFragments);
            }
        } else {

            // If no input required, don't sign and simply finalize the bundle
            bundle.finalize();
            bundle.addTrytes(signatureFragments);

            List<Transaction> trxb = bundle.getTransactions();
            List<String> bundleTrytes = new ArrayList<>();

            for (Transaction tx : trxb) {
                bundleTrytes.add(Converter.transactionTrytes(tx));
            }
            Collections.reverse(bundleTrytes);
            return bundleTrytes;
        }
    }

    /**
     * Gets the inputs of a seed
     *
     * @param {string}   seed
     * @param {object}   options
     * @param {function} callback
     * @method getInputs
     * @property {int} start Starting key index
     * @property {int} end Ending key index
     * @property {int} threshold Min balance required
     **/
    public GetBalancesAndFormatResponse getInputs(String seed, final List<String> balances, int start, int end, int threshold) {

        // validate the seed
        if (!InputValidator.isTrytes(seed, 0)) {
            throw new IllegalStateException("Invalid Seed");
        }

        // validate & if needed pad seed
        if ( (seed = InputValidator.validateSeed(seed)) == null) {
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
    public GetBalancesAndFormatResponse getBalanceAndFormat(final List<String> addresses, List<String> balances, long threshold, int start, int end) {

        if (balances == null || balances.isEmpty()) {
            GetBalancesResponse getBalancesResponse = getBalances(100, addresses);
            balances = Arrays.asList(getBalancesResponse.getBalances());
        }

        // If threshold defined, keep track of whether reached or not
        // else set default to true
        boolean thresholdReached = threshold != 0 ? false : true;
        int i = -1;

        List<Input> inputs = new ArrayList<>();
        long totalBalance = 0;

        for (String address : addresses) {

            long balance = Long.parseLong(balances.get(++i));

            if (balance > 0) {
                final Input newEntry = new Input(address, balance, start + i);

                inputs.add(newEntry);
                // Increase totalBalance of all aggregated inputs
                totalBalance += balance;

                if (!thresholdReached && totalBalance >= threshold) {
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

    /**
     * Gets the associated bundle transactions of a single transaction
     * Does validation of signatures, total sum as well as bundle order
     *
     * @param {string} transaction Hash of a tail transaction
     * @method getBundle
     * @returns {list} bundle Transaction objects
     **/
    public GetBundleResponse getBundle(String transaction) throws ArgumentException, InvalidBundleException, InvalidSignatureException {

        Bundle bundle = traverseBundle(transaction, null, new Bundle());
        if (bundle == null) {
            return null;
        }

        long totalSum = 0;
        int lastIndex = 0;
        String bundleHash = bundle.getTransactions().get(0).getBundle();

        Curl curl = new Curl();
        curl.reset();

        List<Signature> signaturesToValidate = new ArrayList<>();

        for (int i = 0; i < bundle.getTransactions().size(); i++) {
            Transaction trx = bundle.getTransactions().get(i);
            Long bundleValue = Long.parseLong(trx.getValue());
            totalSum += bundleValue;

            if (i != Integer.parseInt(bundle.getTransactions().get(i).getCurrentIndex())) {
                throw new ArgumentException("Invalid Bundle");
            }

            String trxTrytes = Converter.transactionTrytes(trx).substring(2187, 2187 + 162);
            //System.out.println("Bundlesize "+bundle.getTransactions().size()+" "+trxTrytes);
            // Absorb bundle hash + value + timestamp + lastIndex + currentIndex trytes.
            curl.absorb(Converter.trits(trxTrytes));
            // Check if input transaction
            if (bundleValue < 0) {
                String address = trx.getAddress();
                Signature sig = new Signature();
                sig.setAddress(address);
                sig.getSignatureFragments().add(trx.getSignatureFragments());

                // Find the subsequent txs with the remaining signature fragment
                for (int y = i; y < bundle.getTransactions().size() - 1; y++) {
                    Transaction newBundleTx = bundle.getTransactions().get(i + 1);

                    // Check if new tx is part of the signature fragment
                    if (newBundleTx.getAddress().equals(address) && Long.parseLong(newBundleTx.getValue()) == 0) {
                        if (sig.getSignatureFragments().indexOf(newBundleTx.getSignatureFragments()) == -1)
                            sig.getSignatureFragments().add(newBundleTx.getSignatureFragments());
                    }
                }
                signaturesToValidate.add(sig);
            }
        }

        // Check for total sum, if not equal 0 return error
        if (totalSum != 0) throw new InvalidBundleException("Invalid Bundle Sum");
        int[] bundleFromTrxs = new int[243];
        curl.squeeze(bundleFromTrxs);
        String bundleFromTxString = Converter.trytes(bundleFromTrxs);

        // Check if bundle hash is the same as returned by tx object
        if (!bundleFromTxString.equals(bundleHash)) throw new InvalidBundleException("Invalid Bundle Hash");
        // Last tx in the bundle should have currentIndex === lastIndex
        bundle.setLength(bundle.getTransactions().size());
        if (!bundle.getTransactions().get(bundle.getLength() - 1).getCurrentIndex().equals(bundle.getTransactions().get(bundle.getLength() - 1).getLastIndex()))
            throw new InvalidBundleException("Invalid Bundle");

        // Validate the signatures
        for (int i = 0; i < signaturesToValidate.size(); i++) {
            String[] signatureFragments = signaturesToValidate.get(i).getSignatureFragments().toArray(new String[signaturesToValidate.get(i).getSignatureFragments().size()]);
            String address = signaturesToValidate.get(i).getAddress();
            boolean isValidSignature = Signing.validateSignatures(address, signatureFragments, bundleHash);

            if (!isValidSignature) throw new InvalidSignatureException();
        }

        return GetBundleResponse.create(bundle.getTransactions());
    }

    /**
     * Replays a transfer by doing Proof of Work again
     *
     * @param {string}   tail
     * @param {int}      depth
     * @param {int}      minWeightMagnitude
     * @param {function} callback
     * @method replayBundle
     * @returns {object} analyzed Transaction objects
     **/
    public ReplayBundleResponse replayBundle(String transaction, int depth, int minWeightMagnitude) throws InvalidBundleException, ArgumentException, InvalidSignatureException {

        List<String> bundleTrytes = new ArrayList<>();

        GetBundleResponse bundleResponse = getBundle(transaction);
        Bundle bundle = new Bundle(bundleResponse.getTransactions(), bundleResponse.getTransactions().size());
        for (Transaction element : bundle.getTransactions()) {

            bundleTrytes.add(Converter.transactionTrytes(element));
        }

        List<Transaction> trxs = sendTrytes(bundleTrytes.toArray(new String[bundleTrytes.size()]), depth, minWeightMagnitude);

        Boolean[] successful = new Boolean[trxs.size()];

        for (int i = 0; i < trxs.size(); i++) {

            final FindTransactionResponse response = findTransactionsByBundles(trxs.get(i).getBundle());


            successful[i] = response.getHashes().length != 0;
        }

        return ReplayBundleResponse.create(successful);
    }

    /**
     * Wrapper function for getNodeInfo and getInclusionStates
     *
     * @param {array} hashes
     * @method getLatestInclusion
     * @returns {function} callback
     * @returns {array} state
     **/
    public GetInclusionStateResponse getLatestInclusion(String[] hashes) {
        GetNodeInfoResponse getNodeInfoResponse = getNodeInfo();
        if (getNodeInfoResponse == null) return null;

        String[] latestMilestone = {getNodeInfoResponse.getLatestSolidSubtangleMilestone()};

        return getInclusionStates(hashes, latestMilestone);
    }

    public SendTransferResponse sendTransfer(String seed, int depth, int minWeightMagnitude, final List<Transfer> transfers, Input[] inputs, String address) {

        List<String> trytes = prepareTransfers(seed, transfers, address, inputs == null ? null : Arrays.asList(inputs));
        List<Transaction> trxs = sendTrytes(trytes.toArray(new String[trytes.size()]), depth, minWeightMagnitude);

        Boolean[] successful = new Boolean[trxs.size()];

        for (int i = 0; i < trxs.size(); i++) {

            final FindTransactionResponse response = findTransactionsByBundles(trxs.get(i).getBundle());

            successful[i] = response.getHashes().length != 0;
        }

        return SendTransferResponse.create(successful);
    }

    /**
     * Basically traverse the Bundle by going down the trunkTransactions until
     * the bundle hash of the transaction is no longer the same. In case the input
     * transaction hash is not a tail, we return an error.
     *
     * @param {string} trunkTx Hash of a trunk or a tail transaction  of a bundle
     * @param {string} bundleHash
     * @param {array}  bundle List of bundles to be populated
     * @method traverseBundle
     * @returns {array} bundle Transaction objects
     **/
    public Bundle traverseBundle(String trunkTx, String bundleHash, Bundle bundle) throws ArgumentException {
        GetTrytesResponse gtr = getTrytes(trunkTx);

        if (gtr != null) {

            if (gtr.getTrytes().length == 0) {
                throw new ArgumentException("Bundle transactions not visible");
            }

            Transaction trx = Converter.transactionObject(gtr.getTrytes()[0]);
            if (trx == null || trx.getBundle() == null) {
                throw new ArgumentException("Invalid trytes, could not create object");
            }
            // If first transaction to search is not a tail, return error
            if (bundleHash == null && Integer.parseInt(trx.getCurrentIndex()) != 0) {
                throw new ArgumentException("Invalid tail transaction supplied.");
            }
            // If no bundle hash, define it
            if (bundleHash == null) {
                bundleHash = trx.getBundle();
            }
            // If different bundle hash, return with bundle
            if (!bundleHash.equals(trx.getBundle())) {
                return bundle;
            }
            // If only one bundle element, return
            if (Integer.parseInt(trx.getLastIndex()) == 0 && Integer.parseInt(trx.getCurrentIndex()) == 0) {
                return new Bundle(Collections.singletonList(trx), 1);
            }
            // Define new trunkTransaction for search
            trunkTx = trx.getTrunkTransaction();
            // Add transaction object to bundle
            bundle.getTransactions().add(trx);

            // Continue traversing with new trunkTx
            return traverseBundle(trunkTx, bundleHash, bundle);
        } else {
            throw new ArgumentException("Get Trytes Response was null");
        }
    }

    public List<String> addRemainder(final String seed,
                                     final List<Input> inputs,
                                     final Bundle bundle,
                                     final String tag,
                                     final long totalValue,
                                     final String remainderAddress,
                                     final List<String> signatureFragments) {

        for (int i = 0; i < inputs.size(); i++) {
            long thisBalance = inputs.get(i).getBalance();
            long totalTransferValue = totalValue;
            long toSubtract = 0 - thisBalance;
            long timestamp = (new Date()).getTime();

            // Add input as bundle entry
            bundle.addEntry(2, inputs.get(i).getAddress(), toSubtract, tag, timestamp);
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
                    return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments);
                } else if (remainder > 0) {
                    // Generate a new Address by calling getNewAddress

                    GetNewAddressResponse res = getNewAddress(seed, 0, false, 0, false);
                    // Remainder bundle entry
                    bundle.addEntry(1, res.getAddresses().get(0), remainder, tag, timestamp);

                    // Final function for signing inputs
                    return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments);
                } else {
                    // If there is no remainder, do not add transaction to bundle
                    // simply sign and return
                    return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments);
                }

                // If multiple inputs provided, subtract the totalTransferValue by
                // the inputs balance
            } else {
                totalTransferValue -= thisBalance;
            }
        }
        return null;
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
