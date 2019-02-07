package org.iota.jota.account.transferchecker;

import org.iota.jota.IotaAPI;
import org.iota.jota.account.AccountStateManager;
import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.EventManager;

public class IncomingTransferCheckerImpl extends TransferCheckerImpl implements IncomingTransferChecker {

    private EventManager eventManager;
    private IotaAPI api;
    private AccountStateManager accountManager;

    public IncomingTransferCheckerImpl(EventManager eventManager, IotaAPI api, AccountStateManager accountManager) {
        this.eventManager = eventManager;
        this.api = api;
        this.accountManager = accountManager;
    }

    @Override
    public void load() {
        // For each address we have generated, check for transactions
        // Maybe do this on an await for address regeneration in another service?
        // We should have a accounts wide boolean for loaded
    }
    
    @AccountEvent
    public void addressGenerated() {
        
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

    @Override
    public String name() {
        return "IncomingTransferChecker";
    }

}
