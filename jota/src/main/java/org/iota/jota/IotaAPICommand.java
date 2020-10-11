package org.iota.jota;

/**
 * IOTA's node command list
 *
 */
public enum IotaAPICommand {

    GET_NODE_INFO("getNodeInfo"),
    GET_NODE_API_CONFIGURATION("getNodeAPIConfiguration"),
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
    STORE_TRANSACTIONS("storeTransactions"),
    WERE_ADDRESSES_SPENT_FROM("wereAddressesSpentFrom"),
    CUSTOM_IXI("IXI");

    private String command;

    /**
     * Initializes a new instance of the IotaAPICommand class.
     */
    IotaAPICommand(String command) {
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

