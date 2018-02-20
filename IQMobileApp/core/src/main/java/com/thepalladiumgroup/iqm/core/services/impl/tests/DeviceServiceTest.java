package com.thepalladiumgroup.iqm.core.services.impl.tests;

import com.thepalladiumgroup.iqm.core.model.Device;
import com.thepalladiumgroup.iqm.core.services.IDeviceService;
import com.thepalladiumgroup.iqm.core.services.impl.DeviceService;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/04/15]
 */
public final class DeviceServiceTest extends BaseCoreTest {

    private IDeviceService deviceService;

    @Before
    public void setUp() throws SQLException {
        deviceService = new DeviceService(applicationManager);
    }

    @Test
    public void should_load_Deviceinfo() throws SQLException {
        Device device = deviceService.getDevice();
        Assert.assertNotNull(device);
    }

    @Test
    public void should_update_Device() throws SQLException {
        Device device = deviceService.getDevice();
        Assert.assertNotNull(device);
        device.setFacilitycode(11111);

        deviceService.updateDevice(device);

        Device updatedDevice = deviceService.getDevice();
        Assert.assertEquals(11111, updatedDevice.getFacilitycode());
    }


}
