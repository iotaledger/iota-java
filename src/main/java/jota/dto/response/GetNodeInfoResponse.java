package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaNeighborsRequest}
 **/
public class GetNodeInfoResponse extends AbstractResponse {

    private String appName;
    private String appVersion;
    private String jreVersion;
    private int jreAvailableProcessors;
    private long jreFreeMemory;
    private long jreMaxMemory;
    private long jreTotalMemory;
    private String latestMilestone;
    private int latestMilestoneIndex;
    private String latestSolidSubtangleMilestone;
    private int latestSolidSubtangleMilestoneIndex;
    private int neighbors;
    private int packetsQueueSize;
    private long time;
    private int tips;
    private int transactionsToRequest;

    /**
     * Name of the IOTA software you're currently using (IRI stands for Initial Reference Implementation).
     *
     * @return appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * The version of the IOTA software you're currently running.
     *
     * @return
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * The version of running java version.
     *
     * @return
     */
    public String getJreVersion() {
        return jreVersion;
    }

    /**
     * Available cores on your machine for JRE.
     *
     * @return
     */
    public Integer getJreAvailableProcessors() {
        return jreAvailableProcessors;
    }

    /**
     * The amount of free memory in the Java Virtual Machine.
     *
     * @return
     */
    public long getJreFreeMemory() {
        return jreFreeMemory;
    }

    /**
     * The maximum amount of memory that the Java virtual machine will attempt to use.
     *
     * @return
     */
    public long getJreMaxMemory() {
        return jreMaxMemory;
    }

    /**
     * The total amount of memory in the Java virtual machine.
     *
     * @return
     */
    public long getJreTotalMemory() {
        return jreTotalMemory;
    }

    /**
     * Latest milestone that was signed off by the coordinator.
     *
     * @return
     */
    public String getLatestMilestone() {
        return latestMilestone;
    }

    /**
     * Index of the latest milestone.
     *
     * @return
     */
    public int getLatestMilestoneIndex() {
        return latestMilestoneIndex;
    }

    /**
     * The latest milestone which is solid and is used for sending transactions.
     * For a milestone to become solid your local node must basically approve the subtangle of coordinator-approved transactions,
     * and have a consistent view of all referenced transactions.
     *
     * @return
     */
    public String getLatestSolidSubtangleMilestone() {
        return latestSolidSubtangleMilestone;
    }

    /**
     * Index of the latest solid subtangle.
     *
     * @return
     */
    public int getLatestSolidSubtangleMilestoneIndex() {
        return latestSolidSubtangleMilestoneIndex;
    }

    /**
     * Number of neighbors you are directly connected with.
     *
     * @return
     */
    public int getNeighbors() {
        return neighbors;
    }

    /**
     * Packets which are currently queued up
     *
     * @return
     */
    public int getPacketsQueueSize() {
        return packetsQueueSize;
    }

    /**
     * Current UNIX timestamp.
     *
     * @return
     */
    public Long getTime() {
        return time;
    }

    /**
     * Number of tips in the network.
     *
     * @return
     */
    public int getTips() {
        return tips;
    }

    /**
     * Transactions to request during syncing process.
     *
     * @return
     */
    public int getTransactionsToRequest() {
        return transactionsToRequest;
    }
}