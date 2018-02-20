package com.thepalladiumgroup.iqm.presentation.presenter;


import com.thepalladiumgroup.iqm.presentation.view.IPatientManagerView;

/**
 * Created by Koske Kimutai [2016/04/16]
 */
public interface IPatientManagerPresenter {
    IPatientManagerView getView();
    void loadPatient(int id);
    void loadModules();
}
