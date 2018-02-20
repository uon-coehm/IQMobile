package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public class Device extends Entity {

    @DatabaseField
    private String serial;
    @DatabaseField
    private String code;
    @DatabaseField
    private String facility;
    @DatabaseField
    private int facilitycode;
    @DatabaseField
    private boolean defaultsite;

    public Device() {
    }

    public Device(String serial, String code, int facilitycode, String facility) {
        this.serial = serial;
        this.code = code;
        this.facility = facility;
        this.facilitycode = facilitycode;
        this.defaultsite = true;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public int getFacilitycode() {
        return facilitycode;
    }

    public void setFacilitycode(int facilitycode) {
        this.facilitycode = facilitycode;
    }

    public boolean isDefaultsite() {
        return defaultsite;
    }

    public void setDefaultsite(boolean defaultsite) {
        this.defaultsite = defaultsite;
    }
}
