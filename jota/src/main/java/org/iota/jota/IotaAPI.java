package org.iota.jota;

import org.apache.commons.lang3.StringUtils;
import org.iota.jota.builder.AddressRequest;
import org.iota.jota.builder.ApiBuilder;
import org.iota.jota.dto.response.BroadcastTransactionsResponse;
import org.iota.jota.dto.response.FindTransactionResponse;
import org.iota.jota.dto.response.GetAccountDataResponse;
import org.iota.jota.dto.response.GetAttachToTangleResponse;
import org.iota.jota.dto.response.GetBalancesAndFormatResponse;
import org.iota.jota.dto.response.GetBalancesResponse;
import org.iota.jota.dto.response.GetBundleResponse;
import org.iota.jota.dto.response.GetInclusionStateResponse;
import org.iota.jota.dto.response.GetNewAddressResponse;
import org.iota.jota.dto.response.GetTransactionsToApproveResponse;
import org.iota.jota.dto.response.GetTransferResponse;
import org.iota.jota.dto.response.GetTrytesResponse;
import org.iota.jota.dto.response.ReplayBundleResponse;
import org.iota.jota.dto.response.SendTransferResponse;
import org.iota.jota.dto.response.WereAddressesSpentFromResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.error.BaseException;
import org.iota.jota.error.NotPromotableException;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Input;
import org.iota.jota.model.Transaction;
import org.iota.jota.model.Transfer;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.utils.BundleValidator;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.Constants;
import org.iota.jota.utils.InputValidator;
import org.iota.jota.utils.IotaAPIUtils;
import org.iota.jota.utils.Parallel;
import org.iota.jota.utils.StopWatch;
import org.iota.mddoclet.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * IotaAPI Builder. Usage:
 * <p>
 * {@code IotaAPI api = new IotaAPI.Builder()}
 * {@code .protocol("http")}
 * {@code .nodeAddress("localhost")}
 * {@code .port(12345)}
 * {@code .build();}
 * <p>
 * {@code GetNodeInfoResponse response = api.getNodeInfo();}
 */
public class IotaAPI extends IotaAPICore {

    private static final Logger log = LoggerFactory.getLogger(IotaAPI.class);

    protected IotaAPI(ApiOptions options) {
        super(options);
    }

    protected IotaAPI(Builder builder) {
        super(new ApiOptions(builder));
    }

    /**
     * Generates new addresses, meaning addresses which were not spend from, according to the connected node.
     * Stops when {@link AddressRequest#getAmount()} of unspent addresses are found, starting from {@link AddressRequest#getIndex()}
     * <p>
     * If {@link AddressRequest#getAmount()} is set to 0, we will generate until the first unspent address is found, and stop.
     *
     * @param addressRequest {@link AddressRequest}
     * @return {@link GetNewAddressResponse}
     */
    @Document
    public GetNewAddressResponse generateNewAddresses(AddressRequest addressRequest) {
        StopWatch stopWatch = new StopWatch();
        List<String> addresses = new ArrayList<>();
        String seed = addressRequest.getSeed();
        int securityLevel = addressRequest.getSecurityLevel();
        int index = addressRequest.getIndex();
        int amount = addressRequest.getAmount();
        boolean checksum = addressRequest.isChecksum();
        boolean addSpendAddresses = addressRequest.isAddSpendAddresses();

        if (amount == 0) {
            String unusedAddress = getFirstUnusedAddress(seed, securityLevel, index, checksum);
            addresses.add(unusedAddress);
        } else {
            List<String> mAddresses = getAddresses(seed, securityLevel, index, checksum, amount, addSpendAddresses);
            addresses.addAll(mAddresses);
        }

        return GetNewAddressResponse.create(addresses, stopWatch.getElapsedTimeMili());
    }

    private boolean isAddressSpent(String newAddress, boolean checksum) {
        final String address = checksum ? newAddress : Checksum.addChecksum(newAddress);
        final FindTransactionResponse response = findTransactionsByAddresses(address);

        if (response.getHashes().length == 0) {
            Boolean state = checkWereAddressSpentFrom(address);
            return state;
        }

        return true;
    }

    private String getFirstUnusedAddress(String seed, int securityLevel, int index, boolean checksum) {
        int mIndex = index;
        do {
            final String newAddress = IotaAPIUtils.newAddress(seed, securityLevel, mIndex, checksum, getCurl());
            if (!isAddressSpent(newAddress, checksum)) {
                return newAddress;
            }

            mIndex++;
        } while (true);
    }

    private List<String> getAddresses(String seed, int securityLevel, int index, boolean checksum, int amount,
                                      boolean addSpendAddresses) {
        List<String> addresses = new ArrayList<>();

        for (int i = index, numUnspentFound = 0; numUnspentFound < amount; i++) {

            final String newAddress = IotaAPIUtils.newAddress(seed, securityLevel, i, checksum, getCurl());
            if (!isAddressSpent(newAddress, checksum)) {
                addresses.add(newAddress);
                numUnspentFound++;
            } else if (addSpendAddresses) {
                addresses.add(newAddress);
            }
        }

        return addresses;
    }

    /**
     * Generates <code>amount</code> of addresses, starting from <code>index</code>
     * This does not mean that these addresses are safe to use (unspent)
     *
     * @param addressRequest {@link AddressRequest}
     * @return {@link GetNewAddressResponse}
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    @Document
    public GetNewAddressResponse getAddressesUnchecked(AddressRequest addressRequest) throws ArgumentException {
        StopWatch stopWatch = new StopWatch();

        List<String> allAddresses = IntStream
                .range(addressRequest.getIndex(), addressRequest.getIndex() + addressRequest.getAmount())
                .mapToObj(mIndex -> IotaAPIUtils.newAddress(addressRequest, mIndex, getCurl()))
                .collect(toList());

        return GetNewAddressResponse.create(allAddresses, stopWatch.getElapsedTimeMili());
    }

    /**
     * Finds all the bundles for all the addresses based on this seed and security.
     *
     * @param seed            Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security        Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param start           Starting key index, must be at least 0.
     * @param end             Ending key index, must be bigger then <tt>start</tt>
     * @param inclusionStates If <code>true</code>, it also gets the inclusion states of the transfers.
     * @return {@link GetTransferResponse}
     * @throws ArgumentException     when <tt>start</tt> and <tt>end</tt> are more then 500 apart
     * @throws ArgumentException     Invalid security index
     * @throws IllegalStateException When the seed is invalid
     */
    @Document
    public GetTransferResponse getTransfers(String seed, int security, Integer start, Integer end, Boolean inclusionStates) throws ArgumentException {

        // validate seed
        if (!InputValidator.isValidSeed(seed)) {
            throw new IllegalStateException(Constants.INVALID_SEED_INPUT_ERROR);
        }

        if (start < 0 || start > end || end > (start + 500)) {
            throw new ArgumentException(Constants.INVALID_INPUT_ERROR);
        }

        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        AddressRequest addressRequest = new AddressRequest.Builder(seed, security)
                .index(start)
                .checksum(true)
                .amount(end)
                .addSpendAddresses(true)
                .build();

        GetNewAddressResponse gnr = generateNewAddresses(addressRequest);
        if (gnr != null && gnr.getAddresses() != null) {
            Bundle[] bundles = bundlesFromAddresses(inclusionStates, gnr.getAddresses().toArray(new String[0]));
            return GetTransferResponse.create(bundles, stopWatch.getElapsedTimeMili());
        }
        return GetTransferResponse.create(new Bundle[]{}, stopWatch.getElapsedTimeMili());
    }

