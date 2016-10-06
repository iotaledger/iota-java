package jota;

/**
 * IOTA's node command list
 */
public enum IotaAPICommands {

    GET_NODE_INFO("getNodeInfo", 0), 
    GET_MILESTONE("getMilestone", 0), 
    GET_NEIGHBORS("getNeighbors", 0), 
    GET_TIPS("getTips",0), 
    GET_TRANSFER("getTransfers", 0), //
    FIND_TRANSACTIONS("findTransactions", 0), 
    GET_INCLUSIONS_STATES("getInclusionStates", 0), 
    GET_BUNDLE("getBundle", 0), 
    GET_TRYTES("getTrytes", 0), 
    ANALYZE_TRANSACTIONS("analyzeTransactions", 0), 
    GET_NEW_ADDRESS("getNewAddress", 0), 
    PREPARE_TRANSFERS("prepareTransfers", 0), 
    GET_TRANSACTIONS_TO_APPROVE("getTransactionsToApprove", 0), 
    ATTACH_TO_TANGLE("attachToTangle", 0), 
    INTERRUPT_ATTACHING_TO_TANGLE("interruptAttachingToTangle", 0), 
    PUSH_TRANSACTIONS("pushTransactions", 0), 
    STORE_TRANSACTIONS("storeTransactions", 0), 
    TRANSFER("transfer", 0), 
    REPLAY_TRANSFER("replayTransfer",0), 
    PULL_TRANSACTIONS("pullTransactions", 0);

    private IotaAPICommands(String command, int params) {
        this.command = command;
        this.params = params;
    }

    private String command;
    private int params;

    public String command() {
        return command;
    }

    public int params() {
        return params;
    }
}

