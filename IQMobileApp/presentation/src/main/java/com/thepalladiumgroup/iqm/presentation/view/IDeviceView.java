package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.Device;
import com.thepalladiumgroup.iqm.presentation.presenter.IDevicePresenter;


/**
 * Created by Koske Kimutai [2016/04/12]
 */
public interface IDeviceView {

    IDevicePresenter getPresenter();

    void setPresenter(IDevicePresenter presenter);

    Device getDevice();

    void setDevice(Device device);

    void initialize();

    void allowedit(boolean state);
}
