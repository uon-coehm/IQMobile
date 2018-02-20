package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
public abstract class Entity {

    //@SerializedName("Id")
    @DatabaseField(generatedId = true)
    private int id;
    //@SerializedName("UuId")
    @DatabaseField
    private String uuid;
    //@SerializedName("IqcareId")
    @DatabaseField
    private int iqcareid;
    //@SerializedName("SyncState")
    @DatabaseField
    private int syncstate;
    //@SerializedName("UpdateDate")
    @DatabaseField
    private Date updatedate;
    //@SerializedName("UserId")
    @DatabaseField
    private int userid;

    public Entity() {
        this.uuid = UUID.randomUUID().toString();
        this.syncstate = 0;
        this.updatedate = new DateTime().toDate();
        this.userid = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getIqcareid() {
        return iqcareid;
    }

    public void setIqcareid(int iqcareid) {
        this.iqcareid = iqcareid;
    }

    public int getSyncstate() {
        return syncstate;
    }

    public void setSyncstate(int syncstate) {
        this.syncstate = syncstate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void prepareUpdate() {
        this.syncstate = 0;
        this.updatedate = new Date();
    }

    public void prepareUpdate(int userid) {
        this.userid = userid;
        prepareUpdate();
    }

    public boolean isNotNullorEmpty(String string) {
        return string != null && !string.isEmpty() && !string.trim().isEmpty();
    }

    @Override
    public boolean equals(Object ob) {
        if (ob == null) {
            return false;
        }
        if (this == ob) {
            return true;
        }
        if (ob instanceof Entity) {
            Entity other = (Entity) ob;
            return this.getUuid().equalsIgnoreCase(other.getUuid());
        }
        return false;
    }
}
