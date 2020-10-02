package org.iota.jota.connection;

import org.iota.jota.dto.request.IotaAttachToTangleRequest;
import org.iota.jota.dto.request.IotaBroadcastTransactionRequest;
import org.iota.jota.dto.request.IotaCommandRequest;
import org.iota.jota.dto.request.IotaCustomRequest;
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
import org.iota.jota.dto.response.FindTransactionResponse;
import org.iota.jota.dto.response.GetAttachToTangleResponse;
import org.iota.jota.dto.response.GetBalancesResponse;
import org.iota.jota.dto.response.GetInclusionStateResponse;
import org.iota.jota.dto.response.GetNeighborsResponse;
import org.iota.jota.dto.response.GetNodeAPIConfigurationResponse;
import org.iota.jota.dto.response.GetNodeInfoResponse;
import org.iota.jota.dto.response.GetTipsResponse;
import org.iota.jota.dto.response.GetTransactionsToApproveResponse;
import org.iota.jota.dto.response.GetTrytesResponse;
import org.iota.jota.dto.response.InterruptAttachingToTangleResponse;
import org.iota.jota.dto.response.IotaCustomResponse;
import org.iota.jota.dto.response.RemoveNeighborsResponse;
import org.iota.jota.dto.response.StoreTransactionsResponse;
import org.iota.jota.dto.response.WereAddressesSpentFromResponse;
import org.iota.jota.error.ArgumentException;

public interface IotaNodeApi {


    /**
     * Returns information about the node.
     *
     * @throws Exception
     */
    GetNodeInfoResponse getNodeInfo(IotaCommandRequest request) throws ArgumentException;

    /**
     * Returns information about the node API configuration.
     *
     * @throws Exception
     */
    GetNodeAPIConfigurationResponse getNodeAPIConfiguration(IotaCommandRequest request);

    /**
     * Get the list of neighbors from the node.
     *
     * @throws Exception
     */
    GetNeighborsResponse getNeighbors(IotaCommandRequest request) throws ArgumentException;

    /**
     * Add a list of neighbors to the node.
     *
     * @throws Exception
     */
    AddNeighborsResponse addNeighbors(IotaNeighborsRequest request) throws ArgumentException;

    /**
     * Removes a list of neighbors from the node.
     *
     * @throws Exception
     */
    RemoveNeighborsResponse removeNeighbors(IotaNeighborsRequest request) throws ArgumentException;

    /**
     * Get the list of latest tips (unconfirmed transactions).
     *
     * @throws Exception
     */
    GetTipsResponse getTips(IotaCommandRequest request) throws ArgumentException;

    /**
     * Find the transactions which match the specified input and return.
     *
     * @throws Exception
     */
    FindTransactionResponse findTransactions(IotaFindTransactionsRequest request) throws ArgumentException;

    /**
     * Returns the raw trytes data of a transaction.
     *
     * @throws Exception
     */
    GetTrytesResponse getTrytes(IotaGetTrytesRequest request) throws ArgumentException;

    /**
     * Get the inclusion states of a set of transactions. This is for determining if a transaction was accepted and confirmed by the network or not.
     * You can search for multiple tips (and thus, milestones) to get past inclusion states of transactions.
     *
     * @throws Exception
     */
    GetInclusionStateResponse getInclusionStates(IotaGetInclusionStateRequest request) throws ArgumentException;

    /**
     * It returns the confirmed balance which a list of addresses have at the latest confirmed milestone.
     *
     * @throws Exception
     */
    GetBalancesResponse getBalances(IotaGetBalancesRequest request) throws ArgumentException;

    /**
     * Tip selection which returns trunkTransaction and branchTransaction.
     * The input value is the latest coordinator milestone, as provided through the getNodeInfo API call.
     *
     * @throws Exception
     */
    GetTransactionsToApproveResponse getTransactionsToApprove(IotaGetTransactionsToApproveRequest request) throws ArgumentException;

    /**
     * Attaches the specified transactions (trytes) to the Tangle by doing Proof of Work.
     *
     * @throws Exception
     */
    GetAttachToTangleResponse attachToTangle(IotaAttachToTangleRequest request) throws ArgumentException;

    /**
     * Interrupts and completely aborts the attachToTangle process.
     *
     * @throws Exception
     */
    InterruptAttachingToTangleResponse interruptAttachingToTangle(IotaCommandRequest request) throws ArgumentException;

    /**
     * Broadcast a list of transactions to all neighbors. The input trytes for this call are provided by attachToTangle.
     *
     * @throws Exception
     */
    BroadcastTransactionsResponse broadcastTransactions(IotaBroadcastTransactionRequest request) throws ArgumentException;

    /**
     * Store transactions into the nodes local storage. The trytes to be used for this call are returned by attachToTangle.
     *
     * @throws Exception
     */
    StoreTransactionsResponse storeTransactions(IotaStoreTransactionsRequest request) throws ArgumentException;

    /**
     * Check if a list of addresses was ever spent from, in the current epoch, or in previous epochs.
     *
     * @throws Exception
     */
    WereAddressesSpentFromResponse wereAddressesSpentFrom(IotaWereAddressesSpentFromRequest request) throws ArgumentException;

    /**
     * Custom API call used to call IXI modules.
     */
    IotaCustomResponse customRequest(IotaCustomRequest findTransRequest);
}
