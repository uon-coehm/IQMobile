package com.thepalladiumgroup.iqm.common;

import android.widget.DatePicker;

import com.thepalladiumgroup.iqm.core.model.AgeUnit;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientAgeDetail;
import com.thepalladiumgroup.iqm.presentation.view.IDemographicView;

import org.joda.time.LocalDate;
import org.joda.time.Period;

/**
 * Created by Koske Kimutai [2016/05/06]
 */
public class OnDobChangedEvent extends OnDateChangedEvent {

    private final IDemographicView view;

    private Patient patient;
    private PatientAgeDetail patientAgeDetail;


    public OnDobChangedEvent(IDemographicView view) {
        this.view = view;
        setMaxDate(new LocalDate().minusDays(1));
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        super.onDateChanged(datePicker, year, monthOfYear, dayOfMonth);

        LocalDate dob = new LocalDate(year, monthOfYear + 1, dayOfMonth);

        patient = view.getDemographics();
        patient.setDob(dob.toDate());

        patientAgeDetail = new PatientAgeDetail();


        Period period = patient.getAgeInDetail();
        if (period.getYears() == 0) {
            if (period.getMonths() == 0) {
                patientAgeDetail.setAgeUnit(AgeUnit.DAYS);
                patientAgeDetail.setAge(period.getDays());

            } else {
                patientAgeDetail.setAgeUnit(AgeUnit.MONTHS);
                patientAgeDetail.setAge(period.getMonths());
            }
        } else {
            if (patient.isInfant()) {
                patientAgeDetail.setAgeUnit(AgeUnit.MONTHS);
                patientAgeDetail.setAge(patient.getCurrentAgeInMonths());
            } else {
                patientAgeDetail.setAgeUnit(AgeUnit.YEARS);
                patientAgeDetail.setAge(period.getYears());
            }
        }

        view.updatePatientAge(patientAgeDetail);

    }
}
