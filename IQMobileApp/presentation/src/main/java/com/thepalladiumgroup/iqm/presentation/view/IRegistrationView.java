package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IRegistrationPresenter;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public interface IRegistrationView {

    void setSubtitle(String s);
    void setPresenter(IRegistrationPresenter presenter);

    Patient getPatientForEdit();

    void setPatientForEdit(Patient patient);

    void editPatient(Patient patient);

    boolean inEditMode();

    void setViewErrors(String s);

}
