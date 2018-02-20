package com.thepalladiumgroup.iqm.core.model;

/**
 * Created by Koske Kimutai [2016/07/08]
 */
public class RecordStats {
    private int totalCount;
    private int completeCount;
    private int pendingCount;
    private int completeCountPercent;
    private int pendingCountPercent;

    public RecordStats(int completeCount, int pendingCount, int totalCount) {
        this.completeCount = completeCount;
        this.pendingCount = pendingCount;
        this.totalCount = totalCount;

        if ((totalCount > 0) && (completeCount > 0)) {
            completeCountPercent = (completeCount * 100) / totalCount;
        }

        if ((totalCount > 0) && (pendingCount > 0)) {
            pendingCountPercent = (pendingCount * 100) / totalCount;
        }

    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public int getPendingCountPercent() {
        return pendingCountPercent;
    }

    public void setPendingCountPercent(int pendingCountPercent) {
        this.pendingCountPercent = pendingCountPercent;
    }

    public int getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }

    public int getCompleteCountPercent() {
        return completeCountPercent;
    }

    public void setCompleteCountPercent(int completeCountPercent) {
        this.completeCountPercent = completeCountPercent;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalCountInfo() {
        return totalCount + " RECORDS";
    }

    public String getCompleteCountInfo() {
        return completeCount + " COMPLETE";
    }

    public String getPendingCountInfo() {
        return pendingCount + " PENDING";
    }

    public String getCompleteCountPercentInfo() {
        return completeCountPercent + " %";
    }

    public String getPendingCountPercentInfo() {
        return pendingCountPercent + " %";
    }

    @Override
    public String toString() {
        return getTotalCountInfo() + "," + getCompleteCountInfo() + " (" + getCompleteCountPercentInfo() + ")," + getPendingCountInfo() + " (" + getPendingCountPercentInfo() + ")";
    }
}
