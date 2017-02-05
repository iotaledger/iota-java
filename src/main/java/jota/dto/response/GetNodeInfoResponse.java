package jota.dto.response;

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

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getJreVersion() {
        return jreVersion;
    }

    public Integer getJreAvailableProcessors() {
        return jreAvailableProcessors;
    }

    public long getJreFreeMemory() {
        return jreFreeMemory;
    }

    public long getJreMaxMemory() {
        return jreMaxMemory;
    }

    public long getJreTotalMemory() {
        return jreTotalMemory;
    }

    public String getLatestMilestone() {
        return latestMilestone;
    }

    public int getLatestMilestoneIndex() {
        return latestMilestoneIndex;
    }

    public String getLatestSolidSubtangleMilestone() {
        return latestSolidSubtangleMilestone;
    }

    public int getLatestSolidSubtangleMilestoneIndex() {
        return latestSolidSubtangleMilestoneIndex;
    }

    public int getNeighbors() {
        return neighbors;
    }

    public int getPacketsQueueSize() {
        return packetsQueueSize;
    }

    public Long getTime() {
        return time;
    }

    public int getTips() {
        return tips;
    }

    public int getTransactionsToRequest() {
        return transactionsToRequest;
    }
}