    /**
     * Internal function to get the formatted bundles of a list of addresses.
     *
     * @param addresses       If <code>true</code>, it also gets the inclusion state of each bundle.
     * @param inclusionStates Array of addresses.
     * @return All the transaction bundles for the addresses, or <code>null</code> when our thread gets interrupted
     * @throws ArgumentException     When the addresses are invalid
     * @throws IllegalStateException When inclusion state/confirmed could not be determined (<tt>null</tt> returned)
     */
    @Document
    public Bundle[] bundlesFromAddresses(Boolean inclusionStates, String... addresses) throws ArgumentException {

        List<Transaction> trxs = findTransactionObjectsByAddresses(addresses);
        // set of tail transactions
        List<String> tailTransactions = new ArrayList<>();
        List<String> nonTailBundleHashes = new ArrayList<>();

        for (Transaction trx : trxs) {
            // Sort tail and nonTails
            if (trx.getCurrentIndex() == 0) {
                tailTransactions.add(trx.getHash());
            } else {
                if (nonTailBundleHashes.indexOf(trx.getBundle()) == -1) {
                    nonTailBundleHashes.add(trx.getBundle());
                }
            }
        }

        List<Transaction> bundleObjects = findTransactionObjectsByBundle(nonTailBundleHashes.toArray(new String[0]));
        for (Transaction trx : bundleObjects) {
            // Sort tail and nonTails
            if (trx.getCurrentIndex() == 0) {
                if (tailTransactions.indexOf(trx.getHash()) == -1) {
                    tailTransactions.add(trx.getHash());
                }
            }
        }

        final List<Bundle> finalBundles = new ArrayList<>();
        final String[] tailTxArray = tailTransactions.toArray(new String[0]);

        // If inclusionStates, get the confirmation status
        // of the tail transactions, and thus the bundles
        GetInclusionStateResponse gisr = null;
        if (tailTxArray.length != 0 && inclusionStates) {
            gisr = getLatestInclusion(tailTxArray);
            if (gisr == null || gisr.getStates() == null || gisr.getStates().length == 0) {
                throw new IllegalStateException(Constants.GET_INCLUSION_STATE_RESPONSE_ERROR);
            }
        }
        final GetInclusionStateResponse finalInclusionStates = gisr;
        try {
            Parallel.of(Arrays.asList(tailTxArray), tailTx -> {
                try {
                    GetBundleResponse bundleResponse = getBundle(tailTx);
                    Bundle gbr = new Bundle(bundleResponse.getTransactions(), bundleResponse.getTransactions().size());
                    if (gbr.getTransactions() != null) {
                        if (inclusionStates) {
                            boolean thisInclusion = false;
                            if (finalInclusionStates != null) {
                                thisInclusion = finalInclusionStates.getStates()[Arrays.asList(tailTxArray).indexOf(tailTx)];
                            }
                            for (Transaction t : gbr.getTransactions()) {
                                t.setPersistence(thisInclusion);
                            }
                        }
                        finalBundles.add(gbr);
                    }
                    // If error returned from getBundle, simply ignore it because the bundle was most likely incorrect
                } catch (ArgumentException e) {
                    if (!Thread.interrupted()) {
                        log.warn(Constants.GET_BUNDLE_RESPONSE_ERROR);
                    }
                }
            });
        } catch (InterruptedException e) {
            return null;
        }

        Collections.sort(finalBundles);
        Bundle[] returnValue = new Bundle[finalBundles.size()];
        for (int i = 0; i < finalBundles.size(); i++) {
            returnValue[i] = new Bundle(finalBundles.get(i).getTransactions(), finalBundles.get(i).getTransactions().size());
        }
        return returnValue;
    }

    /**
     * Wrapper method: stores and broadcasts the specified trytes.
     *
     * @param trytes The trytes.
     * @return {@link BroadcastTransactionsResponse}
     * @throws ArgumentException is thrown when the specified <tt>trytes</tt> is not valid.
     * @throws ArgumentException If {@link #storeTransactions(String...)} fails
     * @see #storeTransactions(String...)
     * @see #broadcastTransactions(String...)
     */
    @Document
    public BroadcastTransactionsResponse storeAndBroadcast(String... trytes) throws ArgumentException {

        if (!InputValidator.isArrayOfAttachedTrytes(trytes)) {
            throw new ArgumentException(Constants.INVALID_TRYTES_INPUT_ERROR);
        }

        try {
            storeTransactions(trytes);
        } catch (Exception e) {
            throw new ArgumentException(e.getMessage(), e);
        }
        return broadcastTransactions(trytes);
    }

    /**
     * Wrapper method: Gets transactions to approve, attaches to Tangle, broadcasts and stores.
     *
     * @param trytes             The transaction trytes
     * @param depth              The depth for getting transactions to approve
     * @param minWeightMagnitude The minimum weight magnitude for doing proof of work
     * @param reference          Hash of transaction to start random-walk from
     *                           This is used to make sure the tips returned reference a given transaction in their past.
     *                           This can be <tt>null</tt>, in that case the latest milestone is used as a reference.
     * @return Sent {@link Transaction} objects.
     * @throws ArgumentException is thrown when invalid trytes is provided.
     * @see #broadcastTransactions(String...)
     * @see #attachToTangle(String, String, Integer, String...)
     * @see #storeAndBroadcast(String...)
     */
    @Document
    public List<Transaction> sendTrytes(String[] trytes, int depth, int minWeightMagnitude, String reference) throws ArgumentException {
        GetTransactionsToApproveResponse txs = getTransactionsToApprove(depth, reference);
        // attach to tangle - do pow
        GetAttachToTangleResponse res = attachToTangle(txs.getTrunkTransaction(), txs.getBranchTransaction(), minWeightMagnitude, trytes);
        try {
            storeAndBroadcast(res.getTrytes());
        } catch (ArgumentException e) {
            return new ArrayList<>();
        }

        final List<Transaction> trx = new ArrayList<>();

        for (String tryte : res.getTrytes()) {
            trx.add(new Transaction(tryte, SpongeFactory.create(SpongeFactory.Mode.CURL_P81)));
        }

        return trx;
    }

    /**
     * Wrapper function: get trytes and turns into {@link Transaction} objects.
     * Gets the trytes and transaction object from a list of transaction hashes.
     *
     * @param hashes The hashes of the transactions we want to get the transactions from
     * @return {@link Transaction} objects.
     * @throws ArgumentException if hashes is not a valid array of hashes
     * @see #getTrytes(String...)
     **/
    @Document
    public List<Transaction> findTransactionsObjectsByHashes(String... hashes) throws ArgumentException {

        if (!InputValidator.isArrayOfHashes(hashes)) {
            throw new IllegalStateException(Constants.INVALID_HASHES_INPUT_ERROR);
        }

        final GetTrytesResponse trytesResponse = getTrytes(hashes);

        final List<Transaction> trxs = new ArrayList<>();

        for (final String tryte : trytesResponse.getTrytes()) {
            trxs.add(new Transaction(tryte, SpongeFactory.create(SpongeFactory.Mode.CURL_P81)));
        }
        return trxs;
    }

    /**
     * Wrapper function: Finds transactions, gets trytes and turns it into {@link Transaction} objects.
     *
     * @param addresses The addresses we should get the transactions for, must contain checksums
     * @return a list of {@link Transaction} objects.
     * @throws ArgumentException if addresses is not a valid array of hashes
     * @see #findTransactionsByAddresses(String...)
     * @see #findTransactionsObjectsByHashes
     **/
    @Document
    public List<Transaction> findTransactionObjectsByAddresses(String[] addresses) throws ArgumentException {
        FindTransactionResponse ftr = findTransactionsByAddresses(addresses);
        if (ftr == null || ftr.getHashes() == null) {
            return new ArrayList<>();
        }

        // get the transaction objects of the transactions
        return findTransactionsObjectsByHashes(ftr.getHashes());
    }

    /**
     * Wrapper function: Finds transactions, gets trytes and turns it into {@link Transaction} objects.
     *
     * @param tags The tags the transactions we search for have
     * @return a list of {@link Transaction} objects.
     * @throws ArgumentException if any of the tags are not a valid
     * @see #findTransactionsByTags(String...)
     * @see #findTransactionsObjectsByHashes
     **/
    @Document
    public List<Transaction> findTransactionObjectsByTag(String... tags) throws ArgumentException {
        FindTransactionResponse ftr = findTransactionsByTags(tags);
        if (ftr == null || ftr.getHashes() == null) {
            return new ArrayList<>();
        }

        // get the transaction objects of the transactions
        return findTransactionsObjectsByHashes(ftr.getHashes());
    }

    /**
     * Wrapper function: Finds transactions, gets trytes and turns it into {@link Transaction} objects.
     *
     * @param approvees The transaction hashes of which we want to approvers
     * @return {@link Transaction} objects.
     * @throws ArgumentException if addresses is not a valid array of hashes
     * @see #findTransactionsByApprovees
     **/
    @Document
    public List<Transaction> findTransactionObjectsByApprovees(String... approvees) throws ArgumentException {
        FindTransactionResponse ftr = findTransactionsByApprovees(approvees);
        if (ftr == null || ftr.getHashes() == null) {
            return new ArrayList<>();
        }

        // get the transaction objects of the transactions
        return findTransactionsObjectsByHashes(ftr.getHashes());
    }

