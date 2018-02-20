package com.thepalladiumgroup.iqm.core.services.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IDeviceRepository;
import com.thepalladiumgroup.iqm.core.data.impl.DeviceRepository;
import com.thepalladiumgroup.iqm.core.model.Device;
import com.thepalladiumgroup.iqm.core.services.IDeviceService;

import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
public class DeviceService implements IDeviceService {

    private final IDeviceRepository deviceRepository;
    private final IApplicationManager applicationManager;


    public DeviceService(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.deviceRepository = new DeviceRepository(applicationManager);
    }

    @Override
    public Device getDevice() throws SQLException {
        return deviceRepository.getDeviceInfo();
    }

    @Override
    public void updateDevice(Device device) throws SQLException {
        deviceRepository.update(device);
    }
}
