package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public class ActiveSession extends Entity {

    @DatabaseField
    String activeActivity;
    @DatabaseField
    String activeKey;
    @DatabaseField
    String activeValue;
    @DatabaseField
    String activePatientId;
    @DatabaseField
    Date activeTime;

    public ActiveSession() {
    }

    public ActiveSession(String activeActivity) {
        this.activeActivity = activeActivity;
        this.activePatientId = activePatientId;
    }

    public ActiveSession(String activeActivity, String activeKey, String activeValue) {
        this.activeActivity = activeActivity;
        this.activeKey = activeKey;
        this.activeValue = activeValue;
        this.activeTime = new Date();
    }

    public String getActiveActivity() {
        return activeActivity;
    }

    public void setActiveActivity(String activeActivity) {
        this.activeActivity = activeActivity;
    }

    public String getActivePatientId() {
        return activePatientId;
    }

    public void setActivePatientId(String activePatientId) {
        this.activePatientId = activePatientId;
    }

    public String getActiveKey() {
        return activeKey;
    }

    public void setActiveKey(String activeKey) {
        this.activeKey = activeKey;
    }

    public String getActiveValue() {
        return activeValue;
    }

    public void setActiveValue(String activeValue) {
        this.activeValue = activeValue;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public void createSession(String activeKey, String activeValue) {
        setActiveKey(activeKey);
        setActiveValue(activeValue);
    }

    public String getActiveTimeString() {
        DateTime dt = new DateTime(getActiveTime());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM yyyy HH:mm:ss");
        return fmt.print(dt);
    }

    @Override
    public String toString() {
        return "ActiveSession[Patient:" + activePatientId + "," + activeActivity + "." + activeKey + "]={" + activeValue + "} ," + getActiveTimeString();
    }
}
