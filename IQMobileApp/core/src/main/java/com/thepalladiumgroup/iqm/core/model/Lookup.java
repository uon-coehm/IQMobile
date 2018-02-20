package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public class Lookup extends Entity implements Comparable<Lookup> {

    @DatabaseField
    private String name;
    @DatabaseField
    private int codeid;
    @DatabaseField
    private double rank;
    @DatabaseField
    private boolean isloner;
    @DatabaseField
    private boolean isother;

    private int conceptId;

    public Lookup() {
    }

    public Lookup(int iqcareid, String name, int codeid) {
        setIqcareid(iqcareid);
        this.codeid = codeid;
        this.name = name;
    }

    public Lookup(int iqcareid, String name, int codeid, String uuid) {
        setIqcareid(iqcareid);
        this.codeid = codeid;
        this.name = name;
        this.setUuid(uuid);
    }


    public int getCodeid() {
        return codeid;
    }

    public void setCodeid(int codeid) {
        this.codeid = codeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public boolean isloner() {
        return isloner;
    }

    public void setIsloner(boolean isloner) {
        this.isloner = isloner;
    }

    public boolean isother() {
        return isother;
    }

    public void setIsother(boolean isother) {
        this.isother = isother;
    }

    public int getConceptId() {
        return conceptId;
    }

    public void setConceptId(int conceptId) {
        this.conceptId = conceptId;
    }

    public String printInfo() {
        return getIqcareid() + " - " + name + "  [" + codeid + "]";
    }
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Lookup lookup) {
        return this.rank > lookup.rank ? 1 : (this.rank < lookup.rank ? -1 : 0);
    }
}
