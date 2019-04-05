package org.iota.jota.account.event.events;

import java.util.List;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.utils.TrytesConverter;

public class EventReceievedMessage extends EventAbstractBundle {
    
    public EventReceievedMessage(Bundle bundle) {
        super(AccountEventType.ReceivedMessage, bundle);
    }

    public String getMessage() {
        StringBuilder str = new StringBuilder();
        
        List<Transaction> bundles = getBundle().getTransactions();
        for (int i = bundles.size() - 1; i >=0; i--) {
            Transaction t = bundles.get(i);
            str.append(TrytesConverter.trytesToAscii(t.getSignatureFragments()));
        }
        return str.toString();
    }
}
