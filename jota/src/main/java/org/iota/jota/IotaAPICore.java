package org.iota.jota;

import static org.iota.jota.utils.Constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.iota.jota.config.ApiConfig;
import org.iota.jota.config.options.ApiBuilderSettings;
import org.iota.jota.config.options.ApiOptions;

import org.iota.jota.connection.Connection;
import org.iota.jota.connection.HttpConnector;
import org.iota.jota.dto.request.IotaAttachToTangleRequest;
import org.iota.jota.dto.request.IotaBroadcastTransactionRequest;
import org.iota.jota.dto.request.IotaCheckConsistencyRequest;
import org.iota.jota.dto.request.IotaCommandRequest;
import org.iota.jota.dto.request.IotaFindTransactionsRequest;
import org.iota.jota.dto.request.IotaGetBalancesRequest;
import org.iota.jota.dto.request.IotaGetInclusionStateRequest;
import org.iota.jota.dto.request.IotaGetTransactionsToApproveRequest;
import org.iota.jota.dto.request.IotaGetTrytesRequest;
import org.iota.jota.dto.request.IotaNeighborsRequest;
import org.iota.jota.dto.request.IotaStoreTransactionsRequest;
import org.iota.jota.dto.request.IotaWereAddressesSpentFromRequest;
import org.iota.jota.dto.response.AddNeighborsResponse;
import org.iota.jota.dto.response.BroadcastTransactionsResponse;
import org.iota.jota.dto.response.CheckConsistencyResponse;
import org.iota.jota.dto.response.FindTransactionResponse;
import org.iota.jota.dto.response.GetAttachToTangleResponse;
import org.iota.jota.dto.response.GetBalancesResponse;
import org.iota.jota.dto.response.GetInclusionStateResponse;
import org.iota.jota.dto.response.GetNeighborsResponse;
import org.iota.jota.dto.response.GetNodeInfoResponse;
import org.iota.jota.dto.response.GetTipsResponse;
import org.iota.jota.dto.response.GetTransactionsToApproveResponse;
import org.iota.jota.dto.response.GetTrytesResponse;
import org.iota.jota.dto.response.InterruptAttachingToTangleResponse;
import org.iota.jota.dto.response.RemoveNeighborsResponse;
import org.iota.jota.dto.response.StoreTransactionsResponse;
import org.iota.jota.dto.response.WereAddressesSpentFromResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Transaction;
import org.iota.jota.pow.ICurl;
import org.iota.jota.pow.SpongeFactory;
import org.iota.jota.utils.AbstractBuilder;
import org.iota.jota.utils.Checksum;
import org.iota.jota.utils.InputValidator;

/**
 * 
 * This class provides access to the Iota core API
 * Handles direct methods with the connected node(s), and does basic verification
 * 
 */
public class IotaAPICore {
    private static final Logger log = LoggerFactory.getLogger(IotaAPICore.class);

    ApiOptions options;
    
    protected List<Connection> nodes = new ArrayList<>();
    
    private Connection service = null;
    
    protected IotaAPICore(ApiOptions options) {
        this.options = options;
        
        for (Connection c : options.getNodes()) {
            addNode(c);
        }
    }
    
    public boolean hasNodes() {
        return nodes != null && nodes.size() > 0;
    }
    
    public Connection getRandomNode() {
        if (!hasNodes()) return null;
        return nodes.get(new Random().nextInt(nodes.size()));
    }
    
    public List<Connection> getNodes() {
        return nodes;
    }
    
