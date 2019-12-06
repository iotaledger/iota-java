package org.iota.jota.dto.response;

/**
 * Contains information about the result of a successful {@link com.iota.iri.service.API#getNodeAPIConfigurationStatement()} API call.
 * Response of api request 'GetNodeAPIConfiguration'.
 */
public class GetNodeAPIConfigurationResponse extends AbstractResponse {
    private int maxFindTransactions;
    private int maxRequestsList;
    private int maxGetTrytes;
    private int maxBodyLength;
    private boolean testNet;
    private int milestoneStartIndex;

    /**
     * Initializes a new instance of the GetBundleResponse class.
     */
    public static AbstractResponse create(int maxFindTransactions, int maxRequestsList, int maxGetTrytes, int maxBodyLength, boolean testNet, int milestoneStartIndex) {
        final GetNodeAPIConfigurationResponse res = new GetNodeAPIConfigurationResponse();

        res.maxFindTransactions = maxFindTransactions;
        res.maxRequestsList = maxRequestsList;
        res.maxGetTrytes = maxGetTrytes;
        res.maxBodyLength = maxBodyLength;
        res.testNet = testNet;
        res.milestoneStartIndex = milestoneStartIndex;

        return res;
    }

    /** 
     * The maximal number of transactions that may be returned by the "findTransactions" API call. 
     * If the number of transactions found exceeds this number an error will be returned.
     * 
     * @return The maximum number of transactions returned
     * */
    public int getMaxFindTransactions() {
        return maxFindTransactions;
    }

    /** 
     * The maximal number of parameters one can place in an API call. 
     * If the number parameters exceeds this number an error will be returned.
     * 
     * @return The maximum number of request parameters
     */
    public int getMaxRequestsList() {
        return maxRequestsList;
    }

    /**
     * The maximal number of trytes that may be returned by the "getTrytes" API call. 
     * If the number of transactions found exceeds this number an error will be returned.
     * 
     * @return The maximum number of trytes returned
     */
    public int getMaxGetTrytes() {
        return maxGetTrytes;
    }

    /**
     * The maximal number of characters the body of an API call may hold. 
     * If a request body length exceeds this number an error will be returned.
     * 
     * @return The maximum length of the request body
     */
    public int getMaxBodyLength() {
        return maxBodyLength;
    }

    /** 
     * If this node has started in testnet mode.
     * 
     * @return <code>true</code> if it is, otherwise <code>false</code>
     */
    public boolean isTestNet() {
        return testNet;
    }

    /**
     * The start index of the milestones. 
     * This index is encoded in each milestone transaction by the coordinator.
     * Usually the last global snapshot, with local snapshots it is the last/oldest local snapshot milestone
     * 
     * @return The starting milestone index
     */
    public int getMilestoneStartIndex() {
        return milestoneStartIndex;
    }
}
