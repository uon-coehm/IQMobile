package com.thepalladiumgroup.iqm.core.services;

import com.thepalladiumgroup.iqm.core.model.Device;

import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
public interface IDeviceService {
    Device getDevice() throws SQLException;

    void updateDevice(Device device) throws SQLException;
}
