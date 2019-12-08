package org.iota.jota.account;

import java.util.Date;

public class ExportedAccountState {

    private Date exportedDate;
    
    private String id;
    
    private AccountState state;
    
    public ExportedAccountState() {
        
    }

    public ExportedAccountState(Date exportedDate, String id, AccountState state) {
        this.exportedDate = exportedDate;
        this.id = id;
        this.state = state;
    }

    public Date getExportedDate() {
        return exportedDate;
    }

    public String getId() {
        return id;
    }

    public AccountState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "ExportedAccountState [exportedDate=" + exportedDate + ", id=" + id + ", state=" + state + "]";
    }
}
