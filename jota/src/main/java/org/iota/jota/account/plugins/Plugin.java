package org.iota.jota.account.event;

import org.iota.jota.utils.thread.TaskService;

/**
 * A plugin is a custom module which can be added to the accounts API.
 * It will be loaded and unloaded with the API, and automatically listen for events
 *
 */
public interface Plugin extends TaskService, EventListener {

    String name();
}
