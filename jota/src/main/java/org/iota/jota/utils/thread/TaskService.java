package org.iota.jota.utils.thread;

/**
 * A task service is a task which is run by a service.
 * It will be loaded, and if no error occured,started in a later stage.
 * When the service stops, this task will have its shutdown method called.
 */
public interface TaskService {

    /**
     * Loads this task
     *
     * @throws Exception when loading failed
     */
    void load() throws Exception ;

    /**
     * Starts this task
     *
     * @return <code>true</code> if loaded successful. otherwise <code>false</code>
     */
    boolean start();

    /**
     * Stops this task.
     */
    void shutdown();

}
