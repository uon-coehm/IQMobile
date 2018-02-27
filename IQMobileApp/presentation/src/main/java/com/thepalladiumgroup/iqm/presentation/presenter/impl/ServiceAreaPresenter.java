package com.thepalladiumgroup.iqm.presentation.presenter.impl;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.model.ErrorMessage;
import com.thepalladiumgroup.iqm.core.model.Lookup;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.User;
import com.thepalladiumgroup.iqm.core.services.ILookupService;
import com.thepalladiumgroup.iqm.core.services.IPatientService;
import com.thepalladiumgroup.iqm.core.services.impl.LookupService;
import com.thepalladiumgroup.iqm.core.services.impl.PatientService;
import com.thepalladiumgroup.iqm.presentation.presenter.IServiceAreaPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IServiceAreaView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/18]
 */
public class ServiceAreaPresenter implements IServiceAreaPresenter {
    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(ServiceAreaPresenter.class);
    private final Gson gson;
    private final int IDTYPE_LOOKUP = 90004;
    private final ILookupService lookupService;
    private User user;
    private IPatientService patientService;
    private IServiceAreaView view;

    public ServiceAreaPresenter(IServiceAreaView view, IApplicationManager applicationManager) {
        this.view = view;
        this.view.setPresenter(this);
        gson = Converters.registerAll(new GsonBuilder()).create();
        patientService = new PatientService(applicationManager);
        lookupService = new LookupService(applicationManager);
        try {
            user = applicationManager.getCurrentUser();
        } catch (Exception e) {
            SLF_LOGGER.debug("Error loading user:", e);
        }
    }

    @Override
    public IServiceAreaView getView() {
        return view;
    }

    @Override
    public boolean dataIsValid() {
        return getView().viewIsValid();
    }

    @Override
    public String getJsonView() {
        String currentPatientJson = gson.toJson(getView().getServciceArea());
        return currentPatientJson;
    }

    @Override
    public boolean savePatient() {
        boolean savedOk = false;
        SLF_LOGGER.debug("Saving PATIENT...");

        try {
            Patient patientToSave = getView().getServciceArea();
            patientToSave.checkdobIsEstimated();
            if (user != null) {
                //patientToSave.setUserid(user.getId()); //Save IQCare ID rather than UserId
                patientToSave.setUserid(user.getIqcareid());
            }
            if (view.inEditMode()) {
                savedOk = patientService.update(patientToSave);
                SLF_LOGGER.debug("UPDATED PATIENT");
            } else {
                savedOk = patientService.Save(patientToSave);
                SLF_LOGGER.debug("ADDED NEW PATIENT");
            }
            SLF_LOGGER.debug("SAVED PATIENT!");
        } catch (SQLException e) {

            view.setViewErrors(ErrorMessage.getDatabaseError(e));
            SLF_LOGGER.debug(e.getStackTrace().toString());
        }
        return savedOk;
    }

    @Override
    public void loadLookups() {
        List<Lookup> lookups = null;

        try {
            lookups = lookupService.getLookupsByCodeId(IDTYPE_LOOKUP);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getView().setLookups(lookups);
    }

    @Override
    public void loadPartners() {
        Patient current = null;
        try {
            current = getView().getServciceArea();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (current == null) {
            return;
        }
        if (current.isChild()) {
            return;
        }

        List<Patient> patients = new ArrayList<>();
        List<Patient> existingPatients = new ArrayList<>();
        Patient blank = new Patient();
        blank.setId(-1);
        blank.setFirstname("No partner");
        blank.setMiddlename("");
        blank.setLastname("");
        patients.add(blank);

        try {
            existingPatients = patientService.getAllDemographics();
            for (Patient p : existingPatients) {
                if (current != null) {
                    if (current.getId() != p.getId()) {
                        patients.add(p);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getView().setPartnersList(patients);
    }

    @Override
    public void loadServiceArea() {
        if (view.inEditMode()) {
            Patient patient = view.getRegistrationData().getPatientForEdit();
            view.setServciceArea(patient);
        }
    }

    @Override
    public void saveServiceArea() {

    }
}
