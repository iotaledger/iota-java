package jota.dto.response;

public class GetNodeInfoResponse extends AbstractResponse {

    private String incomingPacketsBacklog;
    private String appName;
    private String transactionsToRequest;
    private String jreTotalMemory;
    private String time;
    private String neighbors;
    private String milestoneIndex;
    private String appVersion;
    private String jreAvailableProcessors;
    private String jreMaxMemory;
    private String tips;
    private String jreFreeMemory;

    public String getIncomingPacketsBacklog() {
        return incomingPacketsBacklog;
    }

    public String getAppName() {
        return appName;
    }

    public String getTransactionsToRequest() {
        return transactionsToRequest;
    }

    public String getJreTotalMemory() {
        return jreTotalMemory;
    }

    public String getTime() {
        return time;
    }

    public String getNeighbors() {
        return neighbors;
    }

    public String getMilestoneIndex() {
        return milestoneIndex;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getJreAvailableProcessors() {
        return jreAvailableProcessors;
    }

    public String getJreMaxMemory() {
        return jreMaxMemory;
    }

    public String getTips() {
        return tips;
    }

    public String getJreFreeMemory() {
        return jreFreeMemory;
    }

}
