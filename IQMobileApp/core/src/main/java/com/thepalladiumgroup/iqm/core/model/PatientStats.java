package com.thepalladiumgroup.iqm.core.model;

/**
 * Created by Koske Kimutai [2016/07/08]
 */
public class PatientStats {
    private int totalCount;
    private int maleCount;
    private int femaleCount;
    private int maleCountPercent;
    private int femaleCountPercent;

    public PatientStats(int maleCount, int femaleCount, int totalCount) {
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.totalCount = totalCount;

        if ((totalCount > 0) && (maleCount > 0)) {
            maleCountPercent = (maleCount * 100) / totalCount;
        }

        if ((totalCount > 0) && (femaleCount > 0)) {
            femaleCountPercent = (femaleCount * 100) / totalCount;
        }

    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public int getFemaleCountPercent() {
        return femaleCountPercent;
    }

    public void setFemaleCountPercent(int femaleCountPercent) {
        this.femaleCountPercent = femaleCountPercent;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getMaleCountPercent() {
        return maleCountPercent;
    }

    public void setMaleCountPercent(int maleCountPercent) {
        this.maleCountPercent = maleCountPercent;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalCountInfo() {
        return totalCount + " PATIENTS";
    }

    public String getMaleCountInfo() {
        return maleCount + " MALE";
    }

    public String getFemaleCountInfo() {
        return femaleCount + " FEMALE";
    }

    public String getMaleCountPercentInfo() {
        return maleCountPercent + " %";
    }

    public String getFemaleCountPercentInfo() {
        return femaleCountPercent + " %";
    }

    @Override
    public String toString() {
        return getTotalCountInfo() + "," + getMaleCountInfo() + " (" + getMaleCountPercentInfo() + ")," + getFemaleCountInfo() + " (" + getFemaleCountPercentInfo() + ")";
    }
}