    public boolean addNode(Connection n) {
        try {
            for (Connection c : nodes) {
                if (c.toString().equals(n.toString())) {
                    log.warn("Tried to add a node we allready have: " + n);
                    return true;
                }
            }
            
            boolean started = n.start();
            if (started) {
            
                //Huray! Lets add it
                nodes.add(n);
                log.debug("Added node: " + n.toString());
                //Legacy wants a node in service for getting ports etc
                if (null == service) service = n;
            }
            return started;
        } catch (Exception e) {
            log.warn("Failed to add node connection to pool due to " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gives a clone of the custom curl defined in {@link ApiOptions}
     * @return
     */
    public ICurl getCurl() {
        return options.getCustomCurl().clone();
    }
    
    public void setCurl(ICurl localPoW) {
        options.setCustomCurl(localPoW);
    }
    
    public IotaLocalPoW getLocalPoW() {
        return options.getLocalPoW();
    }
    
    public void setLocalPoW(IotaLocalPoW localPoW) {
        options.setLocalPoW(localPoW);
    }

    /**
     * Returns information about this node.
     *
     * @return {@link GetNodeInfoResponse}
     * @throws ArgumentException 
     */
    public GetNodeInfoResponse getNodeInfo() throws ArgumentException {
        return service.getNodeInfo(IotaCommandRequest.createNodeInfoRequest());
    }

    /**
     * Returns the set of neighbors you are connected with, as well as their activity statistics (or counters).
     * The activity counters are reset after restarting IRI.
     *
     * @return {@link GetNeighborsResponse}
     * @throws ArgumentException 
     */
    public GetNeighborsResponse getNeighbors() throws ArgumentException {
        return service.getNeighbors(IotaCommandRequest.createGetNeighborsRequest());
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
        return service.addNeighbors(IotaNeighborsRequest.createAddNeighborsRequest(uris));
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
        return service.removeNeighbors(IotaNeighborsRequest.createRemoveNeighborsRequest(uris));
    }

    /**
     * Returns all tips currently known by this node.
     *
     * @return {@link GetTipsResponse}
     * @throws ArgumentException 
     */
    public GetTipsResponse getTips() throws ArgumentException {
        return service.getTips(IotaCommandRequest.createGetTipsRequest());
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
        String[] addressesWithoutChecksum = null;
        if (null != addresses) {
            addressesWithoutChecksum = new String[addresses.length];
            for (int i = 0; i < addresses.length; i++) {
                String addressO = Checksum.removeChecksum(addresses[i]);
                addressesWithoutChecksum[i] = addressO;
            }
        }
        
        final IotaFindTransactionsRequest findTransRequest = IotaFindTransactionsRequest
                .createFindTransactionRequest()
                .byAddresses(addressesWithoutChecksum)
                .byTags(tags)
                .byApprovees(approvees)
                .byBundles(bundles);

        return service.findTransactions(findTransRequest);
    }

    /**
     * Find the transactions by addresses
     *
     * @param addresses An array of addresses.
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException 
     */
    public FindTransactionResponse findTransactionsByAddresses(String... addresses) throws ArgumentException {
        if (!InputValidator.isStringArrayValid(addresses)) {
            throw new ArgumentException(ARRAY_NULL_OR_EMPTY);
        }
        
        if (!InputValidator.isAddressesArrayValid(addresses)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }
        
        return findTransactions(addresses, null, null, null);
    }

    /**
     * Find the transactions by bundles
     *
     * @param bundles An array of bundles.
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException 
     */
    public FindTransactionResponse findTransactionsByBundles(String... bundles) throws ArgumentException {
        if (!InputValidator.isStringArrayValid(bundles)) {
            throw new ArgumentException(ARRAY_NULL_OR_EMPTY);
        }
        
        if (!InputValidator.isAddressesArrayValid(bundles)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }
        
        
        return findTransactions(null, null, null, bundles);
    }

    /**
     * Find the transactions by approvees
     *
     * @param approvees An array of approveess.
     * @return {@link FindTransactionResponse}
     * @throws ArgumentException 
     */
    public FindTransactionResponse findTransactionsByApprovees(String... approvees) throws ArgumentException {
        if (!InputValidator.isStringArrayValid(approvees)) {
            throw new ArgumentException(ARRAY_NULL_OR_EMPTY);
        }
        
        if (!InputValidator.isAddressesArrayValid(approvees)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }
        
        return findTransactions(null, null, approvees, null);
    }

    /**
     * Find the transactions by digests
     * Deprecated, Use findTransactionsByTag
     * 
     * @param digests A List of digests. Each should be 27 trytes.
     * @return The transaction hashes which are returned depend on the input.
     * @throws ArgumentException 
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
     * @throws ArgumentException 
     */
    public FindTransactionResponse findTransactionsByTags(String... tags) throws ArgumentException {
        if (!InputValidator.isStringArrayValid(tags)) {
            throw new ArgumentException(ARRAY_NULL_OR_EMPTY);
        }
        
        if (!InputValidator.isArrayOfTrytes(tags, TAG_LENGTH)) {
            throw new ArgumentException(INVALID_TRYTES_INPUT_ERROR);
        }
        
        return findTransactions(null, tags, null, null);
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

        return service.getInclusionStates(IotaGetInclusionStateRequest
                .createGetInclusionStateRequest(transactions, tips));
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
        
        return service.getTrytes(IotaGetTrytesRequest.createGetTrytesRequest(hashes));
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
        if (depth < 0) {
            throw new ArgumentException(INVALID_APPROVE_DEPTH_ERROR);
        }
        
        return service.getTransactionsToApprove(IotaGetTransactionsToApproveRequest.createIotaGetTransactionsToApproveRequest(depth, reference));
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
        if (threshold < 0 || threshold > 100) {
            throw new ArgumentException(INVALID_THRESHOLD_ERROR);
        }
        
        if (!InputValidator.isArrayOfHashes(addresses)) {
            throw new ArgumentException(INVALID_HASHES_INPUT_ERROR);
        }
        
        String[] addressesWithoutChecksum = new String[addresses.length];
        for (int i = 0; i < addresses.length; i++) {
            addressesWithoutChecksum[i] = Checksum.removeChecksum(addresses[0]);
        }
        
        return service.getBalances(IotaGetBalancesRequest.createIotaGetBalancesRequest(threshold, addressesWithoutChecksum, tips));
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
        String[] tipsArray = tips != null ? tips.toArray(new String[tips.size()]) : null;
        return getBalances(threshold, addresses.toArray(new String[addresses.size()]), tipsArray);
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

        return service.wereAddressesSpentFrom(IotaWereAddressesSpentFromRequest.create(addresses));
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

        return service.checkConsistency(IotaCheckConsistencyRequest.create(tails));
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

        if (!InputValidator.isArrayOfRawTransactionTrytes(trytes)) {
            throw new ArgumentException(INVALID_TRYTES_INPUT_ERROR);
        }

        IotaLocalPoW pow = options.getLocalPoW();
        if (pow != null) {
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
                resultTrytes[i] = pow.performPoW(txn.toTrytes(), minWeightMagnitude);
                previousTransaction = new Transaction(resultTrytes[i]).getHash();
            }
            return new GetAttachToTangleResponse(resultTrytes);
        }

        return service.attachToTangle(IotaAttachToTangleRequest.createAttachToTangleRequest(trunkTransaction, branchTransaction, minWeightMagnitude, trytes));
    }

    /**
     * Interrupts and completely aborts the <tt>attachToTangle</tt> process.
     * 
     * @return {@link InterruptAttachingToTangleResponse}
     * @throws ArgumentException 
     */
    public InterruptAttachingToTangleResponse interruptAttachingToTangle() throws ArgumentException {
        return service.interruptAttachingToTangle(IotaCommandRequest.createInterruptAttachToTangleRequest());
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
        if (!InputValidator.isArrayOfRawTransactionTrytes(trytes)) {
            throw new ArgumentException(INVALID_ATTACHED_TRYTES_INPUT_ERROR);
        }

        return service.broadcastTransactions(IotaBroadcastTransactionRequest.createBroadcastTransactionsRequest(trytes));
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
        if (!InputValidator.isArrayOfRawTransactionTrytes(trytes)) {
            throw new ArgumentException(INVALID_ATTACHED_TRYTES_INPUT_ERROR);
        }
        
        return service.storeTransactions(IotaStoreTransactionsRequest.createStoreTransactionsRequest(trytes));
    }

    /**
     * Gets the protocol.
     * Deprecated - Nodes could not have a protocol. Get specific connection and check url
     * @return The protocol to use when connecting to the remote node.
     */
    @Deprecated
    public String getProtocol() {
        //Should be carefull, its still possible to not display the protocol if url doesn't contain :
        //Will never break because a split on not found character returns the entire string in [0]
        return service.url().split(":")[0];
    }

    /**
     * Gets the host.
     * Deprecated - Nodes could not have a host. Get specific connection and check url
     * @return The host you want to connect to.
     */
    @Deprecated
    public String getHost() {
        return service.url().split("://")[1];
    }

    /**
     * Gets the port.
     * Deprecated - Get specific connection and check port
     * @return The port of the host you want to connect to.
     */
    @Deprecated
    public String getPort() {
        return service.port() + "";
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("----------------------");
        builder.append(System.getProperty("line.separator"));
        builder.append(options.toString());
        
        builder.append(System.getProperty("line.separator"));
        builder.append("Registrered nodes: " + System.getProperty("line.separator"));
        for (Connection n : nodes) {
            builder.append(n.toString() + System.getProperty("line.separator"));
        }
        
        return builder.toString();
    }
   
    //All casts are to T, and are okay unless you do really weird things.
    //Warnings are annoying
    @SuppressWarnings("unchecked")
    protected abstract static class Builder<T extends Builder<T, E>, E extends IotaAPICore> 
            extends AbstractBuilder<T, E, ApiConfig> 
            implements ApiConfig, ApiBuilderSettings {
        
        private static final Logger log = LoggerFactory.getLogger(IotaAPICore.class);
        
        List<Connection> nodes = new ArrayList<>();
        
        String protocol, host;
        int port;
        
        // If this is null, no local PoW is done, therefor no default value
        IotaLocalPoW localPoW;
        ICurl customCurl = SpongeFactory.create(SpongeFactory.Mode.KERL);
        
        public Builder() {
            super(log);
        }
        
        /**
         * 
         * @return
         * @throws Exception
         */
        protected T generate() throws Exception {
            for (ApiConfig config : getConfigs()) {
                if (config != null) {
                    if (null == protocol) {
                        protocol = config.getLegacyProtocol();
                    }
    
                    if (null == host) {
                        host = config.getLegacyHost();
                    }
    
                    if (0 == port) {
                        port = config.getLegacyPort();
                    }
                    
                    if (config.hasNodes()) {
                        for (Connection c : config.getNodes()) {
                            nodes.add(c);
                        }
                    }
                }
            };
            
            if (!hasNodes()) {
                //Either we have a legacy node defined in the builder, or in the config.
                if (null != host && null != protocol && 0 != port) {
                    nodes.add(new HttpConnector(protocol, host, port));
                } else {
                  //Fallback on legacy option from config
                    for (ApiConfig config : getConfigs()) {
                        if (config.getLegacyHost() != null
                                && config.getLegacyProtocol() != null
                                && config.getLegacyPort() != 0) {
                            nodes.add(new HttpConnector(
                                    config.getLegacyProtocol(), 
                                    config.getLegacyHost(), 
                                    config.getLegacyPort())
                            );
                            
                            break; //If we define one in config, dont check rest, Otherwise we end up using custom & default
                        }
                    }
                }
            }
            return (T) this;
        }
        
        public T withCustomCurl(ICurl curl) {
            customCurl = curl;
            return (T) this;
        }

        public T host(String host) {
            this.host = host;
            return (T) this;
        }

        public T port(int port) {
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
        
        public String getProtocol() {
            return protocol;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public IotaLocalPoW getLocalPoW() {
            return localPoW;
        }

        public ICurl getCustomCurl() {
            return customCurl;
        }

        public List<Connection> getNodes() {
            return nodes;
        }

        @Override
        public boolean hasNodes() {
            return nodes != null && nodes.size() > 0;
        }

        @Override
        public int getLegacyPort() {
            return getPort();
        }

        @Override
        public String getLegacyProtocol() {
            return getProtocol();
        }

        @Override
        public String getLegacyHost() {
            return getHost();
        }
    }
}
