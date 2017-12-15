package jota;

import jota.dto.response.*;
import jota.error.ArgumentException;
import jota.model.*;
import jota.pow.ICurl;
import jota.pow.SpongeFactory;
import jota.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static jota.utils.Constants.*;


/**
 * IotaAPI Builder. Usage:
 * <p>
 * {@code IotaApiProxy api = IotaApiProxy.Builder}
 * {@code .protocol("http")}
 * {@code .nodeAddress("localhost")}
 * {@code .port(12345)}
 * {@code .build();}
 *
 * {@code GetNodeInfoResponse response = api.getNodeInfo();}
 *
 * @author davassi
 */
public class IotaAPI extends IotaAPICore {

    private static final Logger log = LoggerFactory.getLogger(IotaAPI.class);
    private ICurl customCurl;

    protected IotaAPI(Builder builder) {
        super(builder);
        customCurl = builder.customCurl;
    }

    /**
     * Generates a new address from a seed and returns the remainderAddress.
     * This is either done deterministically, or by providing the index of the new remainderAddress.
     *
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param index     Key index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @param checksum  Adds 9-tryte address checksum.
     * @param total     Total number of addresses to generate.
     * @param returnAll If <code>true</code>, it returns all addresses which were deterministically generated (until findTransactions returns null).
     * @return An array of strings with the specifed number of addresses.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public GetNewAddressResponse getNewAddress(final String seed, int security, final int index, final boolean checksum, final int total, final boolean returnAll) throws ArgumentException {

        StopWatch stopWatch = new StopWatch();

        List<String> allAddresses = new ArrayList<>();

        // If total number of addresses to generate is supplied, simply generate
        // and return the list of all addresses
        if (total != 0) {
            for (int i = index; i < index + total; i++) {
                allAddresses.add(IotaAPIUtils.newAddress(seed, security, i, checksum, customCurl.clone()));
            }
            return GetNewAddressResponse.create(allAddresses, stopWatch.getElapsedTimeMili());
        }


        // No total provided: Continue calling findTransactions to see if address was
        // already created if null, return list of addresses
        for (int i = index; ; i++) {

            final String newAddress = IotaAPIUtils.newAddress(seed, security, i, checksum, customCurl.clone());
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
        return GetNewAddressResponse.create(allAddresses, stopWatch.getElapsedTimeMili());
    }

    /**
     * @param seed            Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security        The security level of private key / seed.
     * @param start           Starting key index.
     * @param end             Ending key index.
     * @param inclusionStates If <code>true</code>, it gets the inclusion states of the transfers.
     * @return Bundle of transfers.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public GetTransferResponse getTransfers(String seed, int security, Integer start, Integer end, Boolean inclusionStates) throws ArgumentException {

        // validate seed
        if ((!InputValidator.isValidSeed(seed))) {
            throw new IllegalStateException(INVALID_SEED_INPUT_ERROR);
        }

        if (start > end || end > (start + 500)) {
            throw new ArgumentException(INVALID_INPUT_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        GetNewAddressResponse gnr = getNewAddress(seed, security, start, false, end, true);
        if (gnr != null && gnr.getAddresses() != null) {
            Bundle[] bundles = bundlesFromAddresses(gnr.getAddresses().toArray(new String[gnr.getAddresses().size()]), inclusionStates);
            return GetTransferResponse.create(bundles, stopWatch.getElapsedTimeMili());
        }
        return GetTransferResponse.create(new Bundle[]{}, stopWatch.getElapsedTimeMili());
    }

    /**
     * Internal function to get the formatted bundles of a list of addresses.
     *
     * @param addresses       List of addresses.
     * @param inclusionStates If <code>true</code>, it gets the inclusion states of the transfers.
     * @return A Transaction objects.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public Bundle[] bundlesFromAddresses(String[] addresses, final Boolean inclusionStates) throws ArgumentException {

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

        List<Transaction> bundleObjects = findTransactionObjectsByBundle(nonTailBundleHashes.toArray(new String[nonTailBundleHashes.size()]));
        for (Transaction trx : bundleObjects) {
            // Sort tail and nonTails
            if (trx.getCurrentIndex() == 0) {
                if (tailTransactions.indexOf(trx.getHash()) == -1) {
                    tailTransactions.add(trx.getHash());
                }
            }
        }

        final List<Bundle> finalBundles = new ArrayList<>();
        final String[] tailTxArray = tailTransactions.toArray(new String[tailTransactions.size()]);

        // If inclusionStates, get the confirmation status
        // of the tail transactions, and thus the bundles
        GetInclusionStateResponse gisr = null;
        if (tailTxArray.length != 0 && inclusionStates) {
                gisr = getLatestInclusion(tailTxArray);
            if (gisr == null || gisr.getStates() == null || gisr.getStates().length == 0) {
                throw new IllegalStateException(GET_INCLUSION_STATE_RESPONSE_ERROR);
            }
        }
        final GetInclusionStateResponse finalInclusionStates = gisr;
        Parallel.For(Arrays.asList(tailTxArray),
                new Parallel.Operation<String>() {
                    public void perform(String param) {

                        try {
                            GetBundleResponse bundleResponse = getBundle(param);
                            Bundle gbr = new Bundle(bundleResponse.getTransactions(), bundleResponse.getTransactions().size());
                            if (gbr.getTransactions() != null) {
                                if (inclusionStates) {
                                    boolean thisInclusion = false;
                                    if (finalInclusionStates != null) {
                                        thisInclusion = finalInclusionStates.getStates()[Arrays.asList(tailTxArray).indexOf(param)];
                                    }
                                    for (Transaction t : gbr.getTransactions()) {
                                        t.setPersistence(thisInclusion);
                                    }
                                }
                                finalBundles.add(gbr);
                            }
                            // If error returned from getBundle, simply ignore it because the bundle was most likely incorrect
                        } catch (ArgumentException e) {
                            log.warn(GET_BUNDLE_RESPONSE_ERROR);
                        }
                    }
                });

        Collections.sort(finalBundles);
        Bundle[] returnValue = new Bundle[finalBundles.size()];
        for (int i = 0; i < finalBundles.size(); i++) {
            returnValue[i] = new Bundle(finalBundles.get(i).getTransactions(), finalBundles.get(i).getTransactions().size());
        }
        return returnValue;
    }

    /**
     * Wrapper function that broadcasts and stores the specified trytes.
     *
     * @param trytes The trytes.
     * @return A StoreTransactionsResponse.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public StoreTransactionsResponse broadcastAndStore(final String... trytes) throws ArgumentException {

        if (!InputValidator.isArrayOfAttachedTrytes(trytes)) {
            throw new ArgumentException(INVALID_TRYTES_INPUT_ERROR);
        }

        try {
            broadcastTransactions(trytes);
        } catch (Exception e) {
            throw new ArgumentException(e.toString());
        }
        return storeTransactions(trytes);
    }

    /**
     * Facade method: Gets transactions to approve, attaches to Tangle, broadcasts and stores.
     *
     * @param trytes             The trytes.
     * @param depth              The depth.
     * @param minWeightMagnitude The minimum weight magnitude.
     * @return Transactions objects.
     * @throws ArgumentException is thrown when invalid trytes is provided.
     */
    public List<Transaction> sendTrytes(final String[] trytes, final int depth, final int minWeightMagnitude) throws ArgumentException {
        final GetTransactionsToApproveResponse txs = getTransactionsToApprove(depth);

        // attach to tangle - do pow
        final GetAttachToTangleResponse res = attachToTangle(txs.getTrunkTransaction(), txs.getBranchTransaction(), minWeightMagnitude, trytes);

        try {
            broadcastAndStore(res.getTrytes());
        } catch (ArgumentException e) {
            return new ArrayList<>();
        }

        final List<Transaction> trx = new ArrayList<>();

        for (final String tryte : Arrays.asList(res.getTrytes())) {
            trx.add(new Transaction(tryte, customCurl.clone()));
        }
        return trx;
    }

