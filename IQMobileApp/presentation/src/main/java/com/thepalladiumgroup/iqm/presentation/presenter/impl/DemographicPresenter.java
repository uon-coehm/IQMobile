package com.thepalladiumgroup.iqm.presentation.presenter.impl;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thepalladiumgroup.iqm.core.model.AgeUnit;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientAgeDetail;
import com.thepalladiumgroup.iqm.presentation.presenter.IDemographicPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IDemographicView;

import org.joda.time.LocalDate;

import java.util.Date;


/**
 * Created by Koske Kimutai [2016/04/16]
 */
public class DemographicPresenter implements IDemographicPresenter {
    private final Gson gson;
    private final IDemographicView view;

    public DemographicPresenter(IDemographicView view) {
        this.view = view;
        this.view.setPresenter(this);
        gson = Converters.registerAll(new GsonBuilder()).create();
    }

    @Override
    public IDemographicView getView() {
        return view;
    }
    @Override
    public String getJsonView() {
        String currentPatientJson = gson.toJson(getView().getDemographics());
        return currentPatientJson;
    }

    @Override
    public void calculateDob(int age) {
        PatientAgeDetail ageDetail = getView().getPatientAgeDetail();
        ageDetail.setAge(age);

        Date dob = new Date();
        Patient patient = getView().getDemographics();

        if (ageDetail.getAgeUnit() == AgeUnit.YEARS) {
            dob = patient.calculateEstimatedDobFromAgeInYrs(ageDetail.getAge());
        }
        if (ageDetail.getAgeUnit() == AgeUnit.MONTHS) {
            dob = patient.calculateDobFromAgeInMnths(ageDetail.getAge());
        }
        if (ageDetail.getAgeUnit() == AgeUnit.DAYS) {
            dob = patient.calculateDobFromAgeInDays(ageDetail.getAge());
        }

        ageDetail.setBirthDate(new LocalDate(dob));
        getView().updatePatientDob(ageDetail);
    }
    @Override
    public void calculateDobByUnit() {
        PatientAgeDetail ageDetail = getView().getPatientAgeDetail();
        calculateDob(ageDetail.getAge());
    }
    @Override
    public void loadDemographics() {
        if (view.inEditMode()) {
            Patient patient = view.getRegistrationData().getPatientForEdit();
            view.setDemographics(patient);
        }
    }

}
