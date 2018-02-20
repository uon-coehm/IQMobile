package com.thepalladiumgroup.iqm.presentation.view;

import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IPatientListPresenter;

import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public interface IPatientListView {

    void setPresenter(IPatientListPresenter presenter);

    List<Patient> getPatients();

    void setPatients(List<Patient> patients);

    Patient getSelectedPatient();

    List<Patient> getSelectedPatients();

    void initialize();
}
