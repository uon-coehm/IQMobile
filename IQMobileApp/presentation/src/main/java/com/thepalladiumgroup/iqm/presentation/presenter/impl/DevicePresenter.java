package com.thepalladiumgroup.iqm.presentation.presenter.impl;


import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.Device;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.IDeviceService;
import com.thepalladiumgroup.iqm.core.services.impl.DeviceService;
import com.thepalladiumgroup.iqm.presentation.presenter.IDevicePresenter;
import com.thepalladiumgroup.iqm.presentation.view.IDeviceView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


/**
 * Created by Koske Kimutai [2016/04/15]
 */
public class DevicePresenter implements IDevicePresenter {
    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(DevicePresenter.class);
    private User user;
    private IDeviceView view;
    private IDeviceService deviceService;

    public DevicePresenter(IDeviceView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        this.deviceService = new DeviceService(applicationManager);
        try {
            user = applicationManager.getCurrentUser();
        } catch (Exception e) {
            SLF_LOGGER.debug("Error loading user:", e);
        }
    }

    @Override
    public IDeviceView getView() {
        return view;
    }

    @Override
    public void loadDeviceInfo() {
        Device device = null;
        try {
            device = deviceService.getDevice();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (device != null) {
            getView().setDevice(device);
            getView().allowedit(false);
        }
    }

    @Override
    public void saveDeviceInfo() {
        Device device = view.getDevice();
        if (device != null) {

            if (user != null) {
                device.setUserid(user.getId());
            }
            try {
                deviceService.updateDevice(device);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadDeviceInfo();
        }
    }
}
