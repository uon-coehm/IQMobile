package com.thepalladiumgroup.iqm.presentation.presenter;


import com.thepalladiumgroup.iqm.presentation.view.IDeviceView;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public interface IDevicePresenter {
    IDeviceView getView();

    void loadDeviceInfo();

    void saveDeviceInfo();
}
