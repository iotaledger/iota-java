package jota;

/**
 * IOTA's node command list
 * <p>
 * 'params' is not currently used.
 */
public enum IotaAPICommands {

    GET_NODE_INFO("getNodeInfo", 0),
    GET_NEIGHBORS("getNeighbors", 0),
    ADD_NEIGHBORS("addNeighbors", 1),
    REMOVE_NEIGHBORS("removeNeighbors", 1),
    GET_TIPS("getTips", 0),
    FIND_TRANSACTIONS("findTransactions", 1),
    GET_TRYTES("getTrytes", 0),
    GET_INCLUSIONS_STATES("getInclusionStates", 0),
    GET_BALANCES("getBalances", 0),
    GET_TRANSACTIONS_TO_APPROVE("getTransactionsToApprove", 0),
    ATTACH_TO_TANGLE("attachToTangle", 0),
    INTERRUPT_ATTACHING_TO_TANGLE("interruptAttachingToTangle", 0),
    BROADCAST_TRANSACTIONS("broadcastTransactions", 0),
    STORE_TRANSACTIONS("storeTransactions", 0);

    private String command;
    private int params;

    IotaAPICommands(String command, int params) {
        this.command = command;
        this.params = params;
    }

    public String command() {
        return command;
    }

    public int params() {
        return params;
    }
}

