package org.iota.jota.account.transferchecker.tasks;

import org.iota.jota.model.Bundle;

public abstract class BundleTask implements Runnable {
    
    Bundle bundle;
    
    public BundleTask(Bundle bundle) {
        this.bundle = bundle;
    }
    
    public Bundle getBundle() {
        return bundle;
    }
}
