package jota;

/**
 * IOTA's node command list
 *
 */
public enum IotaAPICommands {

    GET_NODE_INFO("getNodeInfo"),
    GET_NEIGHBORS("getNeighbors"),
    ADD_NEIGHBORS("addNeighbors"),
    REMOVE_NEIGHBORS("removeNeighbors"),
    GET_TIPS("getTips"),
    FIND_TRANSACTIONS("findTransactions"),
    GET_TRYTES("getTrytes"),
    GET_INCLUSIONS_STATES("getInclusionStates"),
    GET_BALANCES("getBalances"),
    GET_TRANSACTIONS_TO_APPROVE("getTransactionsToApprove"),
    ATTACH_TO_TANGLE("attachToTangle"),
    INTERRUPT_ATTACHING_TO_TANGLE("interruptAttachingToTangle"),
    BROADCAST_TRANSACTIONS("broadcastTransactions"),
    STORE_TRANSACTIONS("storeTransactions");

    private String command;

    /**
     * Initializes a new instance of the IotaAPICommands class.
     */
    IotaAPICommands(String command) {
        this.command = command;
    }

    /**
     * Gets the command.
     *
     * @return The command.
     */
    public String command() {
        return command;
    }

}

