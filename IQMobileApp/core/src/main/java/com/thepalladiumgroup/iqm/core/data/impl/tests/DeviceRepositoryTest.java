package com.thepalladiumgroup.iqm.core.data.impl.tests;

import com.thepalladiumgroup.iqm.core.data.IDeviceRepository;
import com.thepalladiumgroup.iqm.core.data.impl.DeviceRepository;
import com.thepalladiumgroup.iqm.core.model.Device;
import com.thepalladiumgroup.iqm.core.tests.BaseCoreTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/04/14]
 */
public final class DeviceRepositoryTest extends BaseCoreTest {

    private IDeviceRepository deviceRepository;

    @Before
    public void setUp() {

        deviceRepository = new DeviceRepository(applicationManager);
    }

    @Test
    public void should_getDefualtdevice() throws SQLException {
        Device device = deviceRepository.getDeviceInfo();
        Assert.assertNotNull(device);
    }

}
