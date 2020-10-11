package org.iota.jota;

import static org.iota.jota.utils.Constants.INVALID_ADDRESSES_INPUT_ERROR;
import static org.iota.jota.utils.Constants.INVALID_APPROVE_DEPTH_ERROR;
import static org.iota.jota.utils.Constants.INVALID_ATTACHED_TRYTES_INPUT_ERROR;
import static org.iota.jota.utils.Constants.INVALID_BUNDLE_HASH_ERROR;
import static org.iota.jota.utils.Constants.INVALID_HASHES_INPUT_ERROR;
import static org.iota.jota.utils.Constants.INVALID_TAG_INPUT_ERROR;
import static org.iota.jota.utils.Constants.INVALID_THRESHOLD_ERROR;
import static org.iota.jota.utils.Constants.INVALID_TRYTES_INPUT_ERROR;

import org.apache.commons.lang3.ArrayUtils;
import org.iota.jota.connection.Connection;
import org.iota.jota.dto.request.*;
import org.iota.jota.dto.response.*;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Transaction;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.InputValidator;
import org.iota.mddoclet.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This class provides access to the Iota core API
 * Handles direct methods with the connected node(s), and does basic verification
 */
public class IotaAPICore {
    private static final Logger log = LoggerFactory.getLogger(IotaAPICore.class);

    private ApiOptions options;

    protected final List<Connection> nodes = new ArrayList<>();

    protected IotaAPICore(ApiOptions options) {
        this.options = options;

        for (Connection c : options.getNodes()) {
            addNode(c);
        }
    }

    public boolean hasNodes() {
        return !nodes.isEmpty();
    }

    public Connection getRandomNode() {
        if (!hasNodes()) {
            return null;
        }

        int randomNodeIndex = new Random().nextInt(nodes.size());
        return nodes.get(randomNodeIndex);
    }

    public List<Connection> getNodes() {
        return nodes;
    }

    ApiOptions getOptions() {
        return options;
    }

    public boolean addNode(Connection n) {
        try {
            synchronized (nodes) {
                for (Connection c : nodes) {
                    if (c.equals(n)) {
                        log.warn("Tried to add a node we allready have: " + n);
                        return true;
                    }
                }

                boolean started = n.start();
                if (started) {
                    nodes.add(n);
                    log.debug("Added node: " + n.toString());
                }

                return started;
            }
        } catch (Exception e) {
            log.warn("Failed to add node connection to pool due to \"" + e.getMessage() + "\"");
            return false;
        }
    }