    /**
     * Wrapper function for getTrytes and transactionObjects.
     * Gets the trytes and transaction object from a list of transaction hashes.
     *
     * @param hashes The hashes
     * @return Transaction objects.
     **/
    public List<Transaction> findTransactionsObjectsByHashes(String[] hashes) throws ArgumentException {

        if (!InputValidator.isArrayOfHashes(hashes)) {
            throw new IllegalStateException(INVALID_HASHES_INPUT_ERROR);
        }

        final GetTrytesResponse trytesResponse = getTrytes(hashes);

        final List<Transaction> trxs = new ArrayList<>();

        for (final String tryte : trytesResponse.getTrytes()) {
            trxs.add(new Transaction(tryte, customCurl.clone()));
        }
        return trxs;
    }

    /**
     * Wrapper function for findTransactions, getTrytes and transactionObjects.
     * Returns the transactionObject of a transaction hash. The input can be a list of valid addresses.
     *
     * @param addresses The addresses.
     * @return Transactions.
     **/
    public List<Transaction> findTransactionObjectsByAddresses(String[] addresses) throws ArgumentException {
        List<String> addressesWithoutChecksum = new ArrayList<>();

        for (String address : addresses) {
            String addressO = Checksum.removeChecksum(address);
            addressesWithoutChecksum.add(addressO);
        }

        FindTransactionResponse ftr = findTransactions(addressesWithoutChecksum.toArray(new String[]{}), null, null, null);
        if (ftr == null || ftr.getHashes() == null)
            return new ArrayList<>();
        // get the transaction objects of the transactions
        return findTransactionsObjectsByHashes(ftr.getHashes());
    }

