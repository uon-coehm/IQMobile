package com.thepalladiumgroup.iqm.presentation.presenter;

import com.thepalladiumgroup.iqm.presentation.view.IRegistrationView;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public interface IRegistrationPresenter {

    IRegistrationView getView();

    void loadPatientForEdit(int patientid);
}
