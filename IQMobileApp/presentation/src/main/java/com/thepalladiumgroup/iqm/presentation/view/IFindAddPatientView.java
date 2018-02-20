package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IFindAddPatientPresenter;

import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public interface IFindAddPatientView {

    void setPresenter(IFindAddPatientPresenter presenter);

    List<Patient> getPatients();

    void setPatients(List<Patient> patients);

    Patient getSelectedPatient();

    List<Patient> getSelectedPatients();

    void initialize();

    void loadPatientHome();

    void deletePatient();
}
