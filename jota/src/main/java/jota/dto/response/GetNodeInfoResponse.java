package jota.dto.response;

/**
 * Response of {@link jota.dto.request.IotaNeighborsRequest}.
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
     * The name of the IOTA software the node currently running (IRI stands for Initial Reference Implementation).
     *
     * @return appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * The version of the IOTA software the node currently running.
     *
     * @return The version of the IOTA software the node currently running.
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * The version of running java version.
     *
     * @return The version of running java version.
     */
    public String getJreVersion() {
        return jreVersion;
    }

    /**
     * Available cores on the node currently running.
     *
     * @return Available cores on the machine for JRE.
     */
    public Integer getJreAvailableProcessors() {
        return jreAvailableProcessors;
    }

    /**
     * The amount of free memory in the Java Virtual Machine.
     *
     * @return The amount of free memory in the Java Virtual Machine.
     */
    public long getJreFreeMemory() {
        return jreFreeMemory;
    }

    /**
     * The maximum amount of memory that the Java virtual machine will attempt to use.
     *
     * @return The maximum amount of memory that the Java virtual machine will attempt to use.
     */
    public long getJreMaxMemory() {
        return jreMaxMemory;
    }

    /**
     * The total amount of memory in the Java virtual machine.
     *
     * @return The total amount of memory in the Java virtual machine.
     */
    public long getJreTotalMemory() {
        return jreTotalMemory;
    }

    /**
     * Latest milestone that was signed off by the coordinator.
     *
     * @return Latest milestone that was signed off by the coordinator.
     */
    public String getLatestMilestone() {
        return latestMilestone;
    }

    /**
     * Index of the latest milestone.
     *
     * @return Index of the latest milestone.
     */
    public int getLatestMilestoneIndex() {
        return latestMilestoneIndex;
    }

    /**
     * The latest milestone which is solid and is used for sending transactions.
     * For a milestone to become solid the local node must basically approve the subtangle of coordinator-approved transactions,
     * and have a consistent view of all referenced transactions.
     *
     * @return The latest milestone which is solid and is used for sending transactions.
     */
    public String getLatestSolidSubtangleMilestone() {
        return latestSolidSubtangleMilestone;
    }

    /**
     * Index of the latest solid subtangle.
     *
     * @return Index of the latest solid subtangle.
     */
    public int getLatestSolidSubtangleMilestoneIndex() {
        return latestSolidSubtangleMilestoneIndex;
    }

    /**
     * Number of neighbors the node connected with.
     *
     * @return Number of neighbors the node connected with.
     */
    public int getNeighbors() {
        return neighbors;
    }

    /**
     * Packets which are currently queued up.
     *
     * @return Packets which are currently queued up.
     */
    public int getPacketsQueueSize() {
        return packetsQueueSize;
    }

    /**
     * Current UNIX timestamp.
     *
     * @return Current UNIX timestamp.
     */
    public Long getTime() {
        return time;
    }

    /**
     * Number of tips in the network.
     *
     * @return Number of tips in the network.
     */
    public int getTips() {
        return tips;
    }

    /**
     * Transactions to request during syncing process.
     *
     * @return Transactions to request during syncing process.
     */
    public int getTransactionsToRequest() {
        return transactionsToRequest;
    }
}