    /**
     * Wrapper function for findTransactions, getTrytes and transactionObjects.
     * Returns the transactionObject of a transaction hash. The input can be a list of valid tags.
     *
     * @param tags The tags.
     * @return Transactions.
     **/
    public List<Transaction> findTransactionObjectsByTag(String[] tags) throws ArgumentException {
        FindTransactionResponse ftr = findTransactions(null, tags, null, null);
        if (ftr == null || ftr.getHashes() == null)
            return new ArrayList<>();
        // get the transaction objects of the transactions
        return findTransactionsObjectsByHashes(ftr.getHashes());
    }

    /**
     * Wrapper function for findTransactions, getTrytes and transactionObjects.
     * Returns the transactionObject of a transaction hash. The input can be a list of valid approvees.
     *
     * @param approvees The approvees.
     * @return Transactions.
     **/
    public List<Transaction> findTransactionObjectsByApprovees(String[] approvees) throws ArgumentException {
        FindTransactionResponse ftr = findTransactions(null, null, approvees, null);
        if (ftr == null || ftr.getHashes() == null)
            return new ArrayList<>();
        // get the transaction objects of the transactions
        return findTransactionsObjectsByHashes(ftr.getHashes());
    }

    /**
     * Wrapper function for findTransactions, getTrytes and transactionObjects.
     * Returns the transactionObject of a transaction hash. The input can be a list of valid bundles.
     * findTransactions input
     *
     * @param bundles The bundles.
     * @return Transactions.
     **/
    public List<Transaction> findTransactionObjectsByBundle(String[] bundles) throws ArgumentException {
        FindTransactionResponse ftr = findTransactions(null, null, null, bundles);
        if (ftr == null || ftr.getHashes() == null)
            return new ArrayList<>();

        // get the transaction objects of the transactions
        return findTransactionsObjectsByHashes(ftr.getHashes());
    }

