package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.Server;
import com.thepalladiumgroup.iqm.presentation.presenter.ISyncPresenter;


/**
 * Created by Koske Kimutai [2016/04/18]
 */
public interface ISyncView {

    void setPresenter(ISyncPresenter presenter);

    Server getServer();

    void setServer(Server server);

    void showAppSettingsProgress(boolean show);

    void showAppDataProgress(boolean show);

    void setAppSettingsProgress(int percentage);

    void setAppDataProgress(int percentage);

    void setBusy(boolean isbusy);

    void initialize();

    void setViewAppSettingsStatus(String status);

    void setViewAppSettingsErrors(String error);

    void setViewAppDataStatus(String status);

    void setViewAppDataErrors(String error);
}
