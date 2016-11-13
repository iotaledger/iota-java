package jota.dto.response;

public class GetNodeInfoResponse extends AbstractResponse {

    private String appName;
    private String appVersion;
    private Integer jreAvailableProcessors;
    private Integer jreFreeMemory;
    private float jreMaxMemory;
    private float jreTotalMemory;
    private String latestMilestone;
    private Integer latestMilestoneIndex;
    private String latestSolidSubtangleMilestone;
    private Integer latestSolidSubtangleMilestoneIndex;
    private Integer neighbors;
    private Integer packetsQueueSize;
    private long time;
    private Integer tips;
    private Integer transactionsToRequest;

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Integer getJreAvailableProcessors() {
        return jreAvailableProcessors;
    }

    public Integer getJreFreeMemory() {
        return jreFreeMemory;
    }

    public float getJreMaxMemory() {
        return jreMaxMemory;
    }

    public float getJreTotalMemory() {
        return jreTotalMemory;
    }

    public String getLatestMilestone() {
        return latestMilestone;
    }

    public Integer getLatestMilestoneIndex() {
        return latestMilestoneIndex;
    }

    public String getLatestSolidSubtangleMilestone() {
        return latestSolidSubtangleMilestone;
    }

    public Integer getLatestSolidSubtangleMilestoneIndex() {
        return latestSolidSubtangleMilestoneIndex;
    }

    public Integer getNeighbors() {
        return neighbors;
    }

    public Integer getPacketsQueueSize() {
        return packetsQueueSize;
    }

    public long getTime() {
        return time;
    }

    public Integer getTips() {
        return tips;
    }

    public Integer getTransactionsToRequest() {
        return transactionsToRequest;
    }

}