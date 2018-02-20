package com.thepalladiumgroup.iqm.presentation.presenter;

import com.thepalladiumgroup.iqm.presentation.view.IFindAddPatientView;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public interface IFindAddPatientPresenter {
    IFindAddPatientView getView();

    void loadPatientList();

    void deletePatient(int id);

    void deleteSelectedPatient();
}
