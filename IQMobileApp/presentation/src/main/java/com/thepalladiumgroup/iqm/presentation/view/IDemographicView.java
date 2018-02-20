package com.thepalladiumgroup.iqm.presentation.view;


import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientAgeDetail;
import com.thepalladiumgroup.iqm.presentation.presenter.IDemographicPresenter;


/**
 * Created by Koske Kimutai [2016/04/16]
 */
public interface IDemographicView {

    void setPresenter(IDemographicPresenter presenter);
    IRegistrationView getRegistrationData();
    Patient getDemographics();
    void setDemographics(Patient demographics);

    PatientAgeDetail getPatientAgeDetail();
    boolean inEditMode();
    boolean canAgeUpdateDob();
    void allowAgeUpdateDob(boolean allow);
    boolean viewIsValid();

    void initialize();
    void serializeView();

    void updatePatientAge(PatientAgeDetail patientAgeDetail);

    void updatePatientDob(PatientAgeDetail patientAgeDetail);
    void updateRegistrationData();
}
