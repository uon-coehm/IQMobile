package com.thepalladiumgroup.iqm.core.model;

/**
 * Created by Koske Kimutai [2016/05/03]
 */
public class ParentCondition {
    private int conceptId;
    private String condition;

    public ParentCondition(int conceptId, String condition) {
        this.conceptId = conceptId;
        this.condition = condition;
    }

    public int getConceptId() {
        return conceptId;
    }

    public void setConceptId(int conceptId) {
        this.conceptId = conceptId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
