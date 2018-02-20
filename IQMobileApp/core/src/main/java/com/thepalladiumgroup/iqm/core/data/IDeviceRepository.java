package com.thepalladiumgroup.iqm.core.data;

/**
 * Created by Koske Kimutai [2016/04/19]
 */

import com.thepalladiumgroup.iqm.core.model.Device;

import java.sql.SQLException;


public interface IDeviceRepository extends IRepository<Device> {
    Device getDeviceInfo() throws SQLException;
}

