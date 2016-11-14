package jota.dto.response;

public class GetNodeInfoResponse extends AbstractResponse {

    private String appName;
    private String appVersion;
    private Integer jreAvailableProcessors;
    private Integer jreFreeMemory;
    private Integer jreMaxMemory;
    private Integer jreTotalMemory;
    private String latestMilestone;
    private Long latestMilestoneIndex;
    private String latestSolidSubtangleMilestone;
    private Long latestSolidSubtangleMilestoneIndex;
    private Long neighbors;
    private Long packetsQueueSize;
    private Long time;
    private Long tips;
    private Long transactionsToRequest;

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

    public Integer getJreMaxMemory() {
        return jreMaxMemory;
    }

    public Integer getJreTotalMemory() {
        return jreTotalMemory;
    }

    public String getLatestMilestone() {
        return latestMilestone;
    }

    public Long getLatestMilestoneIndex() {
        return latestMilestoneIndex;
    }

    public String getLatestSolidSubtangleMilestone() {
        return latestSolidSubtangleMilestone;
    }

    public Long getLatestSolidSubtangleMilestoneIndex() {
        return latestSolidSubtangleMilestoneIndex;
    }

    public Long getNeighbors() {
        return neighbors;
    }

    public Long getPacketsQueueSize() {
        return packetsQueueSize;
    }

    public Long getTime() {
        return time;
    }

    public Long getTips() {
        return tips;
    }

    public Long getTransactionsToRequest() {
        return transactionsToRequest;
    }

}