package com.thepalladiumgroup.iqm.presentation.presenter;

import com.thepalladiumgroup.iqm.presentation.view.IPatientListView;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public interface IPatientListPresenter {
    IPatientListView getView();

    void loadPatientList();

    void deletePatient(int id);

    void deleteSelectedPatient();
}
