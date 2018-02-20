package com.thepalladiumgroup.iqm.presentation.presenter.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.services.IPatientService;
import com.thepalladiumgroup.iqm.core.services.impl.PatientService;
import com.thepalladiumgroup.iqm.presentation.presenter.IPatientListPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IPatientListView;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public class PatientListPresenter implements IPatientListPresenter {
    private IPatientService patientService;
    private IPatientListView view;

    public PatientListPresenter(IPatientListView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        this.patientService = new PatientService(applicationManager);
    }

    @Override
    public IPatientListView getView() {
        return view;
    }

    @Override
    public void loadPatientList() {
        List<Patient> patients = null;
        try {
            patients = patientService.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getView().setPatients(patients);
    }

    @Override
    public void deletePatient(int id) {
        try {
            patientService.Delete(id);
            loadPatientList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSelectedPatient() {

    }
}
