package com.thepalladiumgroup.iqm.presentation.presenter;


import com.thepalladiumgroup.iqm.presentation.view.IMainView;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public interface IMainPresenter {
    IMainView getView();

    void cleanUp();
    void loadCurrentUser();

    void loadPatientStats();

    void loadRecordStats();
}