    /**
     * Prepares transfer by generating bundle, finding and signing inputs.
     *
     * @param seed           Tryte-encoded private key / seed.
     * @param security       The security level of private key / seed.
     * @param transfers      Array of transfer objects.
     * @param remainder      If defined, this address will be used for sending the remainder value (of the inputs) to.
     * @param inputs         The inputs.
     * @param validateInputs whether or not to validate the balances of the provided inputs
     * @return Returns bundle trytes.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public List<String> prepareTransfers(String seed, int security, final List<Transfer> transfers, String remainder, List<Input> inputs, boolean validateInputs) throws ArgumentException {

        // validate seed
        if ((!InputValidator.isValidSeed(seed))) {
            throw new IllegalStateException(INVALID_SEED_INPUT_ERROR);
        }

        if (security < 1) {
            throw new ArgumentException(INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        // Input validation of transfers object
        if (!InputValidator.isTransfersCollectionValid(transfers)) {
            throw new ArgumentException(INVALID_TRANSFERS_INPUT_ERROR);
        }

        // Create a new bundle
        final Bundle bundle = new Bundle();
        final List<String> signatureFragments = new ArrayList<>();

        long totalValue = 0;
        String tag = "";
        //  Iterate over all transfers, get totalValue
        //  and prepare the signatureFragments, message and tag
        for (final Transfer transfer : transfers) {

            // remove the checksum of the address if provided
            if (Checksum.isValidChecksum(transfer.getAddress())) {
                transfer.setAddress(Checksum.removeChecksum(transfer.getAddress()));
            }

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
            bundle.addEntry(signatureMessageLength, transfer.getAddress(), transfer.getValue(), tag, timestamp);
            // Sum up total value
            totalValue += transfer.getValue();
        }

        // Get inputs if we are sending tokens
        if (totalValue != 0) {

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


                GetBalancesResponse balancesResponse = getBalances(100, inputsAddresses);
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
                            log.info("Total balance already reached ");
                            break;
                        }
                    }

                }

                // Return not enough balance error
                if (totalValue > totalBalance) {
                    throw new IllegalStateException(NOT_ENOUGH_BALANCE_ERROR);
                }

                return addRemainder(seed, security, confirmedInputs, bundle, tag, totalValue, remainder, signatureFragments);
            }

            //  Case 2: Get inputs deterministically
            //
            //  If no inputs provided, derive the addresses from the seed and
            //  confirm that the inputs exceed the threshold
            else {

                @SuppressWarnings("unchecked") GetBalancesAndFormatResponse newinputs = getInputs(seed, security, 0, 0, totalValue);
                // If inputs with enough balance
                return addRemainder(seed, security, newinputs.getInputs(), bundle, tag, totalValue, remainder, signatureFragments);
            }
        } else {

            // If no input required, don't sign and simply finalize the bundle
            bundle.finalize(customCurl.clone());
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
     *
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security  The Security level of private key / seed.
     * @param start     Starting key index.
     * @param end       Ending key index.
     * @param threshold Min balance required.
     * @throws ArgumentException is thrown when the specified input is not valid.
     **/
    public GetBalancesAndFormatResponse getInputs(String seed, int security, int start, int end, long threshold) throws ArgumentException {

        // validate the seed
        if ((!InputValidator.isValidSeed(seed))) {
            throw new IllegalStateException(INVALID_SEED_INPUT_ERROR);
        }

        if (security < 1) {
            throw new ArgumentException(INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        // If start value bigger than end, return error
        // or if difference between end and start is bigger than 500 keys
        if (start > end || end > (start + 500)) {
            throw new IllegalStateException(INVALID_INPUT_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        //  Case 1: start and end
        //
        //  If start and end is defined by the user, simply iterate through the keys
        //  and call getBalances
        if (end != 0) {

            List<String> allAddresses = new ArrayList<>();

            for (int i = start; i < end; i++) {

                String address = IotaAPIUtils.newAddress(seed, security, i, false, customCurl.clone());
                allAddresses.add(address);
            }

            return getBalanceAndFormat(allAddresses, threshold, start, stopWatch, security);
        }
        //  Case 2: iterate till threshold || end
        //
        //  Either start from index: 0 or start (if defined) until threshold is reached.
        //  Calls getNewAddress and deterministically generates and returns all addresses
        //  We then do getBalance, format the output and return it
        else {
            final GetNewAddressResponse res = getNewAddress(seed, security, start, false, 0, true);
            return getBalanceAndFormat(res.getAddresses(), threshold, start, stopWatch, security);
        }
    }

    /**
     * Gets the balances and formats the output.
     *
     * @param addresses The addresses.
     * @param threshold Min balance required.
     * @param start     Starting key index.
     * @param stopWatch the stopwatch.
     * @param security  The security level of private key / seed.
     * @return Inputs object.
     * @throws ArgumentException is thrown when the specified security level is not valid.
     **/
    public GetBalancesAndFormatResponse getBalanceAndFormat(final List<String> addresses, long threshold, int start, StopWatch stopWatch, int security) throws ArgumentException, IllegalStateException {

        if (security < 1) {
            throw new ArgumentException(INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        GetBalancesResponse getBalancesResponse = getBalances(100, addresses);
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
            return GetBalancesAndFormatResponse.create(inputs, totalBalance, stopWatch.getElapsedTimeMili());
        }
        throw new IllegalStateException(NOT_ENOUGH_BALANCE_ERROR);
    }

    /**
     * Gets the associated bundle transactions of a single transaction.
     * Does validation of signatures, total sum as well as bundle order.
     *
     * @param transaction The transaction encoded in trytes.
     * @return an array of bundle, if there are multiple arrays it means that there are conflicting bundles.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public GetBundleResponse getBundle(String transaction) throws ArgumentException {

        if (!InputValidator.isHash(transaction)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        Bundle bundle = traverseBundle(transaction, null, new Bundle());
        if (bundle == null) {
            throw new ArgumentException(INVALID_BUNDLE_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        long totalSum = 0;
        String bundleHash = bundle.getTransactions().get(0).getBundle();

        ICurl curl = SpongeFactory.create(SpongeFactory.Mode.KERL);
        curl.reset();

        List<Signature> signaturesToValidate = new ArrayList<>();

        for (int i = 0; i < bundle.getTransactions().size(); i++) {
            Transaction trx = bundle.getTransactions().get(i);
            Long bundleValue = trx.getValue();
            totalSum += bundleValue;

            if (i != bundle.getTransactions().get(i).getCurrentIndex()) {
                throw new ArgumentException(INVALID_BUNDLE_ERROR);
            }

            String trxTrytes = trx.toTrytes().substring(2187, 2187 + 162);
            // Absorb bundle hash + value + timestamp + lastIndex + currentIndex trytes.
            curl.absorb(Converter.trits(trxTrytes));
            // Check if input transaction
            if (bundleValue < 0) {
                String address = trx.getAddress();
                Signature sig = new Signature();
                sig.setAddress(address);
                sig.getSignatureFragments().add(trx.getSignatureFragments());

                // Find the subsequent txs with the remaining signature fragment
                for (int y = i + 1; y < bundle.getTransactions().size(); y++) {
                    Transaction newBundleTx = bundle.getTransactions().get(y);

                    // Check if new tx is part of the signature fragment
                    if (newBundleTx.getAddress().equals(address) && newBundleTx.getValue() == 0) {
                        if (sig.getSignatureFragments().indexOf(newBundleTx.getSignatureFragments()) == -1)
                            sig.getSignatureFragments().add(newBundleTx.getSignatureFragments());
                    }
                }
                signaturesToValidate.add(sig);
            }
        }

        // Check for total sum, if not equal 0 return error
        if (totalSum != 0)
            throw new ArgumentException(INVALID_BUNDLE_SUM_ERROR);
        int[] bundleFromTrxs = new int[243];
        curl.squeeze(bundleFromTrxs);
        String bundleFromTxString = Converter.trytes(bundleFromTrxs);

        // Check if bundle hash is the same as returned by tx object
        if (!bundleFromTxString.equals(bundleHash))
            throw new ArgumentException(INVALID_BUNDLE_HASH_ERROR);
        // Last tx in the bundle should have currentIndex === lastIndex
        bundle.setLength(bundle.getTransactions().size());
        if (!(bundle.getTransactions().get(bundle.getLength() - 1).getCurrentIndex() == (bundle.getTransactions().get(bundle.getLength() - 1).getLastIndex())))
            throw new ArgumentException(INVALID_BUNDLE_ERROR);

        // Validate the signatures
        for (Signature aSignaturesToValidate : signaturesToValidate) {
            String[] signatureFragments = aSignaturesToValidate.getSignatureFragments().toArray(new String[aSignaturesToValidate.getSignatureFragments().size()]);
            String address = aSignaturesToValidate.getAddress();
            boolean isValidSignature = new Signing(customCurl.clone()).validateSignatures(address, signatureFragments, bundleHash);

            if (!isValidSignature)
                throw new ArgumentException(INVALID_SIGNATURES_ERROR);
        }

        return GetBundleResponse.create(bundle.getTransactions(), stopWatch.getElapsedTimeMili());
    }

    /**
     * Similar to getTransfers, just that it returns additional account data
     *
     * @param seed            Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security        The Security level of private key / seed.
     * @param index           Key index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @param checksum        Adds 9-tryte address checksum.
     * @param total           Total number of addresses to generate.
     * @param returnAll       If <code>true</code>, it returns all addresses which were deterministically generated (until findTransactions returns null).
     * @param start           Starting key index.
     * @param end             Ending key index.
     * @param inclusionStates If <code>true</code>, it gets the inclusion states of the transfers.
     * @param threshold       Min balance required.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public GetAccountDataResponse getAccountData(String seed, int security, int index, boolean checksum, int total, boolean returnAll, int start, int end, boolean inclusionStates, long threshold) throws ArgumentException {

        if (start > end || end > (start + 1000)) {
            throw new ArgumentException(INVALID_INPUT_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        GetNewAddressResponse gna = getNewAddress(seed, security, index, checksum, total, returnAll);
        GetTransferResponse gtr = getTransfers(seed, security, start, end, inclusionStates);
        GetBalancesAndFormatResponse gbr = getInputs(seed, security, start, end, threshold);

        return GetAccountDataResponse.create(gna.getAddresses(), gtr.getTransfers(), gbr.getInputs(), gbr.getTotalBalance(), stopWatch.getElapsedTimeMili());
    }

    /**
     * Replays a transfer by doing Proof of Work again.
     *
     * @param transaction        The transaction.
     * @param depth              The depth.
     * @param minWeightMagnitude The minimum weight magnitude.
     * @return Analyzed Transaction objects.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public ReplayBundleResponse replayBundle(String transaction, int depth, int minWeightMagnitude) throws ArgumentException {

        if (!InputValidator.isHash(transaction)) {
            throw new ArgumentException(INVALID_TAIL_HASH_INPUT_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        List<String> bundleTrytes = new ArrayList<>();

        GetBundleResponse bundleResponse = getBundle(transaction);
        Bundle bundle = new Bundle(bundleResponse.getTransactions(), bundleResponse.getTransactions().size());
        for (Transaction trx : bundle.getTransactions()) {

            bundleTrytes.add(trx.toTrytes());
        }

        Collections.reverse(bundleTrytes);
        List<Transaction> trxs = sendTrytes(bundleTrytes.toArray(new String[bundleTrytes.size()]), depth, minWeightMagnitude);

        Boolean[] successful = new Boolean[trxs.size()];

        for (int i = 0; i < trxs.size(); i++) {

            final FindTransactionResponse response = findTransactionsByBundles(trxs.get(i).getBundle());


            successful[i] = response.getHashes().length != 0;
        }

        return ReplayBundleResponse.create(successful, stopWatch.getElapsedTimeMili());
    }

    /**
     * Wrapper function for getNodeInfo and getInclusionStates
     *
     * @param hashes The hashes.
     * @return Inclusion state.
     */
    public GetInclusionStateResponse getLatestInclusion(String[] hashes) throws ArgumentException {
        GetNodeInfoResponse getNodeInfoResponse = getNodeInfo();

        String[] latestMilestone = {getNodeInfoResponse.getLatestSolidSubtangleMilestone()};

        return getInclusionStates(hashes, latestMilestone);
    }

    /**
     * Wrapper function that basically does prepareTransfers, as well as attachToTangle and finally, it broadcasts and stores the transactions locally.
     *
     * @param seed               Tryte-encoded seed
     * @param security           The security level of private key / seed.
     * @param depth              The depth.
     * @param minWeightMagnitude The minimum weight magnitude.
     * @param transfers          Array of transfer objects.
     * @param inputs             List of inputs used for funding the transfer.
     * @param remainderAddress   If defined, this remainderAddress will be used for sending the remainder value (of the inputs) to.
     * @param validateInputs     Whether or not to validate the balances of the provided inputs.
     * @param validateInputAddresses  Whether or not to validate if the destination address is already used, if a key reuse is detect ot it's send to inputs.
     * @return Array of valid Transaction objects.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public SendTransferResponse sendTransfer(String seed, int security, int depth, int minWeightMagnitude, final List<Transfer> transfers, List<Input> inputs, String remainderAddress, boolean validateInputs, boolean validateInputAddresses) throws ArgumentException {

        StopWatch stopWatch = new StopWatch();

        List<String> trytes = prepareTransfers(seed, security, transfers, remainderAddress, inputs, validateInputs);

        if (validateInputAddresses) {
            validateTransfersAddresses(seed, security, trytes);
        }

        List<Transaction> trxs = sendTrytes(trytes.toArray(new String[trytes.size()]), depth, minWeightMagnitude);

        Boolean[] successful = new Boolean[trxs.size()];

        for (int i = 0; i < trxs.size(); i++) {
            final FindTransactionResponse response = findTransactionsByBundles(trxs.get(i).getBundle());
            successful[i] = response.getHashes().length != 0;
        }

        return SendTransferResponse.create(trxs, successful, stopWatch.getElapsedTimeMili());
    }

    /**
     * Basically traverse the Bundle by going down the trunkTransactions until
     * the bundle hash of the transaction is no longer the same. In case the input
     * transaction hash is not a tail, we return an error.
     *
     * @param trunkTx    Hash of a trunk or a tail transaction of a bundle.
     * @param bundleHash The bundle hashes.
     * @param bundle     List of bundles to be populated.
     * @return Transaction objects.
     * @throws ArgumentException is thrown when an invalid input is provided.
     */
    public Bundle traverseBundle(String trunkTx, String bundleHash, Bundle bundle) throws ArgumentException {
        GetTrytesResponse gtr = getTrytes(trunkTx);

        if (gtr != null) {

            if (gtr.getTrytes().length == 0) {
                throw new ArgumentException(INVALID_BUNDLE_ERROR);
            }

            Transaction trx = new Transaction(gtr.getTrytes()[0], customCurl.clone());
            if (trx.getBundle() == null) {
                throw new ArgumentException(INVALID_TRYTES_INPUT_ERROR);
            }
            // If first transaction to search is not a tail, return error
            if (bundleHash == null && trx.getCurrentIndex() != 0) {
                throw new ArgumentException(INVALID_TAIL_HASH_INPUT_ERROR);
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
            throw new ArgumentException(GET_TRYTES_RESPONSE_ERROR);
        }
    }


    /**
     * Prepares transfer by generating the bundle with the corresponding cosigner transactions.
     * Does not contain signatures.
     *
     * @param securitySum      The sum of security levels used by all co-signers.
     * @param inputAddress     Array of input addresses as well as the securitySum.
     * @param remainderAddress Has to be generated by the cosigners before initiating the transfer, can be null if fully spent.
     * @return Bundle of transaction objects.
     * @throws ArgumentException is thrown when an invalid argument is provided.
     * @throws IllegalStateException     is thrown when a transfer fails because their is not enough balance to perform the transfer.
     */
    public List<Transaction> initiateTransfer(int securitySum, final String inputAddress, String remainderAddress,
                                              final List<Transfer> transfers, boolean testMode) throws ArgumentException {

        // validate input address
        if (!InputValidator.isAddress(inputAddress))
            throw new ArgumentException(INVALID_ADDRESSES_INPUT_ERROR);

        // validate remainder address
        if (remainderAddress != null && !InputValidator.isAddress(remainderAddress)) {
            throw new ArgumentException(INVALID_ADDRESSES_INPUT_ERROR);
        }

        // Input validation of transfers object
        if (!InputValidator.isTransfersCollectionValid(transfers)) {
            throw new ArgumentException(INVALID_TRANSFERS_INPUT_ERROR);
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

            // remove the checksum of the address if provided
            if (Checksum.isValidChecksum(transfer.getAddress())) {
                transfer.setAddress(Checksum.removeChecksum(transfer.getAddress()));
            }

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
            bundle.addEntry(signatureMessageLength, transfer.getAddress(), transfer.getValue(), tag, timestamp);
            // Sum up total value
            totalValue += transfer.getValue();
        }

        // Get inputs if we are sending tokens
        if (totalValue != 0) {

            GetBalancesResponse balancesResponse = getBalances(100, Collections.singletonList(inputAddress));
            String[] balances = balancesResponse.getBalances();

            long totalBalance = 0;

            for (String balance : balances) {
                long thisBalance = Long.parseLong(balance);

                totalBalance += thisBalance;
            }

            // get current timestamp in seconds
            long timestamp = (long) Math.floor(Calendar.getInstance().getTimeInMillis() / 1000);

            // bypass the balance checks during unit testing
            if (testMode)
                totalBalance += 1000;

            if (totalBalance > 0) {

                long toSubtract = 0 - totalBalance;

                // Add input as bundle entry
                // Only a single entry, signatures will be added later
                bundle.addEntry(securitySum, inputAddress, toSubtract, tag, timestamp);
            }
            // Return not enough balance error
            if (totalValue > totalBalance) {
                throw new IllegalStateException(NOT_ENOUGH_BALANCE_ERROR);
            }

            // If there is a remainder value
            // Add extra output to send remaining funds to
            if (totalBalance > totalValue) {

                long remainder = totalBalance - totalValue;

                // Remainder bundle entry if necessary
                if (remainderAddress == null) {
                    throw new IllegalStateException(NO_REMAINDER_ADDRESS_ERROR);
                }

                bundle.addEntry(1, remainderAddress, remainder, tag, timestamp);
            }

            bundle.finalize(SpongeFactory.create(SpongeFactory.Mode.CURLP81));
            bundle.addTrytes(signatureFragments);

            return bundle.getTransactions();
        } else {
            throw new RuntimeException(INVALID_VALUE_TRANSFER_ERROR);
        }

    }

    /**
     * @param seed     Tryte-encoded seed
     * @param security The security level of private key / seed.
     * @param trytes   The trytes.
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public void validateTransfersAddresses(String seed, int security, List<String> trytes) throws ArgumentException {

        HashSet<String> addresses = new HashSet<>();
        List<Transaction> inputTransactions = new ArrayList<>();
        List<String> inputAddresses = new ArrayList<>();

        for (String trx : trytes) {
            addresses.add(new Transaction(trx, customCurl.clone()).getAddress());
            inputTransactions.add(new Transaction(trx, customCurl.clone()));
        }

        String[] hashes = findTransactionsByAddresses(addresses.toArray(new String[addresses.size()])).getHashes();
        List<Transaction> transactions = findTransactionsObjectsByHashes(hashes);
        GetNewAddressResponse gna = getNewAddress(seed, security, 0, false, 0, true);
        GetBalancesAndFormatResponse gbr = getInputs(seed, security, 0, 0, 0);

        for (Input input : gbr.getInputs()) {
            inputAddresses.add(input.getAddress());
        }

        //check if send to input
        for (Transaction trx : inputTransactions) {
            if (trx.getValue() > 0 && inputAddresses.contains(trx.getAddress()))
                throw new ArgumentException(Constants.SEND_TO_INPUTS_ERROR);
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
     * @param seed               Tryte-encoded seed.
     * @param security           The security level of private key / seed.
     * @param inputs             List of inputs used for funding the transfer.
     * @param bundle             To be populated.
     * @param tag                The tag.
     * @param totalValue         The total value.
     * @param remainderAddress   If defined, this address will be used for sending the remainder value (of the inputs) to.
     * @param signatureFragments The signature fragments.
     * @throws ArgumentException is thrown when an invalid argument is provided.
     * @throws IllegalStateException     is thrown when a transfer fails because their is not enough balance to perform the transfer.
     */
    public List<String> addRemainder(final String seed,
                                     final int security,
                                     final List<Input> inputs,
                                     final Bundle bundle,
                                     final String tag,
                                     final long totalValue,
                                     final String remainderAddress,
                                     final List<String> signatureFragments) throws ArgumentException {

        long totalTransferValue = totalValue;
        for (int i = 0; i < inputs.size(); i++) {
            long thisBalance = inputs.get(i).getBalance();
            long toSubtract = 0 - thisBalance;
            long timestamp = (long) Math.floor(Calendar.getInstance().getTimeInMillis() / 1000);

            // Add input as bundle entry
            bundle.addEntry(security, inputs.get(i).getAddress(), toSubtract, tag, timestamp);
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
                    return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments, customCurl.clone());
                } else if (remainder > 0) {
                    // Generate a new Address by calling getNewAddress

                    GetNewAddressResponse res = getNewAddress(seed, security, 0, false, 0, false);
                    // Remainder bundle entry
                    bundle.addEntry(1, res.getAddresses().get(0), remainder, tag, timestamp);

                    // Final function for signing inputs
                    return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments, customCurl.clone());
                } else {
                    // If there is no remainder, do not add transaction to bundle
                    // simply sign and return
                    return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments, customCurl.clone());
                }

                // If multiple inputs provided, subtract the totalTransferValue by
                // the inputs balance
            } else {
                totalTransferValue -= thisBalance;
            }
        }
        throw new IllegalStateException(NOT_ENOUGH_BALANCE_ERROR);
    }

    public static class Builder extends IotaAPICore.Builder<Builder> {
        private ICurl customCurl = SpongeFactory.create(SpongeFactory.Mode.KERL);

        public Builder withCustomCurl(ICurl curl) {
            customCurl = curl;
            return this;
        }

        public IotaAPI build() {
            super.build();
            return new IotaAPI(this);
        }
    }
}
