package com.thepalladiumgroup.iqm.presentation.presenter;


import android.os.AsyncTask;

import com.thepalladiumgroup.iqm.core.model.SyncAction;
import com.thepalladiumgroup.iqm.presentation.view.ISyncView;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public interface ISyncPresenter {
    ISyncView getView();

    void loadSettings();

    void saveServer();


    void syncAppSettings();

    void syncAppData();

    String getError();

    String getStatus();

    AsyncTask<SyncAction, Integer, Boolean> getUserTask();

    AsyncTask<SyncAction, Integer, Boolean> getLookupTask();

    AsyncTask<SyncAction, Integer, Boolean> getModuleTask();

    AsyncTask<SyncAction, Integer, Boolean> getConceptTask();

    AsyncTask<SyncAction, Integer, Boolean> getDataTask();
}
