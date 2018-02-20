package com.thepalladiumgroup.iqm.presentation.presenter.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.services.IPatientService;
import com.thepalladiumgroup.iqm.core.services.impl.PatientService;
import com.thepalladiumgroup.iqm.presentation.presenter.IFindAddPatientPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IFindAddPatientView;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public class FindAddPatientPresenter implements IFindAddPatientPresenter {
    private IPatientService patientService;
    private IFindAddPatientView view;

    public FindAddPatientPresenter(IFindAddPatientView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        this.patientService = new PatientService(applicationManager);
    }

    @Override
    public IFindAddPatientView getView() {
        return view;
    }

    @Override
    public void loadPatientList() {
        List<Patient> patients = null;
        try {
            patients = patientService.getAllDemographics();
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
