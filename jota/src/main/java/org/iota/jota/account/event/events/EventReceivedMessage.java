package org.iota.jota.account.event.events;

import java.util.List;

import org.iota.jota.account.event.AccountEventType;
import org.iota.jota.model.Bundle;
import org.iota.jota.model.Transaction;
import org.iota.jota.utils.TrytesConverter;

public class EventReceivedMessage extends EventAbstractBundle {
    
    private String message = null;
    
    public EventReceivedMessage(Bundle bundle) {
        super(AccountEventType.ReceivedMessage, bundle);
    }

    public String getMessage() {
        if (message != null) {
            return message;
        }
        
        StringBuilder str = new StringBuilder();
        
        List<Transaction> bundles = getBundle().getTransactions();
        for (Transaction t : bundles) {
            if (t.getValue() == 0) {
                str.append(t.getSignatureFragments());
            }
        }
        return message = TrytesConverter.trytesToAscii(str.toString());
    }
}
