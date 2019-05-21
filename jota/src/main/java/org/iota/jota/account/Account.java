package org.iota.jota.account;

import java.util.Date;
import java.util.concurrent.Future;

import org.iota.jota.account.condition.ExpireCondition;
import org.iota.jota.account.deposits.ConditionalDepositAddress;
import org.iota.jota.account.errors.AccountError;
import org.iota.jota.model.Bundle;
import org.iota.jota.types.Recipient;
import org.iota.jota.utils.thread.TaskService;

public interface Account extends TaskService {
    
    /**
     * Returns the account’s unique identifier which is a sha256 hash of the account’s seed.
     * 
     * @return the account ID
     * @throws AccountError
     */
    String getId() throws AccountError;
    
    /**
     * Loads the account’s inner event loop to accept commands and provided plugins using current the account settings
     * 
     * @throws AccountError
     */
    void load() throws AccountError;
    
    /**
     * Starts the account event loop
     * 
     * @throws AccountError
     */
    boolean start() throws AccountError;
    
    /**
     * Shutdowns the account’s inner event loop and shutdowns all plugins. 
     * Will gracefully shut down all plugins
     * 
     * @throws AccountError
     */
    void shutdown() throws AccountError;
    
    /**
     * Sends the specified amounts to the given recipients.
     * 
     * @param recipient
     * @return
     */
    Future<Bundle> send(Recipient recipient) throws AccountError;
    
    /**
     * Allocates a new CDA in the underlying store.
     * @param timeOut
     * @param multiUse test
     * @param expectedAmount
     * @param otherConditions
     * @return
     * @throws AccountError
     */
    Future<ConditionalDepositAddress> newDepositAddress(Date timeOut, boolean multiUse, long expectedAmount,
            ExpireCondition... otherConditions) throws AccountError;
    
    /**
     * Runs the input selection with the CDAs in order to determine the usable balance for funding transfers.
     * 
     * @return
     * @throws AccountError
     */
    long usableBalance() throws AccountError;
    
    /**
     * Uses all stored CDAs to determine the current total balance.
     * 
     * @return
     * @throws AccountError
     */
    long totalBalance() throws AccountError;
    
    /**
     * Checks whether the state of the account is new. 
     * An account is considered new if no pending transfers or CDRs are stored.
     * 
     * @return
     */
    boolean isNew();
    
    /**
     * Updates the settings of the account in a synchronized manner by shutting down all plugins, 
     * stopping the event loop, applying the settings 
     * and starting the newly defined plugins and kicking off the event loop again.
     * 
     * @param newSettings
     * @throws AccountError
     */
    void updateSettings(AccountOptions newSettings) throws AccountError;
}
