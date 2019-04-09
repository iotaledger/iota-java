package org.iota.jota.utils.thread;

public interface TaskService {

    void load() throws Exception ;
    
    boolean start();
    
    void shutdown();

}
