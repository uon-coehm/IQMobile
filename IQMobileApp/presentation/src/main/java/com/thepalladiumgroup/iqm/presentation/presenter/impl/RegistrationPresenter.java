package com.thepalladiumgroup.iqm.presentation.presenter.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.services.IPatientService;
import com.thepalladiumgroup.iqm.core.services.impl.PatientService;
import com.thepalladiumgroup.iqm.presentation.presenter.IRegistrationPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IRegistrationView;

import java.sql.SQLException;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public class RegistrationPresenter implements IRegistrationPresenter {

    private final IPatientService patientService;
    private IRegistrationView view;

    public RegistrationPresenter(IRegistrationView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        this.patientService = new PatientService(applicationManager);
    }

    @Override
    public IRegistrationView getView() {
        return view;
    }

    @Override
    public void loadPatientForEdit(int patientid) {

        if (patientid == 0) {
            return;
        }

        Patient patient = null;
        try {
            patient = patientService.findForEdit(patientid);
        } catch (SQLException e) {
            view.setViewErrors("Error loading patient data " + e.getMessage());
            e.printStackTrace();
        }
        getView().setPatientForEdit(patient);
    }
}
