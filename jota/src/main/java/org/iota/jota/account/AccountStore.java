package org.iota.jota.account;

import org.iota.jota.account.deposits.StoredDepositAddress;
import org.iota.jota.types.Hash;
import org.iota.jota.types.Trytes;

import java.util.Map;

/**
 * This interface defines a persistence layer which takes care of reading and storing account data.
 */
public interface AccountStore {

    /**
     * Loads an existing or allocates a new account state from/in the database and returns the state.
     *
     * @param id The account we are loading or creating
     * @return The loaded account state
     */
    AccountState loadAccount(String id);

    /**
     * Saves this specific account state to the store, regardless of what was stored.
     *
     * @param id The id of the account we are storing
     * @param state The state we are storing for the account
     */
    void saveAccount(String id, AccountState state);

    /**
     * Removes the accoutn state linekd to this account.
     *
     * @param id The id of the account
     */
    void removeAccount(String id);

    /**
     * Reads and returns the last used key index.
     *
     * @param id The id of the account we check for
     * @return The last used index, always at least 0
     */
    int readIndex(String id);

    /**
     * Stores the given index as the last used key index for the given account.
     *
     * @param id The id of the account
     * @param index The new index of this account
     */
    void writeIndex(String id, int index);

    /**
     * Stores the deposit address under the given account with the used key index.
     *
     * @param id The id of the account
     * @param index The index used for this address
     * @param address The deposit address we are adding
     */
    void addDepositAddress(String id, int index, StoredDepositAddress address);

    /**
     * Removes the deposit address with the given key index under the given account.
     *
     * @param id The id of the account
     * @param index The index of the deposit we are removing
     */
    void removeDepositAddress(String id, int index);

    /**
     * Loads the stored deposit addresses of the given account.
     *
     * @param id The id of the account
     * @return A map of deposit addresses with their stored index
     */
    Map<Integer, StoredDepositAddress> getDepositAddresses(String id);
    
    /**
     * Stores the pending transfer under the given account with the origin tail tx hash as a key and
     * removes all deposit addresses which correspond to the used key indices for the transfer.
     * 
     * @param id The id of the account
     * @param tailTx The tail tx of this transfer
     * @param bundleTrytes The trytes/essense of this transfer
     * @param indices indexes to remove for this transfer, currently unused since we remove when we ask for inputs
     */
    void addPendingTransfer(String id, Hash tailTx, Trytes[] bundleTrytes, int... indices);

    /**
     * Removes the pending transfer with the given origin tail transaction hash from the given account.
     *
     * @param id The id of the account
     * @param tailHash The tail tx of the transfer we are remiving
     */
    void removePendingTransfer(String id, Hash tailHash );

    /**
     * Adds the given new tail transaction hash (presumably from a reattachment) under the given pending transfer
     * indexed by the given origin tail transaction hash.
     *
     * @param id The id of the account
     * @param tailHash The tail tx of the initial transfer
     * @param newTailTxHash The tail tx of the newly added transfer
     */
    void addTailHash(String id, Hash tailHash, Hash newTailTxHash);

    /**
     * Returns all pending transfers of the given account.
     *
     * @param id The id of the account
     * @return A map of pending transfers linked to their initial tail tx hash
     */
    Map<String, PendingTransfer> getPendingTransfers(String id);

    /**
     * Imports the given account state into the store.
     * An existing account is overridden by this method.
     *
     * @param state The state of the account we are storing
     */
    void importAccount(ExportedAccountState state);

    /**
     * Axports the given account from the store.
     *
     * @param id The id of the account we want to export
     * @return The exported account state
     */
    ExportedAccountState exportAccount(String id);
}
