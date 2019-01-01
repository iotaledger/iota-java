package org.iota.jota.account.transferchecker;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.event.EventManager;

public class IncomingTransferCheckerImpl extends TransferCheckerImpl implements IncomingTransferChecker {

    private EventManager eventManager;
    private IotaAPI api;

    public IncomingTransferCheckerImpl(EventManager eventManager, IotaAPI api) {
        this.eventManager = eventManager;
        this.api = api;
    }

    @Override
    public void load() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean start() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
        
    }

}
