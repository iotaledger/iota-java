package org.iota.jota.account;

import java.util.Date;
import java.util.concurrent.Future;

import org.iota.jota.account.condition.ExpireCondition;
import org.iota.jota.account.deposits.ConditionalDepositAddress;
import org.iota.jota.account.errors.AccountError;
import org.iota.jota.model.Bundle;
import org.iota.jota.types.Recipient;
import org.iota.jota.utils.thread.TaskService;
import org.iota.mddoclet.Document;

public interface Account extends TaskService {
    
    /**
     * Returns the account’s unique identifier which is a sha256 hash of the account’s seed.
     * 
     * @return the account ID
     * @throws AccountError When we failed to generate the ID
     */
    @Document
    String getId() throws AccountError;
    
    /**
     * Loads the account’s inner event loop to accept commands and provided plugins using current the account settings
     * 
     * @throws AccountError When loading the account failed
     */
    @Document
    void load() throws AccountError;
    
    /**
     * Starts the account event loop
     * 
     * @throws AccountError When an error occurred during start-up
     */
    @Document
    boolean start() throws AccountError;
    
    /**
     * Shutdowns the account’s inner event loop and shutdowns all plugins. 
     * Will gracefully shut down all plugins
     * 
     * @throws AccountError
     */
    @Document
    void shutdown() throws AccountError;
    
    /**
     * Sends the specified amounts to the given recipient.
     * 
     * @param recipient Information about the receiving end of the transactions
     * @return The bundle we sent
     */
    @Document
    Future<Bundle> send(Recipient recipient) throws AccountError;
    
    /**
     * Allocates a new CDA in the underlying store.
     * Note: Specifying both multi-use and an expected amount is not allowed
     * 
     * @param timeOut The date this address expires
     * @param multiUse If we expect multiple deposits
     * @param expectedAmount The final balance we expect to have in this address before we expire
     * @param otherConditions Currently unused, used to specify custom conditions
     * @return {@link ConditionalDepositAddress} The newly generated CDA
     * @throws AccountError When we were not able to generate a new address
     * @throws AccountError When invalid conditions were given
     */
    @Document
    Future<ConditionalDepositAddress> newDepositAddress(Date timeOut, boolean multiUse, long expectedAmount,
            ExpireCondition... otherConditions) throws AccountError;
    
    /**
     * Runs the input selection with the CDAs in order to determine the usable balance for funding transfers.
     * 
     * @return The amount of balance for this account which you can immediately use
     * @throws AccountError When we failed to get the balance
     */
    @Document
    long availableBalance() throws AccountError;
    
    /**
     * Uses all stored CDAs to determine the current total balance.
     * 
     * @return The total balance of this account
     * @throws AccountError When we failed to get the balance
     */
    @Document
    long totalBalance() throws AccountError;
    
    /**
     * Checks whether the state of the account is new. 
     * An account is considered new if no pending transfers or CDAs are stored.
     * 
     * @return <code>true</code> if it is new, otherwise <code>false</code>
     */
    @Document
    boolean isNew();
    
    /**
     * Updates the settings of the account in a synchronized manner by shutting down all plugins, 
     * stopping the event loop, applying the settings 
     * and starting the newly defined plugins and kicking off the event loop again.
     * 
     * @param newSettings The new settings for this account
     * @throws AccountError When we failed to update the settings
     */
    @Document
    void updateSettings(AccountOptions newSettings) throws AccountError;
}
