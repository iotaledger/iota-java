package org.iota.jota.account.event;

import org.iota.jota.utils.thread.TaskService;

public interface Plugin extends TaskService, EventListener {

    String name();
}