    /**
     * Wrapper function: Finds transactions, gets trytes and turns it into {@link Transaction} objects.
     *
     * @param bundles The bundles for which we will get all its transactions
     * @return {@link Transaction} objects.
     * @throws ArgumentException if addresses is not a valid array of hashes
     * @see #findTransactionsByBundles
     **/
    @Document
    public List<Transaction> findTransactionObjectsByBundle(String... bundles) throws ArgumentException {
        FindTransactionResponse ftr = findTransactionsByBundles(bundles);
        if (ftr == null || ftr.getHashes() == null) {
            return new ArrayList<>();
        }

        // get the transaction objects of the transactions
        return findTransactionsObjectsByHashes(ftr.getHashes());
    }

    /**
     * Prepares transfer by generating bundle, finding and signing inputs.
     *
     * @param seed           The tryte-encoded seed. It should be noted that this seed is not transferred.
     *                       Only required when we are preparing a value transfer
     * @param security       Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param transfers      List of transfer objects.
     *                       If the total value of the transfers is 0, no signing is performed.
     * @param remainder      If defined, this address will be used for sending the remainder value (of the inputs) to.
     *                       Otherwise, then next available address is used (if the transfer value is over 0)
     * @param inputs         The inputs used for this transfer
     * @param tips           The starting points we walk back from to find the balance of the addresses, can be <tt>null</tt>
     * @param validateInputs Whether or not to validate the balances of the provided inputs
     *                       If no validation is required
     * @return Returns a list of the trytes of each bundle.
     * @throws ArgumentException     If the seed is invalid
     * @throws ArgumentException     If the security level is wrong.
     * @throws IllegalStateException If the transfers are not all valid
     * @throws IllegalStateException If there is not enough balance in the inputs
     */
    @Document
    public List<String> prepareTransfers(String seed, int security,
                                         List<Transfer> transfers,
                                         String remainder,
                                         List<Input> inputs,
                                         List<Transaction> tips,
                                         boolean validateInputs) throws ArgumentException {
        //validate seed
        if ((!InputValidator.isValidSeed(seed))) {
            throw new IllegalStateException(Constants.INVALID_SEED_INPUT_ERROR);
        }

        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        if (remainder != null && !InputValidator.checkAddress(remainder)) {
            throw new ArgumentException(Constants.INVALID_ADDRESSES_INPUT_ERROR);
        }

        // Input validation of transfers object
        if (!InputValidator.isTransfersCollectionValid(transfers)) {
            throw new ArgumentException(Constants.INVALID_TRANSFERS_INPUT_ERROR);
        }

        if (inputs != null && !InputValidator.areValidInputsList(inputs)) {
            throw new ArgumentException(Constants.INVALID_ADDRESSES_INPUT_ERROR);
        }

        // Create a new bundle
        final Bundle bundle = new Bundle();
        final List<String> signatureFragments = new ArrayList<>();

        long totalValue = 0;
        String tag = "";
        //  Iterate over all transfers, get totalValue
        //  and prepare the signatureFragments, message and tag
        for (final Transfer transfer : transfers) {

            int signatureMessageLength = 1;

            // If message longer than 2187 trytes, increase signatureMessageLength (add 2nd transaction)
            if (transfer.getMessage().length() > Constants.MESSAGE_LENGTH) {

                // Get total length, message / maxLength (2187 trytes)
                signatureMessageLength += Math.floor(transfer.getMessage().length() / Constants.MESSAGE_LENGTH);

                String msgCopy = transfer.getMessage();

                // While there is still a message, copy it
                while (!msgCopy.isEmpty()) {

                    String fragment = StringUtils.substring(msgCopy, 0, Constants.MESSAGE_LENGTH);
                    msgCopy = StringUtils.substring(msgCopy, Constants.MESSAGE_LENGTH, msgCopy.length());

                    // Pad remainder of fragment

                    fragment = StringUtils.rightPad(fragment, Constants.MESSAGE_LENGTH, '9');

                    signatureFragments.add(fragment);
                }
            } else {
                // Else, get single fragment with 2187 of 9's trytes
                String fragment = transfer.getMessage();

                if (transfer.getMessage().length() < Constants.MESSAGE_LENGTH) {
                    fragment = StringUtils.rightPad(fragment, Constants.MESSAGE_LENGTH, '9');
                }
                signatureFragments.add(fragment);
            }

            tag = transfer.getTag();

            // pad for required 27 tryte length
            if (transfer.getTag().length() < Constants.TAG_LENGTH) {
                tag = StringUtils.rightPad(tag, Constants.TAG_LENGTH, '9');
            }


            // get current timestamp in seconds
            long timestamp = (long) Math.floor(Calendar.getInstance().getTimeInMillis() / 1000);

            // Add first entry to the bundle
            bundle.addEntry(signatureMessageLength,
                    Checksum.removeChecksum(transfer.getAddress()),
                    transfer.getValue(),
                    tag, timestamp);
            // Sum up total value
            totalValue += transfer.getValue();
        }

        // Get inputs if we are sending tokens
        if (totalValue != 0) {
            // validate seed
            if ((!InputValidator.isValidSeed(seed))) {
                throw new IllegalStateException(Constants.INVALID_SEED_INPUT_ERROR);
            }

            for (Transfer t : transfers) {
                if (!InputValidator.hasTrailingZeroTrit(t.getAddress())) {
                    throw new ArgumentException(Constants.INVALID_ADDRESSES_INPUT_ERROR);
                }
            }

            //  Case 1: user provided inputs
            //  Validate the inputs by calling getBalances
            if (inputs != null && !inputs.isEmpty()) {

                if (!validateInputs) {
                    return addRemainder(seed, security, inputs, bundle, tag, totalValue, remainder, signatureFragments);
                }

                // Get list if addresses of the provided inputs
                List<String> inputsAddresses = new ArrayList<>();
                for (final Input i : inputs) {
                    inputsAddresses.add(i.getAddress());
                }

                List<String> tipHashes = null;
                if (tips != null) {
                    tipHashes = new ArrayList<>();

                    for (final Transaction tx : tips) {
                        tipHashes.add(tx.getHash());
                    }
                }

                GetBalancesResponse balancesResponse = getBalances(inputsAddresses, tipHashes);
                String[] balances = balancesResponse.getBalances();

                List<Input> confirmedInputs = new ArrayList<>();
                long totalBalance = 0;

                for (int i = 0; i < balances.length; i++) {
                    long thisBalance = Long.parseLong(balances[i]);

                    // If input has balance, add it to confirmedInputs
                    if (thisBalance > 0) {
                        totalBalance += thisBalance;
                        Input inputEl = inputs.get(i);
                        inputEl.setBalance(thisBalance);
                        confirmedInputs.add(inputEl);

                        // if we've already reached the intended input value, break out of loop
                        if (totalBalance >= totalValue) {
                            break;
                        }
                    }

                }

                // Return not enough balance error
                if (totalValue > totalBalance) {
                    throw new IllegalStateException(Constants.NOT_ENOUGH_BALANCE_ERROR);
                }

                return addRemainder(seed, security, confirmedInputs, bundle, tag, totalValue, remainder, signatureFragments);
            }

            //  Case 2: Get inputs deterministically
            //
            //  If no inputs provided, derive the addresses from the seed and
            //  confirm that the inputs exceed the threshold
            else {
                GetBalancesAndFormatResponse newinputs = getInputs(seed, security, 0, 0, totalValue);
                if (newinputs == null || newinputs.getTotalBalance() < totalValue) {
                    throw new IllegalStateException(Constants.NOT_ENOUGH_BALANCE_ERROR);
                }
                // If inputs with enough balance
                return addRemainder(seed, security, newinputs.getInputs(), bundle, tag, totalValue, remainder, signatureFragments);
            }
        } else {

            // If no input required, don't sign and simply finalize the bundle
            bundle.finalize(SpongeFactory.create(SpongeFactory.Mode.KERL));
            bundle.addTrytes(signatureFragments);

            List<Transaction> trxb = bundle.getTransactions();
            List<String> bundleTrytes = new ArrayList<>();

            for (Transaction trx : trxb) {
                bundleTrytes.add(trx.toTrytes());
            }

            Collections.reverse(bundleTrytes);
            return bundleTrytes;
        }
    }

