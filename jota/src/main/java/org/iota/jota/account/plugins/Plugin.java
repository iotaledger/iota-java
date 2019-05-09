package org.iota.jota.account.plugins;

import org.iota.jota.account.Account;
import org.iota.jota.account.event.EventListener;
import org.iota.jota.utils.thread.TaskService;

/**
 * A plugin is a custom module which can be added to the accounts API.
 * It will be loaded and unloaded with the API, and automatically listen for events
 *
 */
public interface Plugin extends TaskService, EventListener {

    /**
     * Setter for the account this plugin is working for.
     * If set to code>null</code>, this plugin is unloaded
     *
     * @param account The account object were working for
     */
    void setAccount(Account account);

    /**
     * Getter for the account this plugin is working for.
     * If <code>null</code>, this plugin is not loaded
     *
     * @return The account object
     */
    Account getAccount();

    /**
     * The public identifier/name of this plugin
     *
     * @return
     */
    String name();
}
