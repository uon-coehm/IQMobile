package com.thepalladiumgroup.iqm.core.data.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IDeviceRepository;
import com.thepalladiumgroup.iqm.core.model.Device;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
public class DeviceRepository extends BaseRepository<Device> implements IDeviceRepository {

    public DeviceRepository(IApplicationManager applicationManager) {
        super(applicationManager);
    }

    @Override
    public Device getDeviceInfo() throws SQLException {
        List<Device> results = entityDao.queryForEq("defaultsite", true);

        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }
}
