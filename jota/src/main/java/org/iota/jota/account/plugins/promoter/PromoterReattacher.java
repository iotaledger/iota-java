package org.iota.jota.account.plugins.promoter;

import org.iota.jota.model.Bundle;
import org.iota.jota.utils.thread.TaskService;

public interface PromoterReattacher extends TaskService {

    /**
     * 
     * @param pendingBundle
     * @param promotableTail
     */
    void promote(Bundle pendingBundle, String promotableTail);
    
    /**
     * 
     * @param pendingBundle
     */
    void reattach(Bundle pendingBundle);
}