    /**
     * Gets the inputs of a seed
     * If start, end and threshold are 0, checks everything until an address with nothing is found.
     * Addresses are all with checksum appended
     *
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param start     Starting key index, must be at least 0.
     * @param end       Ending key index, must be bigger then <tt>start</tt>, and cant span more than 500 indexes
     * @param threshold Minimum balance required.
     * @param tips      The starting points we walk back from to find the balance of the addresses, can be <tt>null</tt>
     * @return {@link GetBalancesAndFormatResponse}
     * @throws ArgumentException If the seed is invalid
     * @throws ArgumentException If the security level is wrong.
     * @throws ArgumentException when <tt>start</tt> and <tt>end</tt> are more then 500 apart
     * @see #getBalanceAndFormat(List, List, long, int, StopWatch, int)
     **/
    @Document
    public GetBalancesAndFormatResponse getInputs(String seed, int security, int start, int end, long threshold, final String... tips) throws ArgumentException {

        // validate the seed
        if ((!InputValidator.isValidSeed(seed))) {
            throw new IllegalStateException(Constants.INVALID_SEED_INPUT_ERROR);
        }

        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        // If start value bigger than end, return error
        // or if difference between end and start is bigger than 500 keys
        if (start < 0 || start > end || end > (start + 500)) {
            throw new IllegalStateException(Constants.INVALID_INPUT_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        List<String> tipsList = tips != null ? Arrays.asList(tips) : null;

        //  Case 1: start and end
        //
        //  If start and end is defined by the user, simply iterate through the keys
        //  and call getBalances
        if (end != 0) {

            List<String> allAddresses = new ArrayList<>();

            for (int i = start; i < end; i++) {
                String address = IotaAPIUtils.newAddress(seed, security, i, true, getCurl());
                allAddresses.add(address);
            }

            return getBalanceAndFormat(allAddresses, tipsList, threshold, start, stopWatch, security);
        }
        //  Case 2: iterate till threshold
        //
        //  Either start from index: 0 or start (if defined) until threshold is reached.
        else {
            List<Input> allInputs = new ArrayList<>();

            boolean thresholdReached = true;
            long currentTotal = 0;

            for (int i = start; thresholdReached; i++) {
                String address = IotaAPIUtils.newAddress(seed, security, i, true, getCurl());

                // Received input, this epoch or previous
                GetBalancesResponse response = getBalances(Collections.singletonList(address), tipsList);
                long balance;
                try {
                    balance = Long.parseLong(response.getBalances()[0]);
                } catch (NumberFormatException e) {
                    throw new ArgumentException(e.getMessage());
                }

                if (balance > 0) {
                    // Is it already spent from?
                    WereAddressesSpentFromResponse wasSpent = this.wereAddressesSpentFrom(address);
                    if (wasSpent.getStates().length > 0 && !wasSpent.getStates()[0]) {
                        // We can use this!
                        allInputs.add(new Input(address, balance, i, security));
                        currentTotal += balance;

                        if (threshold != 0 && threshold <= currentTotal) {
                            // Stop because we found threshold
                            thresholdReached = false;
                        }
                    }
                } else {
                    // Check if there was any activity at all
                    FindTransactionResponse tx = findTransactionsByAddresses(address);
                    if (tx.getHashes().length == 0 || i - start > 500) {
                        // Stop because we reached our limit or no activity
                        thresholdReached = false;
                    }
                }
            }

            return GetBalancesAndFormatResponse.create(allInputs, currentTotal, stopWatch.getElapsedTimeMili());
        }
    }

    /**
     * <p>
     * The returned balance is based on the latest confirmed milestone.
     * In addition to the balances, it also returns the referencing <tt>tips</tt> (or milestone),
     * as well as the index with which the confirmed balance was determined.
     * The balances are returned as a list in the same order as the addresses were provided as input.
     * </p>
     *
     * @param address The addresses where we will find the balance for.
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException The the request was considered wrong in any way by the node
     */
    public long getBalance(String address) throws ArgumentException {
        GetBalancesResponse response = getBalances(Collections.singletonList(address));
        try {
            return Long.parseLong(response.getBalances()[0]);
        } catch (NumberFormatException e) {
            throw new ArgumentException(e.getMessage());
        }
    }

    /**
     * <p>
     * The returned balance is based on the latest confirmed milestone.
     * In addition to the balances, it also returns the referencing <tt>tips</tt> (or milestone),
     * as well as the index with which the confirmed balance was determined.
     * The balances are returned as a list in the same order as the addresses were provided as input.
     * </p>
     *
     * @param threshold The confirmation threshold between 0 and 100(inclusive).
     *                  Should be set to 100 for getting balance by counting only confirmed transactions.
     * @param address   The addresses where we will find the balance for.
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException The the request was considered wrong in any way by the node
     * @deprecated The threshold parameter is removed from the getBalances endpoint on IRI nodes.
     * Alternative use {@link IotaAPI#getBalance(String)}
     */
    @Deprecated
    public long getBalance(int threshold, String address) throws ArgumentException {
        GetBalancesResponse response = getBalances(threshold, new String[]{address}, null);
        try {
            return Long.parseLong(response.getBalances()[0]);
        } catch (NumberFormatException e) {
            throw new ArgumentException(e.getMessage());
        }
    }

    /**
     * Gets the balances and formats the output.
     *
     * @param addresses The addresses.
     * @param tips      The starting points we walk back from to find the balance of the addresses, can be <tt>null</tt>
     * @param threshold Min balance required.
     * @param start     Starting key index.
     * @param stopWatch the stopwatch. If you pass <tt>null</tt>, a new one is created.
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @return {@link GetBalancesAndFormatResponse}
     * @throws ArgumentException     is thrown when the specified security level is not valid.
     * @throws IllegalStateException when there is not enough balance on the addresses
     **/
    @Document
    public GetBalancesAndFormatResponse getBalanceAndFormat(List<String> addresses,
                                                            List<String> tips,
                                                            long threshold,
                                                            int start,
                                                            StopWatch stopWatch,
                                                            int security) throws ArgumentException,
            IllegalStateException {

        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        StopWatch suppliedStopWatch = stopWatch;
        if (suppliedStopWatch == null) {
            suppliedStopWatch = new StopWatch();
        }

        GetBalancesResponse getBalancesResponse = getBalances(addresses, tips);
        List<String> balances = Arrays.asList(getBalancesResponse.getBalances());

        // If threshold defined, keep track of whether reached or not
        // else set default to true

        boolean thresholdReached = threshold == 0;

        List<Input> inputs = new ArrayList<>();
        long totalBalance = 0;

        for (int i = 0; i < addresses.size(); i++) {

            long balance = Long.parseLong(balances.get(i));

            if (balance > 0) {
                final Input newEntry = new Input(addresses.get(i), balance, start + i, security);

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
            return GetBalancesAndFormatResponse.create(inputs, totalBalance, suppliedStopWatch.getElapsedTimeMili());
        }
        throw new IllegalStateException(Constants.NOT_ENOUGH_BALANCE_ERROR);
    }

    /**
     * Gets the associated bundle transactions of a single transaction.
     * Does validation of signatures, total sum as well as bundle order.
     *
     * @param transaction The transaction hash
     * @return {@link GetBundleResponse}
     * @throws ArgumentException if the transaction hash is invalid
     * @throws ArgumentException if the bundle is invalid or not found
     */
    @Document
    public GetBundleResponse getBundle(String transaction) throws ArgumentException {

        if (!InputValidator.isHash(transaction)) {
            throw new ArgumentException(Constants.INVALID_HASHES_INPUT_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        Bundle bundle = traverseBundle(transaction, null, new Bundle());
        if (bundle == null) {
            throw new ArgumentException(Constants.INVALID_BUNDLE_ERROR);
        }

        if (!BundleValidator.isBundle(bundle)) {
            throw new ArgumentException(Constants.INVALID_BUNDLE_ERROR);
        }

        return GetBundleResponse.create(bundle.getTransactions(), stopWatch.getElapsedTimeMili());
    }

    /**
     * Similar to getTransfers, just that it returns additional account data
     *
     * @param seed            Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security        Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param index           Key index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @param checksum        Adds 9-tryte address checksum. Checksum is required for all API calls.
     * @param total           Total number of addresses to generate.
     * @param returnAll       If <code>true</code>, it returns all addresses which were deterministically generated (until findTransactions returns null).
     * @param start           Starting key index, must be at least 0.
     * @param end             Ending key index, must be bigger then <tt>start</tt>
     * @param inclusionStates If <code>true</code>, it gets the inclusion states of the transfers.
     * @param threshold       Minimum balance required.
     * @return {@link GetAccountDataResponse}
     * @throws ArgumentException when the specified security level is not valid.
     * @throws ArgumentException when <tt>start</tt> and <tt>end</tt> are invalid
     * @see #getTransfers(String, int, Integer, Integer, Boolean)
     */
    @Document
    public GetAccountDataResponse getAccountData(String seed, int security, int index, boolean checksum, int total,
                                                 boolean returnAll, int start, int end, boolean inclusionStates, long threshold) throws ArgumentException {

        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        if (start < 0 || start > end || end > (start + 1000)) {
            throw new ArgumentException(Constants.INVALID_INPUT_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        AddressRequest addressRequest = new AddressRequest.Builder(seed, security)
                .index(index)
                .checksum(checksum)
                .amount(total)
                .addSpendAddresses(returnAll)
                .build();

        GetNewAddressResponse gna = generateNewAddresses(addressRequest);
        GetTransferResponse gtr = getTransfers(seed, security, start, end, inclusionStates);
        GetBalancesAndFormatResponse gbr = getInputs(seed, security, start, end, threshold);

        return GetAccountDataResponse.create(gna.getAddresses(), gtr.getTransfers(), gbr.getInputs(), gbr.getTotalBalance(), stopWatch.getElapsedTimeMili());
    }

    /**
     * Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.
     * Addresses must have a checksum.
     *
     * @param addresses the addresses to check
     * @return list of address boolean checks
     * @throws ArgumentException when an address is invalid
     */
    @Document
    public boolean[] checkWereAddressSpentFrom(String... addresses) throws ArgumentException {
        WereAddressesSpentFromResponse response = wereAddressesSpentFrom(addresses);
        return response.getStates();

    }

    /**
     * Check if an addresses was ever spent from, in the current epoch, or in previous epochs.
     * Address must have a checksum.
     *
     * @param address the address to check
     * @return <tt>true</tt> if it was spent, otherwise <tt>false</tt>
     * @throws ArgumentException when the address is invalid
     */
    public Boolean checkWereAddressSpentFrom(String address) throws ArgumentException {
        String[] spentAddresses = new String[]{address};
        boolean[] response = checkWereAddressSpentFrom(spentAddresses);
        return response[0];
    }

    /**
     * Replays a transfer by doing Proof of Work again.
     * This will make a new, but identical transaction which now also can be approved.
     * If any of the replayed transactions gets approved, the others stop getting approved.
     *
     * @param tailTransactionHash The hash of tail transaction.
     * @param depth               The depth for getting transactions to approve
     * @param minWeightMagnitude  The minimum weight magnitude for doing proof of work
     * @param reference           Hash of transaction to start random-walk from.
     *                            This is used to make sure the tips returned reference a given transaction in their past.
     *                            Can be <tt>null</tt>, in that case the latest milestone is used as a reference.
     * @return {@link ReplayBundleResponse}
     * @throws ArgumentException when the <tt>tailTransactionHash</tt> is invalid
     * @throws ArgumentException when the bundle is invalid or not found
     * @see #sendTrytes(String[], int, int, String)
     */
    @Document
    public ReplayBundleResponse replayBundle(String tailTransactionHash, int depth, int minWeightMagnitude,
                                             String reference) throws ArgumentException {

        if (!InputValidator.isHash(tailTransactionHash)) {
            throw new ArgumentException(Constants.INVALID_TAIL_HASH_INPUT_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        GetBundleResponse bundleResponse = getBundle(tailTransactionHash);
        Bundle bundle = new Bundle(bundleResponse.getTransactions(), bundleResponse.getTransactions().size());
        return this.replayBundle(bundle, depth, minWeightMagnitude, reference, stopWatch);
    }

    /**
     * Replays a transfer by doing Proof of Work again.
     * This will make a new, but identical transaction which now also can be approved.
     * If any of the replayed transactions gets approved, the others stop getting approved.
     *
     * @param bundle             The bundle we wish to replay on the network
     * @param depth              The depth for getting transactions to approve
     * @param minWeightMagnitude The minimum weight magnitude for doing proof of work
     * @param reference          Hash of transaction to start random-walk from.
     *                           This is used to make sure the tips returned reference a given transaction in their past.
     *                           Can be <tt>null</tt>, in that case the latest milestone is used as a reference.
     * @return {@link ReplayBundleResponse}
     * @throws ArgumentException when the <tt>tailTransactionHash</tt> is invalid
     * @throws ArgumentException when the bundle is invalid or not found
     * @see #sendTrytes(String[], int, int, String)
     */
    public ReplayBundleResponse replayBundle(Bundle bundle, int depth, int minWeightMagnitude,
                                             String reference) throws ArgumentException {

        return this.replayBundle(bundle, depth, minWeightMagnitude, reference, new StopWatch());
    }

    /**
     * Replays a transfer by doing Proof of Work again.
     * This will make a new, but identical transaction which now also can be approved.
     * If any of the replayed transactions gets approved, the others stop getting approved.
     *
     * @param bundle             The bundle we wish to replay on the network
     * @param depth              The depth for getting transactions to approve
     * @param minWeightMagnitude The minimum weight magnitude for doing proof of work
     * @param reference          Hash of transaction to start random-walk from.
     *                           This is used to make sure the tips returned reference a given transaction in their past.
     *                           Can be <tt>null</tt>, in that case the latest milestone is used as a reference.
     * @param stopWatch          The stopwatch used for recording this response time.
     * @return {@link ReplayBundleResponse}
     * @throws ArgumentException when the <tt>tailTransactionHash</tt> is invalid
     * @throws ArgumentException when the bundle is invalid or not found
     * @see #sendTrytes(String[], int, int, String)
     */
    private ReplayBundleResponse replayBundle(Bundle bundle, int depth, int minWeightMagnitude, String reference,
                                              StopWatch stopWatch) throws ArgumentException {

        List<String> bundleTrytes = new ArrayList<>();
        for (Transaction trx : bundle.getTransactions()) {
            bundleTrytes.add(trx.toTrytes());
        }

        Collections.reverse(bundleTrytes);
        List<Transaction> trxs = sendTrytes(bundleTrytes.toArray(new String[0]), depth, minWeightMagnitude, reference);

        Boolean[] successful = new Boolean[trxs.size()];

        for (int i = 0; i < trxs.size(); i++) {

            final FindTransactionResponse response = findTransactionsByBundles(trxs.get(i).getBundle());


            successful[i] = response.getHashes().length != 0;
        }

        return ReplayBundleResponse.create(new Bundle(trxs), successful, stopWatch.getElapsedTimeMili());
    }

    /**
     * Deprecated: {@link #getInclusionStates(String...)} is now always with the latest milestone
     * <p>
     * Runs getInclusionStates
     *
     * @param hashes The hashes.
     * @return {@link GetInclusionStateResponse}
     * @throws ArgumentException when one of the hashes is invalid
     * @see #getInclusionStates(String...)
     */
    @Deprecated
    @Document
    public GetInclusionStateResponse getLatestInclusion(String... hashes) throws ArgumentException {
        return getInclusionStates(hashes);
    }

    /**
     * <p>
     * Get the inclusion states of a set of transactions.
     * This is for determining if a transaction was accepted and confirmed by the network or not.
     * </p>
     * <p>
     * This API call returns a list of boolean values in the same order as the submitted transactions.
     * Boolean values will be <tt>true</tt> for confirmed transactions, otherwise <tt>false</tt>.
     * </p>
     * <p>
     * This is command does the same as {@link #getInclusionStates(String...)} but a copy exists
     * for readability.
     *
     * @param hashes The transaction hashes to check for
     * @return {@link GetInclusionStateResponse}
     * @throws ArgumentException when one of the hashes is invalid
     * @see #getInclusionStates(String...)
     */
    @Document
    public GetInclusionStateResponse isConfirmed(String... hashes) throws ArgumentException {
        return getInclusionStates(hashes);
    }

    /**
     * Wrapper function: Runs prepareTransfers, as well as attachToTangle.
     * We then broadcasts this and and store the transactions on the node.
     *
     * @param seed                   The tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security               Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param depth                  The depth for getting transactions to approve
     * @param minWeightMagnitude     The minimum weight magnitude for doing proof of work
     * @param transfers              List of {@link Transfer} objects.
     * @param inputs                 List of {@link Input} used for funding the transfer.
     * @param remainderAddress       If defined, this remainderAddress will be used for sending the remainder value (of the inputs) to.
     *                               When this is not defined, but a remaining exists, the next free address is used.
     * @param validateInputs         Whether or not to validate the balances of the provided inputs.
     * @param validateInputAddresses Whether or not to validate if the destination address is already use.
     *                               If a key reuse is detect or it's send to inputs.
     * @param tips                   The starting points we walk back from to find the balance of the addresses
     *                               If multiple tips are supplied, only the first tip is used for {@link #getTransactionsToApprove(Integer, String)}
     * @return {@link SendTransferResponse}
     * @throws ArgumentException     If the seed is invalid
     * @throws ArgumentException     If the security level is wrong.
     * @throws ArgumentException     When <tt>validateInputAddresses</tt> is <tt>true</tt>, if validateTransfersAddresses has an error.
     * @throws IllegalStateException If the transfers are not all valid
     * @throws IllegalStateException If there is not enough balance in the inputs to supply to the transfers
     * @see #prepareTransfers(String, int, List, String, List, List, boolean)
     * @see #sendTrytes(String[], int, int, String)
     * @see #validateTransfersAddresses(String, int, List)
     */
    @Document
    public SendTransferResponse sendTransfer(String seed, int security, int depth, int minWeightMagnitude,
                                             List<Transfer> transfers, List<Input> inputs, String remainderAddress,
                                             boolean validateInputs, boolean validateInputAddresses,
                                             List<Transaction> tips) throws ArgumentException {

        StopWatch stopWatch = new StopWatch();

        List<String> trytes = prepareTransfers(seed, security, transfers, remainderAddress, inputs, tips, validateInputs);

        if (validateInputAddresses) {
            validateTransfersAddresses(seed, security, trytes);
        }

        String reference = tips != null && tips.size() > 0 ? tips.get(0).getHash() : null;

        List<Transaction> trxs = sendTrytes(trytes.toArray(new String[0]), depth, minWeightMagnitude, reference);

        Boolean[] successful = new Boolean[trxs.size()];

        for (int i = 0; i < trxs.size(); i++) {
            final FindTransactionResponse response = findTransactionsByBundles(trxs.get(i).getBundle());
            successful[i] = response.getHashes().length != 0;
        }

        return SendTransferResponse.create(trxs, successful, stopWatch.getElapsedTimeMili());
    }

    /**
     * Traverses the Bundle by going down the trunkTransactions until the bundle hash of the transaction changes.
     * In case the input transaction hash is not a tail, we return an error.
     *
     * @param trunkTx    Hash of a trunk or a tail transaction of a bundle.
     * @param bundleHash The bundle hash. Should be <tt>null</tt>, and will use the transactions bundle hash
     * @param bundle     {@link Bundle} to be populated by traversing.
     * @return Transaction objects.
     * @throws ArgumentException when <tt>trunkTx</tt> is invalid, or has no transactions
     * @throws ArgumentException when a transaction in the bundle has no reference to the bundle
     * @throws ArgumentException when the first transaction in the bundle is not a tail
     */
    @Document
    public Bundle traverseBundle(String trunkTx, String bundleHash, Bundle bundle) throws ArgumentException {
        GetTrytesResponse gtr = getTrytes(trunkTx);

        if (gtr != null) {

            if (gtr.getTrytes().length == 0) {
                throw new ArgumentException(Constants.INVALID_BUNDLE_ERROR);
            }

            Transaction trx = new Transaction(gtr.getTrytes()[0], SpongeFactory.create(SpongeFactory.Mode.CURL_P81));
            if (trx.getBundle() == null) {
                throw new ArgumentException(Constants.INVALID_TRYTES_INPUT_ERROR);
            }
            // If first transaction to search is not a tail, return error
            if (bundleHash == null && trx.getCurrentIndex() != 0) {
                throw new ArgumentException(Constants.INVALID_TAIL_HASH_INPUT_ERROR);
            }
            // If no bundle hash, define it
            if (bundleHash == null) {
                bundleHash = trx.getBundle();
            }
            // If different bundle hash, return with bundle
            if (!bundleHash.equals(trx.getBundle())) {
                bundle.setLength(bundle.getTransactions().size());
                return bundle;
            }
            // If only one bundle element, return
            if (trx.getLastIndex() == 0 && trx.getCurrentIndex() == 0) {
                return new Bundle(Collections.singletonList(trx), 1);
            }
            // Define new trunkTransaction for search
            trunkTx = trx.getTrunkTransaction();
            // Add transaction object to bundle
            bundle.getTransactions().add(trx);

            // Continue traversing with new trunkTx
            return traverseBundle(trunkTx, bundleHash, bundle);
        } else {
            throw new ArgumentException(Constants.GET_TRYTES_RESPONSE_ERROR);
        }
    }

    /**
     * Prepares transfer by generating the bundle with the corresponding cosigner transactions.
     * Does not contain signatures.
     *
     * @param securitySum      The sum of security levels used by all co-signers.
     * @param inputAddress     Array of input addresses as well as the securitySum.
     * @param remainderAddress Has to be generated by the cosigners before initiating the transfer, can be null if fully spent.
     * @param transfers        List of {@link Transfer} we want to make using the unputAddresses
     * @return All the {@link Transaction} objects in this newly created transfer
     * @throws ArgumentException     when an address is invalid.
     * @throws ArgumentException     when the security level is wrong.
     * @throws IllegalStateException when a transfer fails because their is not enough balance to perform the transfer.
     * @throws IllegalStateException When a <tt>remainderAddress</tt> is required, but not supplied
     * @throws RuntimeException      When the total value from the transfers is not 0
     */
    public List<Transaction> initiateTransfer(int securitySum, final String inputAddress, String remainderAddress,
                                              final List<Transfer> transfers) throws ArgumentException {
        return initiateTransfer(securitySum, inputAddress, remainderAddress, transfers, null, false);
    }

    /**
     * Prepares transfer by generating the bundle with the corresponding cosigner transactions.
     * Does not contain signatures.
     *
     * @param securitySum      The sum of security levels used by all co-signers.
     * @param inputAddress     Array of input addresses as well as the securitySum.
     * @param remainderAddress Has to be generated by the cosigners before initiating the transfer, can be null if fully spent.
     * @param transfers        List of {@link Transfer} we want to make using the unputAddresses
     * @param tips             The starting points for checking if the balances of the input addresses contain enough to make this transfer
     *                         This can be <tt>null</tt>
     * @return All the {@link Transaction} objects in this newly created transfer
     * @throws ArgumentException     when an address is invalid.
     * @throws ArgumentException     when the security level is wrong.
     * @throws IllegalStateException when a transfer fails because their is not enough balance to perform the transfer.
     * @throws IllegalStateException When a <tt>remainderAddress</tt> is required, but not supplied
     * @throws RuntimeException      When the total value from the transfers is not 0
     */
    public List<Transaction> initiateTransfer(int securitySum, final String inputAddress, String remainderAddress,
                                              final List<Transfer> transfers, List<Transaction> tips) throws ArgumentException {
        return initiateTransfer(securitySum, inputAddress, remainderAddress, transfers, tips, false);
    }

    /**
     * Prepares transfer by generating the bundle with the corresponding cosigner transactions.
     * Does not contain signatures.
     *
     * @param securitySum      The sum of security levels used by all co-signers.
     * @param inputAddress     Array of input addresses as well as the securitySum.
     * @param remainderAddress Has to be generated by the cosigners before initiating the transfer, can be null if fully spent.
     * @param transfers        List of {@link Transfer} we want to make using the unputAddresses
     * @param testMode         If were running unit tests, set to true to bypass total value check
     * @return All the {@link Transaction} objects in this newly created transfer
     * @throws ArgumentException     when an address is invalid.
     * @throws ArgumentException     when the security level is wrong.
     * @throws IllegalStateException when a transfer fails because their is not enough balance to perform the transfer.
     * @throws IllegalStateException When a <tt>remainderAddress</tt> is required, but not supplied
     * @throws RuntimeException      When the total value from the transfers is not 0
     */
    public List<Transaction> initiateTransfer(int securitySum, String inputAddress, String remainderAddress,
                                              List<Transfer> transfers, boolean testMode) throws ArgumentException {
        return initiateTransfer(securitySum, inputAddress, remainderAddress, transfers, null, testMode);
    }

    /**
     * Prepares transfer by generating the bundle with the corresponding cosigner transactions.
     * Does not contain signatures.
     *
     * @param securitySum      The sum of security levels used by all co-signers.
     * @param inputAddress     Input address with checksum
     * @param remainderAddress Has to be generated by the cosigners before initiating the transfer, can be null if fully spent.
     * @param transfers        List of {@link Transfer} we want to make using the inputAddress
     * @param tips             The starting points for checking if the balance of the input address contains enough to make this transfer.
     *                         This can be <tt>null</tt>
     * @param testMode         If were running unit tests, set to true to bypass total value check
     * @return All the {@link Transaction} objects in this newly created transfer
     * @throws ArgumentException     when an address is invalid.
     * @throws ArgumentException     when the security level is wrong.
     * @throws IllegalStateException when a transfer fails because their is not enough balance to perform the transfer.
     * @throws IllegalStateException When a <tt>remainderAddress</tt> is required, but not supplied
     * @throws RuntimeException      When the total value from the transfers is not 0
     */
    @Document
    public List<Transaction> initiateTransfer(int securitySum, String inputAddress, String remainderAddress,
                                              List<Transfer> transfers, List<Transaction> tips,
                                              boolean testMode) throws ArgumentException {
        if (securitySum < Constants.MIN_SECURITY_LEVEL) {
            throw new ArgumentException(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        // validate input address
        if (!InputValidator.isAddress(inputAddress)) {
            throw new ArgumentException(Constants.INVALID_ADDRESSES_INPUT_ERROR);
        }

        // validate remainder address
        if (remainderAddress != null && !InputValidator.isAddress(remainderAddress)) {
            throw new ArgumentException(Constants.INVALID_ADDRESSES_INPUT_ERROR);
        }

        // Input validation of transfers object
        if (!InputValidator.isTransfersCollectionValid(transfers)) {
            throw new ArgumentException(Constants.INVALID_TRANSFERS_INPUT_ERROR);
        }

        // Create a new bundle
        final Bundle bundle = new Bundle();

        int totalValue = 0;
        final List<String> signatureFragments = new ArrayList<>();
        String tag = "";
        //

        //  Iterate over all transfers, get totalValue
        //  and prepare the signatureFragments, message and tag
        for (final Transfer transfer : transfers) {
            int signatureMessageLength = 1;

            // If message longer than 2187 trytes, increase signatureMessageLength (add next transaction)
            if (transfer.getMessage().length() > Constants.MESSAGE_LENGTH) {

                // Get total length, message / maxLength (2187 trytes)
                signatureMessageLength += Math.floor(transfer.getMessage().length() / Constants.MESSAGE_LENGTH);

                String msgCopy = transfer.getMessage();

                // While there is still a message, copy it
                while (!msgCopy.isEmpty()) {

                    String fragment = StringUtils.substring(msgCopy, 0, Constants.MESSAGE_LENGTH);
                    msgCopy = StringUtils.substring(msgCopy, Constants.MESSAGE_LENGTH, msgCopy.length());

                    // Pad remainder of fragment

                    fragment = StringUtils.rightPad(fragment, Constants.MESSAGE_LENGTH, '9');

                    signatureFragments.add(fragment);
                }

            } else {

                // Else, get single fragment with 2187 of 9's trytes
                String fragment = transfer.getMessage();

                if (transfer.getMessage().length() < Constants.MESSAGE_LENGTH) {
                    fragment = StringUtils.rightPad(fragment, Constants.MESSAGE_LENGTH, '9');
                }

                signatureFragments.add(fragment);

            }

            tag = transfer.getTag();

            // pad for required 27 tryte length
            if (transfer.getTag().length() < Constants.TAG_LENGTH) {
                tag = StringUtils.rightPad(tag, Constants.TAG_LENGTH, '9');
            }

            // get current timestamp in seconds
            long timestamp = (long) Math.floor(Calendar.getInstance().getTimeInMillis() / 1000);

            // Add first entry to the bundle
            bundle.addEntry(signatureMessageLength, Checksum.removeChecksum(transfer.getAddress()), transfer.getValue(), tag, timestamp);
            // Sum up total value
            totalValue += transfer.getValue();
        }

        // Get inputs if we are sending tokens
        if (totalValue != 0) {
            for (Transfer t : transfers) {
                if (!InputValidator.hasTrailingZeroTrit(t.getAddress())) {
                    throw new ArgumentException(Constants.INVALID_ADDRESSES_INPUT_ERROR);
                }
            }

            List<String> tipHashes = null;
            if (tips != null) {
                tipHashes = new ArrayList<>(tips.size());
                for (final Transaction tx : tips) {
                    tipHashes.add(tx.getHash());
                }
            }


            GetBalancesResponse balancesResponse = getBalances(Collections.singletonList(inputAddress), tipHashes);
            String[] balances = balancesResponse.getBalances();

            long totalBalance = 0;

            for (String balance : balances) {
                long thisBalance = Long.parseLong(balance);

                totalBalance += thisBalance;
            }

            // get current timestamp in seconds
            long timestamp = (long) Math.floor(Calendar.getInstance().getTimeInMillis() / 1000);

            // bypass the balance checks during unit testing
            //TODO remove this uglyness
            if (testMode) {
                totalBalance += 1000;
            }

            if (totalBalance > 0) {

                long toSubtract = 0 - totalBalance;

                // Add input as bundle entry
                // Only a single entry, signatures will be added later
                bundle.addEntry(securitySum, Checksum.removeChecksum(inputAddress), toSubtract, tag, timestamp);
            }
            // Return not enough balance error
            if (totalValue > totalBalance) {
                throw new IllegalStateException(Constants.NOT_ENOUGH_BALANCE_ERROR);
            }

            // If there is a remainder value
            // Add extra output to send remaining funds to
            if (totalBalance > totalValue) {

                long remainder = totalBalance - totalValue;

                // Remainder bundle entry if necessary
                if (remainderAddress == null) {
                    throw new IllegalStateException(Constants.NO_REMAINDER_ADDRESS_ERROR);
                }

                bundle.addEntry(1, remainderAddress, remainder, tag, timestamp);
            }
            bundle.finalize(SpongeFactory.create(SpongeFactory.Mode.KERL));
            bundle.addTrytes(signatureFragments);

            return bundle.getTransactions();
        } else {
            throw new RuntimeException(Constants.INVALID_VALUE_TRANSFER_ERROR);
        }

    }

    /**
     * <p>
     * Validates the supplied transactions with seed and security.
     * This will check for correct input/output and key reuse
     * </p>
     * <p>
     * In order to do this we will generate all addresses for this seed which are currently in use.
     * Address checksums will be regenerated and these addresses will be looked up, making this an expensive method call.
     * </p>
     * If no error is thrown, the transaction trytes are using correct addresses.
     * This will not validate transaction fields.
     *
     * @param seed     The tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param trytes   List of transaction trytes.
     * @throws ArgumentException when we are sending to our own input addresses
     * @throws ArgumentException when we try to remove funds from an address that is not an input
     * @throws ArgumentException when we are sending from an address we have already used for sending
     */
    @Document
    public void validateTransfersAddresses(String seed, int security, List<String> trytes) throws ArgumentException {

        HashSet<String> addresses = new HashSet<>();
        List<Transaction> inputTransactions = new ArrayList<>();
        List<String> inputAddresses = new ArrayList<>();

        for (String trx : trytes) {
            Transaction transaction = new Transaction(trx, SpongeFactory.create(SpongeFactory.Mode.CURL_P81));
            addresses.add(Checksum.addChecksum(transaction.getAddress()));
            inputTransactions.add(transaction);
        }

        String[] hashes = findTransactionsByAddresses(addresses.toArray(new String[0])).getHashes();
        List<Transaction> transactions = findTransactionsObjectsByHashes(hashes);

        // Get addresses until first unspent
        AddressRequest addressRequest = new AddressRequest.Builder(seed, security).amount(0).checksum(true).build();
        GetNewAddressResponse gna = generateNewAddresses(addressRequest);

        // Get inputs for this seed, until we fund an unused address
        GetBalancesAndFormatResponse gbr = getInputs(seed, security, 0, 0, 0);

        for (Input input : gbr.getInputs()) {
            inputAddresses.add(input.getAddress());
        }

        //check if receive address is also used as an input address
        for (Transaction trx : inputTransactions) {
            if (trx.getValue() > 0) {
                if (inputAddresses.contains(trx.getAddress())) {
                    throw new ArgumentException(Constants.SEND_TO_INPUTS_ERROR);
                } else if (!InputValidator.hasTrailingZeroTrit(trx.getAddress())) {
                    throw new ArgumentException(Constants.INVALID_ADDRESSES_INPUT_ERROR);
                }
            }
        }

        for (Transaction trx : transactions) {

            //check if destination address is already in use
            if (trx.getValue() < 0 && !inputAddresses.contains(trx.getAddress())) {
                throw new ArgumentException(Constants.SENDING_TO_USED_ADDRESS_ERROR);
            }

            //check if key reuse
            if (trx.getValue() < 0 && gna.getAddresses().contains(trx.getAddress())) {
                throw new ArgumentException(Constants.PRIVATE_KEY_REUSE_ERROR);
            }

        }
    }

    /**
     * Uses input, and adds to the bundle, untill <tt>totalValue</tt> is reached.
     * If there is a remainder left on the last input, a remainder transfer is added.
     *
     * @param seed               The tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security           Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param inputs             List of inputs used for funding the transfer.
     * @param bundle             The {@link Bundle} to be populated.
     * @param tag                The tag to add to each bundle entry (input and remainder)
     * @param totalValue         The total value of the desired transaction
     * @param remainderAddress   The address used for sending the remainder value (of the last input).
     *                           If this is <tt>null</tt>, {@link #generateNewAddresses(AddressRequest)} is used.
     * @param signatureFragments The signature fragments (message), used for signing.
     *                           Should be 2187 characters long, can be padded with 9s.
     * @return A list of signed inputs to be used in a transaction
     * @throws ArgumentException     When the seed is invalid
     * @throws ArgumentException     When the security level is wrong.
     * @throws IllegalStateException When the inputs do not contain enough balance to reach <tt>totalValue</tt>.
     * @see IotaAPIUtils#signInputsAndReturn(String, List, Bundle, List, org.iota.jota.pow.ICurl)
     * @see #generateNewAddresses(AddressRequest)
     */
    @Document
    public List<String> addRemainder(String seed, int security, List<Input> inputs, Bundle bundle,
                                     String tag, long totalValue, String remainderAddress,
                                     List<String> signatureFragments) throws ArgumentException {
        // validate seed
        if ((!InputValidator.isValidSeed(seed))) {
            throw new IllegalStateException(Constants.INVALID_SEED_INPUT_ERROR);
        }

        if (remainderAddress != null && !InputValidator.checkAddress(remainderAddress)) {
            throw new ArgumentException(Constants.INVALID_ADDRESSES_INPUT_ERROR);
        }

        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }
        if (!InputValidator.areValidInputsList(inputs)) {
            throw new ArgumentException(Constants.INVALID_INPUT_ERROR);
        }

        long totalTransferValue = totalValue;
        for (int i = 0; i < inputs.size(); i++) {
            long thisBalance = inputs.get(i).getBalance();
            long toSubtract = 0 - thisBalance;
            long timestamp = (long) Math.floor(Calendar.getInstance().getTimeInMillis() / 1000);

            // Add input as bundle entry
            bundle.addEntry(security, Checksum.removeChecksum(inputs.get(i).getAddress()), toSubtract, tag, timestamp);
            // If there is a remainder value
            // Add extra output to send remaining funds to

            if (thisBalance >= totalTransferValue) {
                long remainder = thisBalance - totalTransferValue;

                // If user has provided remainder address
                // Use it to send remaining funds to
                if (remainder > 0 && remainderAddress != null) {
                    // Remainder bundle entry
                    bundle.addEntry(1, Checksum.removeChecksum(remainderAddress), remainder, tag, timestamp);
                    // Final function for signing inputs
                    return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments, getCurl());
                } else if (remainder > 0) {
                    AddressRequest addressRequest = new AddressRequest.Builder(seed, security).build();
                    GetNewAddressResponse res = generateNewAddresses(addressRequest);
                    // Remainder bundle entry
                    bundle.addEntry(1, res.getAddresses().get(0), remainder, tag, timestamp);

                    // Final function for signing inputs
                    return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments, getCurl());
                } else {
                    // If there is no remainder, do not add transaction to bundle
                    // simply sign and return
                    return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments, getCurl());
                }

                // If multiple inputs provided, subtract the totalTransferValue by
                // the inputs balance
            } else {
                totalTransferValue -= thisBalance;
            }
        }
        throw new IllegalStateException(Constants.NOT_ENOUGH_BALANCE_ERROR);
    }

    /**
     * Checks if a transaction hash is promotable
     *
     * @param tail the {@link Transaction} we want to promote
     * @return <tt>true</tt> if it is, otherwise <tt>false</tt>
     * @throws ArgumentException When we cant find the transaction
     */
    @Document
    public boolean isPromotable(Transaction tail) throws ArgumentException {
        long lowerBound = tail.getAttachmentTimestamp();

        return isAboveMaxDepth(lowerBound);
    }

    /**
     * Checks if a transaction hash is promotable
     *
     * @param tail the {@link Transaction} hash we want to check
     * @return <tt>true</tt> if it is, otherwise <tt>false</tt>
     * @throws ArgumentException When the transaction is not found
     */
    public boolean isPromotable(String tail) throws ArgumentException {
        GetTrytesResponse transaction = getTrytes(tail);
        if (0 == transaction.getTrytes().length) {
            throw new ArgumentException(Constants.TRANSACTION_NOT_FOUND);
        }

        return isPromotable(new Transaction(transaction.getTrytes()[0]));
    }

    private boolean isAboveMaxDepth(long attachmentTimestamp) {
        // Check against future timestamps
        return attachmentTimestamp < System.currentTimeMillis() &&
                /*
                 * Check if transaction wasn't issued before last 6 milestones
                 * Without a coo, technically there is no limit for promotion on the network.
                 * But old transactions are less likely to be selected in tipselection
                 * This means that the higher maxdepth, the lower the use of promoting is.
                 * 6 was picked by means of "observing" nodes for their most popular depth, and 6 was the "edge" of popularity.
                 *
                 * Milestones are being issued every ~2mins
                 *
                 * The 11 is  calculated like this:
                 * 6 milestones is the limit, so 5 milestones are used, each 2 minutes each totaling 10 minutes.
                 * Add 1 minute delay for propagating through the network of nodes.
                 *
                 * That's why its 11 and not 10 or 12 (*60*1000)
                 */
                System.currentTimeMillis() - attachmentTimestamp < 11 * 60 * 1000;
    }


    /**
     * Attempts to promote a transaction using a provided bundle and, if successful, returns the promoting Transactions.
     * This is done by creating another transaction which points to the tail.
     * This will effectively double the chances of the transaction to be picked, and this approved.
     *
     * @param tail               bundle tail to promote, cannot be <tt>null</tt>
     * @param depth              depth for getTransactionsToApprove
     * @param minWeightMagnitude minWeightMagnitude to use for Proof-of-Work
     * @param bundle             the {@link Bundle} to attach for promotion
     * @return List of the bundle {@link Transaction}s made with the attached transaction trytes
     * @throws ArgumentException      When the bundle has no transaction
     * @throws ArgumentException      When <tt>depth</tt> or <tt>minWeightMagnitude</tt> is lower than 0
     * @throws ArgumentException      When the <tt>tail</tt> hash is invalid
     * @throws NotPromotableException When the transaction is not promotable
     * @see #getTransactionsToApprove(Integer, String)
     * @see #attachToTangle(String, String, Integer, String...)
     * @see #storeAndBroadcast(String...)
     */
    @Document
    public List<Transaction> promoteTransaction(String tail, int depth, int minWeightMagnitude, Bundle bundle) throws BaseException {
        if (bundle == null || bundle.getTransactions().size() == 0) {
            throw new ArgumentException("Need at least one transaction in the bundle");
        }

        if (depth < 0) {
            throw new ArgumentException("Depth must be >= 0");
        }

        if (minWeightMagnitude <= 0) {
            throw new ArgumentException("MinWeightMagnitude must be > 0");
        }

        GetTransactionsToApproveResponse transactionsToApprove = getTransactionsToApprove(depth, tail);

        final GetAttachToTangleResponse res = attachToTangle(transactionsToApprove.getTrunkTransaction(), transactionsToApprove.getBranchTransaction(), minWeightMagnitude,
                bundle.getTransactions().stream().map(Transaction::toTrytes).toArray(String[]::new));

        try {
            storeAndBroadcast(res.getTrytes());
        } catch (ArgumentException e) {
            return Collections.emptyList();
        }

        return Arrays.stream(res.getTrytes()).map(trytes -> new Transaction(trytes, getCurl())).collect(toList());
    }

    public static class Builder extends ApiBuilder<Builder, IotaAPI> {

        @Override
        protected IotaAPI compile() {
            return new IotaAPI(this);
        }
    }
}