    public boolean removeNode(Connection n) {
        synchronized (nodes) {
            for (int i = 0; i < nodes.size(); i++) {
                Connection c = nodes.get(i);
                if (c.equals(n)) {
                    c.stop();
                    nodes.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gives a clone of the custom curl defined in {@link ApiOptions}
     *
     * @return A clone of our curl object
     */
    public ICurl getCurl() {
        return getOptions().getCustomCurl().clone();
    }

    public void setCurl(ICurl localPoW) {
        getOptions().setCustomCurl(localPoW);
    }

    public IotaPoW getLocalPoW() {
        return getOptions().getLocalPoW();
    }

    public void setLocalPoW(IotaPoW localPoW) {
        getOptions().setLocalPoW(localPoW);
    }

    /**
     * Finds the proper connection for a request
     *
     * @param cmd The request we are going to make on the node
     *            If this is <code>null</code>, the primary node will be returned (first node)
     * @return A node connection
     */
    private Connection getNodeFor(IotaAPICommand cmd) {
        if (null == cmd) {
            return nodes.get(0);
        }

        //TODO switch based on cmd
        return nodes.get(0);
    }

    /**
     * Returns information about this node.
     *
     * @return {@link GetNodeInfoResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     */
    @Document
    public GetNodeInfoResponse getNodeInfo() throws ArgumentException {
        return getNodeFor(IotaAPICommand.GET_NODE_INFO).getNodeInfo(IotaCommandRequest.createNodeInfoRequest());
    }

    /**
     * Returns information about this node.
     *
     * @return {@link GetNodeInfoResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     */
    @Document
    public GetNodeAPIConfigurationResponse getNodeAPIConfiguration() throws ArgumentException {
        return getNodeFor(IotaAPICommand.GET_NODE_API_CONFIGURATION).getNodeAPIConfiguration(IotaCommandRequest.createGetNodeAPIConfiguration());
    }

    /**
     * Returns the set of neighbors you are connected with, as well as their activity statistics (or counters).
     * The activity counters are reset after restarting IRI.
     *
     * @return {@link GetNeighborsResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     */
    @Document
    public GetNeighborsResponse getNeighbors() throws ArgumentException {
        return getNodeFor(IotaAPICommand.GET_NEIGHBORS).getNeighbors(IotaCommandRequest.createGetNeighborsRequest());
    }

    /**
     * Temporarily add a list of neighbors to your node.
     * The added neighbors will not be available after restart.
     * Add the neighbors to your config file
     * or supply them in the <tt>-n</tt> command line option if you want to add them permanently.
     * <p>
     * The URI (Unique Resource Identification) for adding neighbors is:
     * <b>tcp://IPADDRESS:PORT</b>
     *
     * @param uris list of neighbors to add
     * @return {@link AddNeighborsResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException When an URI is invalid
     */
    @Document
    public AddNeighborsResponse addNeighbors(String... uris) throws ArgumentException {
        return getNodeFor(IotaAPICommand.ADD_NEIGHBORS).addNeighbors(IotaNeighborsRequest.createAddNeighborsRequest(uris));
    }

    /**
     * Temporarily removes a list of neighbors from your node.
     * The added neighbors will be added again after relaunching IRI.
     * Remove the neighbors from your config file or make sure you don't supply them in the -n command line option if you want to keep them removed after restart.
     * <p>
     * The URI (Unique Resource Identification) for removing neighbors is:
     * <b>tcp://IPADDRESS:PORT</b>
     *
     * @param uris The URIs of the neighbors we want to remove.
     * @return {@link RemoveNeighborsResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException When an URI is invalid
     */
    @Document
    public RemoveNeighborsResponse removeNeighbors(String... uris) throws ArgumentException {
        return getNodeFor(IotaAPICommand.REMOVE_NEIGHBORS).removeNeighbors(IotaNeighborsRequest.createRemoveNeighborsRequest(uris));
    }

    /**
     * Returns all tips currently known by this node.
     *
     * @return {@link GetTipsResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     */
    @Document
    public GetTipsResponse getTips() throws ArgumentException {
        return getNodeFor(IotaAPICommand.GET_TIPS).getTips(IotaCommandRequest.createGetTipsRequest());
    }


    /**
     * <p>
     * Find the transactions which match the specified input and return.
     * All input values are lists, for which a list of return values (transaction hashes), in the same order, is returned for all individual elements.
     * The input fields can either be <tt>bundles</tt>, <tt>addresses</tt>, <tt>tags</tt> or <tt>approvees</tt>.
     * </p>
     * <p>
     * Using multiple of these input fields returns the intersection of the values.
     * Can error if the node found more transactions than the max transactions send amount
     *
     * @param addresses Array of hashes from addresses, must contain checksums
     * @param tags      Array of tags
     * @param approvees Array of transaction hashes
     * @param bundles   Array of bundle hashes
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException If any of the parameters are not empty but have invalid values
     */
    @Document
    public FindTransactionResponse findTransactions(String[] addresses, String[] tags, String[] approvees, String[] bundles) throws ArgumentException {

        String[] addressesWithoutChecksum;
        if (ArrayUtils.isNotEmpty(addresses)) {
            validateAddresses(addresses);
            addressesWithoutChecksum = removeChecksumFromAddresses(addresses);
        } else {
            addressesWithoutChecksum = addresses;
        }

        if (ArrayUtils.isNotEmpty(tags)) {
            validateTags(tags);
        }

        if (ArrayUtils.isNotEmpty(bundles)) {
            validateBundles(bundles);
        }

        if (ArrayUtils.isNotEmpty(approvees)) {
            validateApprovees(approvees);
        }

        final IotaFindTransactionsRequest findTransRequest = IotaFindTransactionsRequest
                .createFindTransactionRequest()
                .byAddresses(addressesWithoutChecksum)
                .byTags(tags)
                .byApprovees(approvees)
                .byBundles(bundles);

        return getNodeFor(IotaAPICommand.FIND_TRANSACTIONS).findTransactions(findTransRequest);
    }

    /**
     * Custom API call used to call IXI modules.
     *
     * @param module The module name, or the folder of the ixi on the node
     * @param params Optional arguments for the IXI module,
     * @return {@link IotaCustomResponse}
     * @throws ArgumentException If the ixi failed processing
     */
    @Document
    public IotaCustomResponse callIxi(String module, Map<String, Object> params) throws ArgumentException {
        final IotaCustomRequest ixiRequest = IotaCustomRequest.createCustomRequest(module, params);

        return getNodeFor(IotaAPICommand.CUSTOM_IXI).customRequest(ixiRequest);
    }

    /**
     * Find the transactions by addresses with checksum
     *
     * @param addresses An array of addresses, must contain checksums
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException If one of the addresses is invalid
     */
    @Document
    public FindTransactionResponse findTransactionsByAddresses(String... addresses) throws ArgumentException {
        validateAddresses(addresses);

        return findTransactions(addresses, null, null, null);
    }

    /**
     * Find the transactions by bundles
     *
     * @param bundles An array of bundles.
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException If one of the bundle hashes is invalid
     */
    @Document
    public FindTransactionResponse findTransactionsByBundles(String... bundles) throws ArgumentException {
        return findTransactions(null, null, null, bundles);
    }

    /**
     * Find the transactions by approvees
     *
     * @param approvees An array of approvees.
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException If one of the approvee hashes is invalid
     */
    @Document
    public FindTransactionResponse findTransactionsByApprovees(String... approvees) throws ArgumentException {
        return findTransactions(null, null, approvees, null);
    }

    /**
     * Find the transactions by digests
     * Deprecated: Use {@link #findTransactionsByTags} instead
     *
     * @param digests A List of digests. Must be hashed tags (digest)
     * @return The transaction hashes which are returned depend on the input.
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException When one of the digests is invalid
     */
    @Deprecated
    public FindTransactionResponse findTransactionsByDigests(String... digests) throws ArgumentException {
        return findTransactionsByTags(digests);
    }

    /**
     * Find the transactions by tags
     *
     * @param tags A List of tags.
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException When one of the tags is invalid
     */
    @Document
    public FindTransactionResponse findTransactionsByTags(String... tags) throws ArgumentException {
        return findTransactions(null, tags, null, null);
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
     *
     * @param transactions Array of transactions you want to get the inclusion state for.
     * @return {@link GetInclusionStateResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException when a transaction hash is invalid
     */
    @Document
    public GetInclusionStateResponse getInclusionStates(String... transactions) throws ArgumentException {

        if (!InputValidator.isArrayOfHashes(transactions)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        return getNodeFor(IotaAPICommand.GET_INCLUSIONS_STATES).getInclusionStates(IotaGetInclusionStateRequest
                .createGetInclusionStateRequest(transactions));
    }

    /**
     * Returns the raw transaction data (trytes) of a specific transaction.
     * These trytes can then be easily converted into the actual transaction object.
     * You can use {@link Transaction#Transaction(String)} for conversion to an object.
     *
     * @param hashes The transaction hashes you want to get trytes from.
     * @return {@link GetTrytesResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException when a transaction hash is invalid
     */
    @Document
    public GetTrytesResponse getTrytes(String... hashes) throws ArgumentException {

        if (!InputValidator.isArrayOfHashes(hashes)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        return getNodeFor(IotaAPICommand.GET_TRYTES).getTrytes(IotaGetTrytesRequest.createGetTrytesRequest(hashes));
    }

    /**
     * Tip selection which returns <tt>trunkTransaction</tt> and <tt>branchTransaction</tt>.
     * The input value <tt>depth</tt> determines how many milestones to go back for finding the transactions to approve.
     * The higher your <tt>depth</tt> value, the more work you have to do as you are confirming more transactions.
     * If the <tt>depth</tt> is too large (usually above 15, it depends on the node's configuration) an error will be returned.
     * The <tt>reference</tt> is an optional hash of a transaction you want to approve.
     * If it can't be found at the specified <tt>depth</tt> then an error will be returned.
     *
     * @param depth     Number of bundles to go back to determine the transactions for approval.
     * @param reference Hash of transaction to start random-walk from.
     *                  This used to make sure the tips returned reference a given transaction in their past.
     *                  Can be <tt>null</tt>.
     * @return {@link GetTransactionsToApproveResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException The depth is invalid
     */
    @Document
    public GetTransactionsToApproveResponse getTransactionsToApprove(Integer depth, String reference) throws ArgumentException {
        if (depth < 0) {
            throw new ArgumentException(INVALID_APPROVE_DEPTH_ERROR);
        }

        return getNodeFor(IotaAPICommand.GET_TRANSACTIONS_TO_APPROVE).getTransactionsToApprove(IotaGetTransactionsToApproveRequest.createIotaGetTransactionsToApproveRequest(depth, reference));
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
     * @throws ArgumentException The request was considered wrong in any way by the node
     */
    @Document
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
     * @param addresses The addresses where we will find the balance for. Must contain the checksum.
     * @param tips      The optional tips to find the balance through.
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException The tips or addresses are invalid
     */
    @Document
    public GetBalancesResponse getBalances(String[] addresses, String[] tips) throws ArgumentException {
        if (null != tips && !InputValidator.isArrayOfHashes(tips)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        validateAddresses(addresses);

        String[] addressesWithoutChecksum = removeChecksumFromAddresses(addresses);

        return getNodeFor(IotaAPICommand.GET_BALANCES).getBalances(IotaGetBalancesRequest.createIotaGetBalancesRequest(addressesWithoutChecksum, tips));
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
     * @param addresses The addresses where we will find the balance for. Must contain the checksum.
     * @param tips      The optional tips to find the balance through.
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @throws ArgumentException The threshold is invalid
     * @throws ArgumentException The tips or addresses are invalid
     * @deprecated The threshold parameter is removed from the getBalances endpoint on IRI nodes.
     * Alternative use {@link IotaAPICore#getBalances(String[], String[])}
     */
    @Deprecated
    @Document
    public GetBalancesResponse getBalances(Integer threshold, String[] addresses, String[] tips) throws ArgumentException {
        if (threshold < 0 || threshold > 100) {
            throw new ArgumentException(INVALID_THRESHOLD_ERROR);
        }

        if (null != tips && !InputValidator.isArrayOfHashes(tips)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        validateAddresses(addresses);

        String[] addressesWithoutChecksum = removeChecksumFromAddresses(addresses);

        return getNodeFor(IotaAPICommand.GET_BALANCES).getBalances(IotaGetBalancesRequest.createIotaGetBalancesRequest(threshold, addressesWithoutChecksum, tips));
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
     * @param addresses The addresses where we will find the balance for. Must contain the checksum.
     * @param tips      The tips to find the balance through. Can be <tt>null</tt>
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     */
    @Document
    public GetBalancesResponse getBalances(List<String> addresses, List<String> tips) throws ArgumentException {
        String[] tipsArray = tips != null ? tips.toArray(new String[0]) : null;
        String[] addressesArray = addresses != null ? addresses.toArray(new String[0]) : null;

        return getBalances(addressesArray, tipsArray);
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
     * @param addresses The addresses where we will find the balance for. Must contain the checksum.
     * @param tips      The tips to find the balance through. Can be <tt>null</tt>
     * @return {@link GetBalancesResponse}
     * @deprecated The threshold parameter is removed from the getBalances endpoint on IRI nodes.
     * Alternative use {@link IotaAPICore#getBalances(List, List)}
     */
    @Deprecated
    @Document
    public GetBalancesResponse getBalances(Integer threshold, List<String> addresses, List<String> tips) throws ArgumentException {
        String[] tipsArray = tips != null ? tips.toArray(new String[0]) : null;
        String[] addressesArray = addresses != null ? addresses.toArray(new String[0]) : null;

        return getBalances(threshold, addressesArray, tipsArray);
    }

    /**
     * <p>
     * Calculates the confirmed balance, as viewed by the latest solid milestone.
     * In addition to the balances, it also returns the referencing <tt>milestone</tt>,
     * and the index with which the confirmed balance was determined.
     * The balances are returned as a list in the same order as the addresses were provided as input.
     * </p>
     *
     * @param addresses The list of addresses you want to get the confirmed balance from. Must contain the checksum.
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     */
    @Document
    public GetBalancesResponse getBalances(List<String> addresses) throws ArgumentException {
        return getBalances(addresses, null);
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
     * @param addresses The list of addresses you want to get the confirmed balance from. Must contain the checksum.
     * @return {@link GetBalancesResponse}
     * @throws ArgumentException The request was considered wrong in any way by the node
     * @deprecated The threshold parameter is removed from the getBalances endpoint on IRI nodes.
     * Alternative use {@link IotaAPICore#getBalances(List)}
     */
    @Deprecated
    @Document
    public GetBalancesResponse getBalances(Integer threshold, List<String> addresses) throws ArgumentException {
        return getBalances(threshold, addresses, null);
    }

    /**
     * Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.
     *
     * @param addresses List of addresses to check if they were ever spent from. Must contain the checksum.
     * @return {@link WereAddressesSpentFromResponse}
     * @throws ArgumentException when an address is invalid
     */
    @Document
    public WereAddressesSpentFromResponse wereAddressesSpentFrom(String... addresses) throws ArgumentException {
        validateAddresses(addresses);

        String[] addressesWithoutChecksum = removeChecksumFromAddresses(addresses);

        return getNodeFor(IotaAPICommand.WERE_ADDRESSES_SPENT_FROM).wereAddressesSpentFrom(IotaWereAddressesSpentFromRequest.create(addressesWithoutChecksum));
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
     * <p>
     * The last 243 trytes of the return value consist of the following:
     * <ul>
     * <li><code>trunkTransaction</code></li>
     * <li><code>branchTransaction</code></li>
     * <li><code>nonce</code></li>
     * </ul>
     * <p>
     * These are valid trytes which are then accepted by the network.
     *
     * @param trunkTransaction   A reference to an external transaction (tip) used as trunk.
     *                           The transaction with index 0 will have this tip in its trunk.
     *                           All other transactions reference the previous transaction in the bundle (Their index-1).
     * @param branchTransaction  A reference to an external transaction (tip) used as branch.
     *                           Each Transaction in the bundle will have this tip as their branch, except the last.
     *                           The last one will have the branch in its trunk.
     * @param minWeightMagnitude The amount of work we should do to confirm this transaction.
     *                           Each 0-trit on the end of the transaction represents 1 magnitude.
     *                           A 9-tryte represents 3 magnitudes, since a 9 is represented by 3 0-trits.
     *                           Transactions with a different minWeightMagnitude are compatible.
     * @param trytes             The list of trytes to prepare for network attachment, by doing proof of work.
     * @return {@link GetAttachToTangleResponse}
     * @throws ArgumentException when a trunk or branch hash is invalid
     * @throws ArgumentException when the provided transaction trytes are invalid
     */
    @Document
    public GetAttachToTangleResponse attachToTangle(String trunkTransaction, String branchTransaction, Integer minWeightMagnitude, String... trytes) throws ArgumentException {
        IotaPoW pow = getOptions().getLocalPoW();
        if (pow != null) {
            return attachToTangleLocalPow(trunkTransaction, branchTransaction, minWeightMagnitude, pow, trytes);
        }

        if (!InputValidator.isHash(trunkTransaction)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        if (!InputValidator.isHash(branchTransaction)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        if (!InputValidator.isArrayOfRawTransactionTrytes(trytes)) {
            throw new ArgumentException(INVALID_TRYTES_INPUT_ERROR);
        }

        IotaAttachToTangleRequest attachToTangleRequest = IotaAttachToTangleRequest.createAttachToTangleRequest(trunkTransaction, branchTransaction, minWeightMagnitude, trytes);
        return getNodeFor(IotaAPICommand.ATTACH_TO_TANGLE).attachToTangle(attachToTangleRequest);
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
     * <p>
     * The last 243 trytes of the return value consist of the following:
     * <ul>
     * <li><code>trunkTransaction</code></li>
     * <li><code>branchTransaction</code></li>
     * <li><code>nonce</code></li>
     * </ul>
     * <p>
     * These are valid trytes which are then accepted by the network.
     *
     * @param trunkTransaction   A reference to an external transaction (tip) used as trunk.
     *                           The transaction with index 0 will have this tip in its trunk.
     *                           All other transactions reference the previous transaction in the bundle (Their index-1).
     * @param branchTransaction  A reference to an external transaction (tip) used as branch.
     *                           Each Transaction in the bundle will have this tip as their branch, except the last.
     *                           The last one will have the branch in its trunk.
     * @param minWeightMagnitude The amount of work we should do to confirm this transaction.
     *                           Each 0-trit on the end of the transaction represents 1 magnitude.
     *                           A 9-tryte represents 3 magnitudes, since a 9 is represented by 3 0-trits.
     *                           Transactions with a different minWeightMagnitude are compatible.
     * @param pow                Method of proof of work
     * @param trytes             The list of trytes to prepare for network attachment, by doing proof of work.
     * @return {@link GetAttachToTangleResponse}
     * @throws ArgumentException when a trunk or branch hash is invalid
     * @throws ArgumentException when the provided transaction trytes are invalid
     */
    @Document
    public GetAttachToTangleResponse attachToTangleLocalPow(String trunkTransaction, String branchTransaction,
                                                            Integer minWeightMagnitude, IotaPoW pow, String... trytes) {
        if (pow == null) {
            log.warn("Called local POW without POW defined, switching to remote POW");
            return attachToTangle(trunkTransaction, branchTransaction, minWeightMagnitude, trytes);
        }

        if (!InputValidator.isHash(trunkTransaction)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        if (!InputValidator.isHash(branchTransaction)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }

        if (!InputValidator.isArrayOfRawTransactionTrytes(trytes)) {
            throw new ArgumentException(INVALID_TRYTES_INPUT_ERROR);
        }

        final String[] resultTrytes = new String[trytes.length];
        String previousTransaction = null;

        try {
            for (int i = 0; i < resultTrytes.length; i++) {
                Transaction txn = new Transaction(trytes[i]);
                txn.setTrunkTransaction(previousTransaction == null ? trunkTransaction : previousTransaction);
                txn.setBranchTransaction(previousTransaction == null ? branchTransaction : trunkTransaction);

                if (txn.getTag().isEmpty() || txn.getTag().matches("9*")) {
                    txn.setTag(txn.getObsoleteTag());
                }

                txn.setAttachmentTimestamp(System.currentTimeMillis());
                txn.setAttachmentTimestampLowerBound(0);
                txn.setAttachmentTimestampUpperBound(3_812_798_742_493L);

                resultTrytes[i] = pow.performPoW(txn.toTrytes(), minWeightMagnitude);
                previousTransaction = new Transaction(resultTrytes[i], SpongeFactory.create(SpongeFactory.Mode.CURL_P81)).getHash();
            }
            Collections.reverse(Arrays.asList(resultTrytes));
        } catch (Exception e) {
            throw new ArgumentException("Could not compute PoW trytes", e);
        }
        return new GetAttachToTangleResponse(resultTrytes);
    }

    /**
     * Interrupts and completely aborts the <tt>attachToTangle</tt> process.
     *
     * @return {@link InterruptAttachingToTangleResponse}
     * @throws ArgumentException If node connection fails
     */
    @Document
    public InterruptAttachingToTangleResponse interruptAttachingToTangle() throws ArgumentException {
        return getNodeFor(IotaAPICommand.INTERRUPT_ATTACHING_TO_TANGLE).interruptAttachingToTangle(IotaCommandRequest.createInterruptAttachToTangleRequest());
    }

    /**
     * Broadcast a list of transactions to all neighbors.
     * The trytes to be used for this call should be valid, attached transaction trytes.
     * These trytes are returned by <tt>attachToTangle</tt>, or by doing proof of work somewhere else.
     *
     * @param trytes The list of transaction trytes to broadcast
     * @return {@link BroadcastTransactionsResponse}
     * @throws ArgumentException when the provided transaction trytes are invalid
     */
    @Document
    public BroadcastTransactionsResponse broadcastTransactions(String... trytes) throws ArgumentException {
        if (!InputValidator.isArrayOfAttachedTrytes(trytes)) {
            throw new ArgumentException(INVALID_ATTACHED_TRYTES_INPUT_ERROR);
        }

        return getNodeFor(IotaAPICommand.BROADCAST_TRANSACTIONS).broadcastTransactions(IotaBroadcastTransactionRequest.createBroadcastTransactionsRequest(trytes));
    }

    /**
     * Stores transactions in the local storage.
     * The trytes to be used for this call should be valid, attached transaction trytes.
     * These trytes are returned by <tt>attachToTangle</tt>, or by doing proof of work somewhere else.
     *
     * @param trytes Transaction data to be stored.
     * @return {@link StoreTransactionsResponse}
     * @throws ArgumentException when the provided transaction trytes are invalid
     */
    @Document
    public StoreTransactionsResponse storeTransactions(String... trytes) throws ArgumentException {
        if (!InputValidator.isArrayOfAttachedTrytes(trytes)) {
            throw new ArgumentException(INVALID_ATTACHED_TRYTES_INPUT_ERROR);
        }

        return getNodeFor(IotaAPICommand.STORE_TRANSACTIONS).storeTransactions(IotaStoreTransactionsRequest.createStoreTransactionsRequest(trytes));
    }

    private void validateAddresses(String[] addresses) {
        if (ArrayUtils.isEmpty(addresses) || !InputValidator.isAddressesArrayValid(addresses)) {
            throw new ArgumentException(INVALID_ADDRESSES_INPUT_ERROR);
        }
    }

    private String[] removeChecksumFromAddresses(String[] addresses) {
        String[] addressesWithoutChecksum = new String[addresses.length];

        for (int i = 0; i < addresses.length; i++) {
            addressesWithoutChecksum[i] = Checksum.removeChecksum(addresses[i]);
        }

        return addressesWithoutChecksum;
    }

    private void validateTags(String[] tags) {
        if (!InputValidator.areValidTags(tags)) {
            throw new ArgumentException(INVALID_TAG_INPUT_ERROR);
        }
    }

    private void validateBundles(String[] bundles) {
        if (!InputValidator.isArrayOfHashes(bundles)) {
            throw new ArgumentException(INVALID_BUNDLE_HASH_ERROR);
        }
    }

    private void validateApprovees(String[] bundles) {
        if (!InputValidator.isArrayOfHashes(bundles)) {
            throw new ArgumentException(INVALID_BUNDLE_HASH_ERROR);
        }
    }

    /**
     * Gets the protocol.
     * Deprecated - Nodes could not have a protocol. Get specific connection and check url
     *
     * @return The protocol to use when connecting to the remote node.
     */
    @Deprecated
    public String getProtocol() {
        //Should be careful, its still possible to not display the protocol if url doesn't contain :
        //Will never break because a split on not found character returns the entire string in [0]
        return getNodeFor(null).url().getProtocol();
    }

    /**
     * Gets the host.
     * Deprecated - Nodes could not have a host. Get specific connection and check url
     *
     * @return The host you want to connect to.
     */
    @Deprecated
    public String getHost() {
        return getNodeFor(null).url().getHost();
    }

    /**
     * Gets the port.
     * Deprecated - Get specific connection and check port
     *
     * @return The port of the host you want to connect to.
     */
    @Deprecated
    public String getPort() {
        return getNodeFor(null).url().getPort() + "";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("----------------------");
        builder.append(System.getProperty("line.separator"));
        builder.append(getOptions().toString());

        builder.append(System.getProperty("line.separator"));
        builder.append("Registered nodes: ");
        builder.append(System.getProperty("line.separator"));
        nodes.forEach(node -> builder
                .append(node.toString())
                .append(System.getProperty("line.separator")));

        return builder.toString();
    }
}
