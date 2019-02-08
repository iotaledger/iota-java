package org.iota.jota;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.iota.jota.config.options.ApiOptions;
import org.iota.jota.dto.response.BroadcastTransactionsResponse;
import org.iota.jota.dto.response.CheckConsistencyResponse;
import org.iota.jota.dto.response.FindTransactionResponse;
import org.iota.jota.dto.response.GetAccountDataResponse;
import org.iota.jota.dto.response.GetAttachToTangleResponse;
import org.iota.jota.dto.response.GetBalancesAndFormatResponse;
import org.iota.jota.dto.response.GetBalancesResponse;
import org.iota.jota.dto.response.GetBundleResponse;
import org.iota.jota.dto.response.GetInclusionStateResponse;
import org.iota.jota.dto.response.GetNewAddressResponse;
import org.iota.jota.dto.response.GetNodeInfoResponse;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * IotaAPI Builder. Usage:
 * <p>
 * {@code IotaAPI api = new IotaAPI.Builder()}
 * {@code .protocol("http")}
 * {@code .nodeAddress("localhost")}
 * {@code .port(12345)}
 * {@code .build();}
 *
 * {@code GetNodeInfoResponse response = api.getNodeInfo();}
 *
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
     * <p>
     * Generates a new address from a seed and returns the remainderAddress.
     * This is either done deterministically, or by providing the index of the new remainderAddress.
     * </p>
     * Deprecated - Use the new functions {@link #getNextAvailableAddress}, {@link #getAddressesUnchecked} and {@link #generateNewAddresses}
     * 
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param index     Key index to start search from. If the index is provided, the generation of the address is not deterministic.
     * @param checksum  Adds 9-tryte address checksum.
     * @param total     Total number of addresses to generate.
     * @param returnAll If <code>true</code>, it returns all addresses which were deterministically generated (until findTransactions returns null).
     * @return {@link GetNewAddressResponse}
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    @Deprecated
    public GetNewAddressResponse getNewAddress(final String seed, int security, int index, boolean checksum, int total, boolean returnAll) throws ArgumentException {

        // If total number of addresses to generate is supplied, simply generate
        // and return the list of all addresses
        if (total != 0) {
            return getAddressesUnchecked(seed, security, checksum, index, total);
        }

        // If !returnAll return only the last address that was generated
        return generateNewAddresses(seed, security, checksum, index, 1, returnAll);
    }
    
    /**
     * Checks all addresses until the first unspent address is found. Starts at index 0.
     * 
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param checksum  Adds 9-tryte address checksum.
     * @return {@link GetNewAddressResponse}
     * @throws ArgumentException When the seed is invalid
     * @throws ArgumentException When the security level is wrong.
     */
    public GetNewAddressResponse getNextAvailableAddress(String seed, int security, boolean checksum) throws ArgumentException {
        return generateNewAddresses(seed, security, checksum, 0, 1, false);
    }
    
    /**
     * Checks all addresses until the first unspent address is found.
     * 
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param checksum  Adds 9-tryte address checksum.
     * @param index     Key index to start search from.
     * @return {@link GetNewAddressResponse}
     * @throws ArgumentException When the seed is invalid
     * @throws ArgumentException When the security level is wrong.
     */
    public GetNewAddressResponse getNextAvailableAddress(String seed, int security, boolean checksum, int index) throws ArgumentException {
        return generateNewAddresses(seed, security, checksum, index, 1, false);
    }
    
    /**
     * Generates new addresses, meaning addresses which were not spend from, according to the connected node.
     * Starts at index 0, untill <code>amount</code> of unspent addresses are found.
     * 
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param checksum  Adds 9-tryte address checksum.
     * @param amount    Total number of addresses to generate.
     * @return {@link GetNewAddressResponse}
     * @throws ArgumentException When the seed is invalid
     * @throws ArgumentException When the security level is wrong.
     * @throws ArgumentException When the amount is negative
     */
    public GetNewAddressResponse generateNewAddresses(String seed, int security, boolean checksum, int amount) throws ArgumentException {
        return generateNewAddresses(seed, security, checksum, 0, amount, false);
    }
    
    /**
     * Generates new addresses, meaning addresses which were not spend from, according to the connected node.
     * Stops when <code>amount</code> of unspent addresses are found,starting from <code>index</code>
     * 
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param checksum  Adds 9-tryte address checksum.
     * @param index     Key index to start search from.
     * @param amount    Total number of addresses to generate.
     * @return {@link GetNewAddressResponse}
     * @throws ArgumentException When the seed is invalid
     * @throws ArgumentException When the security level is wrong.
     * @throws ArgumentException When index plus the amount are below 0
     */
    public GetNewAddressResponse generateNewAddresses(String seed, int security, boolean checksum, int index, int amount) throws ArgumentException {
        return generateNewAddresses(seed, security, checksum, 0, amount, false);
    }
    
    /**
     * Generates new addresses, meaning addresses which were not spend from, according to the connected node.
     * Stops when <code>amount</code> of unspent addresses are found,starting from <code>index</code>
     * 
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param checksum  Adds 9-tryte address checksum.
     * @param index     Key index to start search from.
     * @param amount    Total number of addresses to generate.
     *                  If this is set to 0, we will generate until the first unspent address is found, and stop.
     *                  If amount is negative, we count back from index.
     * @param addSpendAddresses If <code>true</code>, it returns all addresses, even those who were determined to be spent from
     * @return {@link GetNewAddressResponse}
     * @throws ArgumentException When the seed is invalid
     * @throws ArgumentException When the security level is wrong.
     * @throws ArgumentException When index plus the amount are below 0
     */
    public GetNewAddressResponse generateNewAddresses(String seed, int security, boolean checksum, int index, int amount, boolean addSpendAddresses) throws ArgumentException {
        if ((!InputValidator.isValidSeed(seed))) {
            throw new IllegalStateException(Constants.INVALID_SEED_INPUT_ERROR);
        }
        
        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }
        
        if (index + amount < 0) {
            throw new ArgumentException(Constants.INVALID_INPUT_ERROR);
        }
        
        StopWatch stopWatch = new StopWatch();
        List<String> allAddresses = new ArrayList<>();

        for (int i = index, numUnspentFound=0; numUnspentFound < amount; i++) {

            final String newAddress = IotaAPIUtils.newAddress(seed, security, i, checksum, getCurl());
            final FindTransactionResponse response = findTransactionsByAddresses(newAddress);

            
            if (response.getHashes().length == 0) {
                //Unspent address, if we ask for 0, we dont need to add it
                if (amount != 0) {
                    allAddresses.add(newAddress);
                }
                
                numUnspentFound++;
            } else if (addSpendAddresses) {
                //Spend address, were interested anyways
                allAddresses.add(newAddress);
            }
        }
        
        return GetNewAddressResponse.create(allAddresses, stopWatch.getElapsedTimeMili());
    }
    
    /**
     * Generates <code>amount</code> of addresses, starting from <code>index</code>
     * This does not mean that these addresses are safe to use (unspent)
     * 
     * @param seed      Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security  Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param checksum  Adds 9-tryte address checksum.
     * @param index     Key index to start search from. The generation of the address is not deterministic.
     * @param amount    Total number of addresses to generate.
     * @return {@link GetNewAddressResponse}
     * @throws ArgumentException is thrown when the specified input is not valid.
     */
    public GetNewAddressResponse getAddressesUnchecked(String seed, int security, boolean checksum, int index, int amount) throws ArgumentException {
        if ((!InputValidator.isValidSeed(seed))) {
            throw new IllegalStateException(Constants.INVALID_SEED_INPUT_ERROR);
        }
        
        StopWatch stopWatch = new StopWatch();

        List<String> allAddresses = new ArrayList<>();
        for (int i = index; i < index + amount; i++) {
            allAddresses.add(IotaAPIUtils.newAddress(seed, security, i, checksum, getCurl()));
        }
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
     * @throws ArgumentException when <tt>start</tt> and <tt>end</tt> are more then 500 apart
     * @throws ArgumentException Invalid security index
     * @throws IllegalStateException When the seed is invalid
     */
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

        GetNewAddressResponse gnr = getNewAddress(seed, security, start, false, end, true);
        if (gnr != null && gnr.getAddresses() != null) {
            Bundle[] bundles = bundlesFromAddresses(inclusionStates, gnr.getAddresses().toArray(new String[gnr.getAddresses().size()]));
            return GetTransferResponse.create(bundles, stopWatch.getElapsedTimeMili());
        }
        return GetTransferResponse.create(new Bundle[]{}, stopWatch.getElapsedTimeMili());
    }

    /**
     * Internal function to get the formatted bundles of a list of addresses.
     *
     * @param addresses       Array of addresses.
     * @param inclusionStates If <code>true</code>, it also gets the inclusion state of each bundle.
     * @return All the transaction bundles for the addresses
     * @throws ArgumentException When the addresses are invalid
     * @throws IllegalStateException When inclusion state/confirmed could not be determined (<tt>null</tt> returned)
     */
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
                throw new IllegalStateException(Constants.GET_INCLUSION_STATE_RESPONSE_ERROR);
            }
        }
        final GetInclusionStateResponse finalInclusionStates = gisr;
        Parallel.of(Arrays.asList(tailTxArray),
                new Parallel.Operation<String>() {
                    public void perform(String tailTx) {

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
                            log.warn(Constants.GET_BUNDLE_RESPONSE_ERROR);
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
     * Wrapper method: stores and broadcasts the specified trytes.
     *
     * @param trytes The trytes.
     * @return {@link BroadcastTransactionsResponse}
     * @throws ArgumentException is thrown when the specified <tt>trytes</tt> is not valid.
     * @throws ArgumentException If {@link #storeTransactions(String...)} fails
     * @see #storeTransactions(String...)
     * @see #broadcastTransactions(String...)
     */
    public BroadcastTransactionsResponse storeAndBroadcast(String... trytes) throws ArgumentException {

        if (!InputValidator.isArrayOfAttachedTrytes(trytes)) {
            throw new ArgumentException(Constants.INVALID_TRYTES_INPUT_ERROR);
        }

        try {
            System.out.println("storeTransactions");
            storeTransactions(trytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ArgumentException(e.toString());
        }
        System.out.println("broadcastTransactions");
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
    public List<Transaction> sendTrytes(String[] trytes, int depth, int minWeightMagnitude, String reference) throws ArgumentException {
        System.out.println("gtta");
        GetTransactionsToApproveResponse txs = getTransactionsToApprove(depth, reference);

        // attach to tangle - do pow
        System.out.println("attachToTangle " + minWeightMagnitude);
        GetAttachToTangleResponse res = attachToTangle(txs.getTrunkTransaction(), txs.getBranchTransaction(), minWeightMagnitude, trytes);

        try {
            System.out.println("storeAndBroadcast");
            storeAndBroadcast(res.getTrytes());
        } catch (ArgumentException e) {
            return new ArrayList<>();
        }

        final List<Transaction> trx = new ArrayList<>();

        for (String tryte : res.getTrytes()) {
            trx.add(new Transaction(tryte, getCurl()));
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
    public List<Transaction> findTransactionsObjectsByHashes(String... hashes) throws ArgumentException {

        if (!InputValidator.isArrayOfHashes(hashes)) {
            throw new IllegalStateException(Constants.INVALID_HASHES_INPUT_ERROR);
        }

        final GetTrytesResponse trytesResponse = getTrytes(hashes);

        final List<Transaction> trxs = new ArrayList<>();

        for (final String tryte : trytesResponse.getTrytes()) {
            trxs.add(new Transaction(tryte, getCurl()));
        }
        return trxs;
    }

    /**
     * Wrapper function: Finds transactions, gets trytes and turns it into {@link Transaction} objects.
     *
     * @param addresses The addresses we should get the transactions for
     * @return {@link Transaction} objects.
     * @throws ArgumentException if addresses is not a valid array of hashes
     * @see #findTransactionsByAddresses(String...)
     * @see #findTransactionsObjectsByHashes
     **/
    public List<Transaction> findTransactionObjectsByAddresses(String... addresses) throws ArgumentException {
        List<String> addressesWithoutChecksum = new ArrayList<>();

        for (String address : addresses) {
            String addressO = Checksum.removeChecksum(address);
            addressesWithoutChecksum.add(addressO);
        }

        FindTransactionResponse ftr = findTransactionsByAddresses(addressesWithoutChecksum.toArray(new String[]{}));
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
     * @return Transactions.
     **/
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
     * @throws ArgumentException If the seed is invalid
     * @throws ArgumentException If the security level is wrong.
     * @throws IllegalStateException If the transfers are not all valid
     * @throws IllegalStateException If there is not enough balance in the inputs
     */
    public List<String> prepareTransfers(String seed, int security, 
                                         List<Transfer> transfers, 
                                         String remainder, 
                                         List<Input> inputs, 
                                         List<Transaction> tips, 
                                         boolean validateInputs) throws ArgumentException {

        // validate seed
        if ((!InputValidator.isValidSeed(seed))) {
            throw new IllegalStateException(Constants.INVALID_SEED_INPUT_ERROR);
        }

        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }

        // Input validation of transfers object
        if (!InputValidator.isTransfersCollectionValid(transfers)) {
            throw new ArgumentException(Constants.INVALID_TRANSFERS_INPUT_ERROR);
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

                List<String> tipHashes = null;
                if (tips != null) {
                    tipHashes = new ArrayList<>();
                
                    for (final Transaction tx: tips) {
                        tipHashes.add(tx.getHash());
                    }
                }

                GetBalancesResponse balancesResponse = getBalances(100, inputsAddresses, tipHashes);
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
                
                // If inputs with enough balance
                return addRemainder(seed, security, newinputs.getInputs(), bundle, tag, totalValue, remainder, signatureFragments);
            }
        } else {

            // If no input required, don't sign and simply finalize the bundle
            bundle.finalize(getCurl());
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
     * @param seed            Tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security        Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param start           Starting key index, must be at least 0.
     * @param end             Ending key index, must be bigger then <tt>start</tt>
     * @param threshold       Minimum balance required.
     * @param tips            The starting points we walk back from to find the balance of the addresses, can be <tt>null</tt>
     * @return {@link GetBalancesAndFormatResponse}
     * @throws ArgumentException If the seed is invalid
     * @throws ArgumentException If the security level is wrong.
     * @throws ArgumentException when <tt>start</tt> and <tt>end</tt> are more then 500 apart
     * @see #getBalanceAndFormat(List, List, long, int, StopWatch, int)
     **/
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

                String address = IotaAPIUtils.newAddress(seed, security, i, false, getCurl());
                allAddresses.add(address);
            }

            return getBalanceAndFormat(allAddresses, tipsList, threshold, start, stopWatch, security);
        }
        //  Case 2: iterate till threshold || end
        //
        //  Either start from index: 0 or start (if defined) until threshold is reached.
        //  Calls getNewAddress and deterministically generates and returns all addresses
        //  We then do getBalance, format the output and return it
        else {
            final GetNewAddressResponse res =  generateNewAddresses(seed, security, false, start, 0, true);
            return getBalanceAndFormat(res.getAddresses(), tipsList, threshold, start, stopWatch, security);
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
     * @param addresse The addresses where we will find the balance for.
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException The the request was considered wrong in any way by the node
     */
    public long getBalance(int threshold, String address) throws ArgumentException {
        GetBalancesResponse response = getBalances(threshold, new String[] { address }, null);
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
     * @throws ArgumentException is thrown when the specified security level is not valid.
     **/
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

        GetBalancesResponse getBalancesResponse = getBalances(100, addresses, tips);
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
    public GetBundleResponse getBundle(String transaction) throws ArgumentException {

        if (!InputValidator.isHash(transaction)) {
            throw new ArgumentException(Constants.INVALID_HASHES_INPUT_ERROR);
        }
        
        StopWatch stopWatch = new StopWatch();
        
        Bundle bundle = traverseBundle(transaction, null, new Bundle());
        if (bundle == null) {
            throw new ArgumentException(Constants.INVALID_BUNDLE_ERROR);
        }

        if (!BundleValidator.isBundle(bundle)){
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
     * @param checksum        Adds 9-tryte address checksum.
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
    public GetAccountDataResponse getAccountData(String seed, int security, int index, boolean checksum, int total, boolean returnAll, int start, int end, boolean inclusionStates, long threshold) throws ArgumentException {
        if (!InputValidator.isValidSecurityLevel(security)) {
            throw new ArgumentException(Constants.INVALID_SECURITY_LEVEL_INPUT_ERROR);
        }
        
        if (start < 0 || start > end || end > (start + 1000)) {
            throw new ArgumentException(Constants.INVALID_INPUT_ERROR);
        }

        StopWatch stopWatch = new StopWatch();

        GetNewAddressResponse gna = getNewAddress(seed, security, index, checksum, total, returnAll);
        GetTransferResponse gtr = getTransfers(seed, security, start, end, inclusionStates);
        GetBalancesAndFormatResponse gbr = getInputs(seed, security, start, end, threshold);

        return GetAccountDataResponse.create(gna.getAddresses(), gtr.getTransfers(), gbr.getInputs(), gbr.getTotalBalance(), stopWatch.getElapsedTimeMili());
    }
    
    /**
     * Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.
     * If the address has a checksum, it is automatically removed
     * 
     * @param addresses the addresses to check
     * @return list of address boolean checks
     * @throws ArgumentException when an address is invalid
     */
    public boolean[] checkWereAddressSpentFrom(String... addresses) throws ArgumentException {
        List<String> rawAddresses=new ArrayList<>();
        for(String address: addresses) {
            String rawAddress=null;
            try {
                if (Checksum.isAddressWithChecksum(address)) {
                    rawAddress=Checksum.removeChecksum(address);
                }
            } catch (ArgumentException e) {}
            if(rawAddress==null) {
                rawAddresses.add(address);
            } else {
                rawAddresses.add(rawAddress);
            }
        }
        String[] spentAddresses = new String[rawAddresses.size()];
        spentAddresses = rawAddresses.toArray(spentAddresses);
        WereAddressesSpentFromResponse response = wereAddressesSpentFrom(spentAddresses);
        return response.getStates();

    }
    
    /**
     * Check if an addresses was ever spent from, in the current epoch, or in previous epochs.
     * If the address has a checksum, it is removed
     * 
     * @param address the address to check
     * @return <tt>true</tt> if it was spent, otherwise <tt>false</tt>
     * @throws ArgumentException when the address is invalid
     */
    public Boolean checkWereAddressSpentFrom(String address) throws ArgumentException {
        String[] spentAddresses =new String[] {address};
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
     * @param bundle              The bundle we wish to replay on the network
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
    public ReplayBundleResponse replayBundle(Bundle bundle, int depth, int minWeightMagnitude, 
            String reference) throws ArgumentException {
        
        return this.replayBundle(bundle, depth, minWeightMagnitude, reference, new StopWatch());
    }
    
    /**
     * Replays a transfer by doing Proof of Work again.
     * This will make a new, but identical transaction which now also can be approved.
     * If any of the replayed transactions gets approved, the others stop getting approved.
     *
     * @param bundle              The bundle we wish to replay on the network
     * @param depth               The depth for getting transactions to approve
     * @param minWeightMagnitude  The minimum weight magnitude for doing proof of work
     * @param reference           Hash of transaction to start random-walk from.
     *                            This is used to make sure the tips returned reference a given transaction in their past.
     *                            Can be <tt>null</tt>, in that case the latest milestone is used as a reference.
     * @param stopWatch           The stopwatch used for recording this response time.                           
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
        List<Transaction> trxs = sendTrytes(bundleTrytes.toArray(new String[bundleTrytes.size()]), depth, minWeightMagnitude, reference);

        Boolean[] successful = new Boolean[trxs.size()];

        for (int i = 0; i < trxs.size(); i++) {

            final FindTransactionResponse response = findTransactionsByBundles(trxs.get(i).getBundle());


            successful[i] = response.getHashes().length != 0;
        }
        
        return ReplayBundleResponse.create(new Bundle(trxs), successful, stopWatch.getElapsedTimeMili());
    }

    /**
     * Wrapper function: runs getNodeInfo and getInclusionStates
     * Uses the latest milestone as tip
     * 
     * @param hashes The hashes.
     * @return {@link GetInclusionStateResponse}
     * @throws ArgumentException when one of the hashes is invalid
     * @see #getNodeInfo()
     * @see #getInclusionStates(String[], String[])
     */
    public GetInclusionStateResponse getLatestInclusion(String... hashes) throws ArgumentException {
        GetNodeInfoResponse getNodeInfoResponse = getNodeInfo();

        String[] latestMilestone = {getNodeInfoResponse.getLatestSolidSubtangleMilestone()};

        return getInclusionStates(hashes, latestMilestone);
    }

    /**
     * Wrapper function: Runs prepareTransfers, as well as attachToTangle.
     * We then broadcasts this and and store the transactions on the node.
     *
     * @param seed               The tryte-encoded seed. It should be noted that this seed is not transferred.
     * @param security           Security level to be used for the private key / address. Can be 1, 2 or 3.
     * @param depth              The depth for getting transactions to approve
     * @param minWeightMagnitude The minimum weight magnitude for doing proof of work
     * @param transfers          List of {@link Transfer} objects.
     * @param inputs             List of {@link Input} used for funding the transfer.
     * @param remainderAddress   If defined, this remainderAddress will be used for sending the remainder value (of the inputs) to.
     *                           When this is not defined, but a remaining exists, the next free address is used.
     * @param validateInputs     Whether or not to validate the balances of the provided inputs.
     * @param validateInputAddresses  Whether or not to validate if the destination address is already use. 
     *                                If a key reuse is detect or it's send to inputs.
     * @param tips               The starting points we walk back from to find the balance of the addresses
     *                           If multiple tips are supplied, only the first tip is used for {@link #getTransactionsToApprove(Integer, String)}
     * @return {@link SendTransferResponse}
     * @throws ArgumentException If the seed is invalid
     * @throws ArgumentException If the security level is wrong.
     * @throws ArgumentException When <tt>validateInputAddresses</tt> is <tt>true</tt>, if validateTransfersAddresses has an error.
     * @throws IllegalStateException If the transfers are not all valid
     * @throws IllegalStateException If there is not enough balance in the inputs to supply to the transfers
     * @see #prepareTransfers(String, int, List, String, List, List, boolean)
     * @see #sendTrytes(String[], int, int, String)
     * @see #validateTransfersAddresses(String, int, List)
     */
    public SendTransferResponse sendTransfer(String seed, int security, int depth, int minWeightMagnitude, 
                                             List<Transfer> transfers, List<Input> inputs, String remainderAddress, 
                                             boolean validateInputs, boolean validateInputAddresses, 
                                             List<Transaction> tips) throws ArgumentException {

        StopWatch stopWatch = new StopWatch();

        System.out.println("prepare");
        List<String> trytes = prepareTransfers(seed, security, transfers, remainderAddress, inputs, tips, validateInputs);
        
        if (validateInputAddresses) {
            validateTransfersAddresses(seed, security, trytes);
        }

        String reference = tips != null && tips.size() > 0 ? tips.get(0).getHash(): null;

        System.out.println("send");
        List<Transaction> trxs = sendTrytes(trytes.toArray(new String[trytes.size()]), depth, minWeightMagnitude, reference);

        Boolean[] successful = new Boolean[trxs.size()];
        
        for (int i = 0; i < trxs.size(); i++) {
            System.out.println("findTransactionsByBundles");
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
    public Bundle traverseBundle(String trunkTx, String bundleHash, Bundle bundle) throws ArgumentException {
        GetTrytesResponse gtr = getTrytes(trunkTx);
        
        if (gtr != null) {

            if (gtr.getTrytes().length == 0) {
                throw new ArgumentException(Constants.INVALID_BUNDLE_ERROR);
            }

            Transaction trx = new Transaction(gtr.getTrytes()[0], getCurl());
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
     * @throws ArgumentException when an address is invalid.
     * @throws ArgumentException when the security level is wrong.
     * @throws IllegalStateException when a transfer fails because their is not enough balance to perform the transfer.
     * @throws IllegalStateException When a <tt>remainderAddress</tt> is required, but not supplied
     * @throws RuntimeException When the total value from the transfers is not 0
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
     * @throws ArgumentException when an address is invalid.
     * @throws ArgumentException when the security level is wrong.
     * @throws IllegalStateException when a transfer fails because their is not enough balance to perform the transfer.
     * @throws IllegalStateException When a <tt>remainderAddress</tt> is required, but not supplied
     * @throws RuntimeException When the total value from the transfers is not 0
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
     * @throws ArgumentException when an address is invalid.
     * @throws ArgumentException when the security level is wrong.
     * @throws IllegalStateException when a transfer fails because their is not enough balance to perform the transfer.
     * @throws IllegalStateException When a <tt>remainderAddress</tt> is required, but not supplied
     * @throws RuntimeException When the total value from the transfers is not 0
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
     * @param inputAddress     Array of input addresses as well as the securitySum.
     * @param remainderAddress Has to be generated by the cosigners before initiating the transfer, can be null if fully spent.
     * @param transfers        List of {@link Transfer} we want to make using the unputAddresses
     * @param tips             The starting points for checking if the balances of the input addresses contain enough to make this transfer.
     *                         This can be <tt>null</tt>
     * @param testMode         If were running unit tests, set to true to bypass total value check
     * @return All the {@link Transaction} objects in this newly created transfer
     * @throws ArgumentException when an address is invalid.
     * @throws ArgumentException when the security level is wrong.
     * @throws IllegalStateException when a transfer fails because their is not enough balance to perform the transfer.
     * @throws IllegalStateException When a <tt>remainderAddress</tt> is required, but not supplied
     * @throws RuntimeException When the total value from the transfers is not 0
     */
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

            List<String> tipHashes = null;
            if (tips != null) {
                tipHashes = new ArrayList<>(tips.size());
                for(final Transaction tx: tips) {
                    tipHashes.add(tx.getHash());
                }
            }
            

            GetBalancesResponse balancesResponse = getBalances(100, Collections.singletonList(inputAddress), tipHashes);
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
                bundle.addEntry(securitySum, inputAddress, toSubtract, tag, timestamp);
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

            bundle.finalize(SpongeFactory.create(SpongeFactory.Mode.CURLP81));
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
     * Transactions for these addresses will be looked up, making this an expensive method call.
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
    public void validateTransfersAddresses(String seed, int security, List<String> trytes) throws ArgumentException {

        HashSet<String> addresses = new HashSet<>();
        List<Transaction> inputTransactions = new ArrayList<>();
        List<String> inputAddresses = new ArrayList<>();

        for (String trx : trytes) {
            Transaction transaction = new Transaction(trx, getCurl());
            addresses.add(transaction.getAddress());
            inputTransactions.add(transaction);
        }

        String[] hashes = findTransactionsByAddresses(addresses.toArray(new String[addresses.size()])).getHashes();
        List<Transaction> transactions = findTransactionsObjectsByHashes(hashes);
        GetNewAddressResponse gna = generateNewAddresses(seed, security, false, 0, 0, false);
        GetBalancesAndFormatResponse gbr = getInputs(seed, security, 0, 0, 0);

        for (Input input : gbr.getInputs()) {
            inputAddresses.add(input.getAddress());
        }

        //check if send to input
        for (Transaction trx : inputTransactions) {
            if (trx.getValue() > 0 && inputAddresses.contains(trx.getAddress())) {
                throw new ArgumentException(Constants.SEND_TO_INPUTS_ERROR);
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
     *                           If this is <tt>null</tt>, {@link #getNextAvailableAddress(String, int, boolean)} is used.
     * @param signatureFragments The signature fragments (message), used for signing. 
     *                           Should be 2187 characters long, can be padded with 9s.
     * @return A list of signed inputs to be used in a transaction 
     * @throws ArgumentException When the seed is invalid
     * @throws ArgumentException When the security level is wrong.
     * @throws IllegalStateException When the inputs do not contain enough balance to reach <tt>totalValue</tt>.
     * @see IotaAPIUtils#signInputsAndReturn
     * @see #getNextAvailableAddress(String, int, boolean)
     */
    public List<String> addRemainder(String seed, int security, List<Input> inputs, Bundle bundle, 
                                     String tag, long totalValue, String remainderAddress,
                                     List<String> signatureFragments) throws ArgumentException {
        //TODO: replace 2187 with {@value Constants#MESSAGE_LENGTH}. 
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=490247
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
                    return IotaAPIUtils.signInputsAndReturn(seed, inputs, bundle, signatureFragments, getCurl());
                } else if (remainder > 0) {
                    // Generate a new Address by calling getNewAddress

                    GetNewAddressResponse res = getNextAvailableAddress(seed, security, false);
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
     * @throws ArgumentException when we can't get the consistency of this transaction
     * @see #checkConsistency(String...)
     */
    public boolean isPromotable(Transaction tail) throws ArgumentException {
        long lowerBound = tail.getAttachmentTimestamp();
        CheckConsistencyResponse consistencyResponse = checkConsistency(tail.getHash());
        
        return consistencyResponse.getState() && isAboveMaxDepth(lowerBound);
    }
    
    /**
     * Checks if a transaction hash is promotable
     * 
     * @param tail the {@link Transaction} hash we want to check
     * @return <tt>true</tt> if it is, otherwise <tt>false</tt>
     * @throws ArgumentException when we can't get the consistency of this transaction
     * or when the transaction is not found
     * @see #checkConsistency(String...)
     */
    public boolean isPromotable(String tail) throws ArgumentException {
        GetTrytesResponse transaction = getTrytes(tail);
        if (0 == transaction.getTrytes().length) {
            throw new ArgumentException(Constants.TRANSACTION_NOT_FOUND);
        }
        
        return isPromotable(new Transaction(transaction.getTrytes()[0]));
    }
    
    private boolean isAboveMaxDepth (long attachmentTimestamp) {
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
     * @param tail bundle tail to promote, cannot be <tt>null</tt>
     * @param depth depth for getTransactionsToApprove
     * @param minWeightMagnitude minWeightMagnitude to use for Proof-of-Work
     * @param bundle the {@link Bundle} to attach for promotion
     * @return List of the bundle {@link Transaction}s made with the attached transaction trytes
     * @throws ArgumentException When the bundle has no transaction
     * @throws ArgumentException When <tt>depth</tt> or <tt>minWeightMagnitude</tt> is lower than 0
     * @throws ArgumentException When the <tt>tail</tt> hash is invalid
     * @throws NotPromotableException When the transaction is not promotable
     * @see #checkConsistency(String...)
     * @see #getTransactionsToApprove(Integer, String)
     * @see #attachToTangle(String, String, Integer, String...)
     * @see #storeAndBroadcast(String...)
     */
    public List<Transaction> promoteTransaction(String tail, int depth, int minWeightMagnitude, Bundle bundle) throws BaseException {
        if (bundle == null || bundle.getTransactions().size() == 0) {
            throw new ArgumentException("Need at least one transaction in the bundle");
        }

        if(depth < 0) {
            throw new ArgumentException("Depth must be >= 0");
        }

        if(minWeightMagnitude <= 0) {
            throw new ArgumentException("MinWeightMagnitude must be > 0");
        }

        CheckConsistencyResponse consistencyResponse = checkConsistency(tail);

        if (!consistencyResponse.getState()) {
            throw new NotPromotableException(consistencyResponse.getInfo());
        }

        GetTransactionsToApproveResponse transactionsToApprove = getTransactionsToApprove(depth, tail);

        final GetAttachToTangleResponse res = attachToTangle(transactionsToApprove.getTrunkTransaction(), transactionsToApprove.getBranchTransaction(), minWeightMagnitude,
                bundle.getTransactions().stream().map(tx -> tx.toTrytes()).toArray(String[]::new));

        try {
            storeAndBroadcast(res.getTrytes());
        } catch (ArgumentException e) {
            return Collections.emptyList();
        }

        return Arrays.stream(res.getTrytes()).map(trytes -> new Transaction(trytes, getCurl())).collect(Collectors.toList());
    }
    
    public static class Builder extends IotaAPICore.Builder<IotaAPI.Builder, IotaAPI> {
        
        @Override
        protected IotaAPI compile() {
            return new IotaAPI(this);
        }
    }
}
