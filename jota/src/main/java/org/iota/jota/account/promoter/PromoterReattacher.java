package org.iota.jota.account.promoter;

import org.iota.jota.model.Bundle;
import org.iota.jota.types.Hash;
import org.iota.jota.utils.thread.TaskService;

public interface PromoterReattacher extends TaskService {

    /**
     * 
     * @param pendingBundle
     * @param promotableTail
     */
    void promote(Bundle pendingBundle, Hash promotableTail);
    
    /**
     * 
     * @param pendingBundle
     */
    void reattach(Bundle pendingBundle);
}
