package com.thepalladiumgroup.iqm;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.thepalladiumgroup.iqm.common.OnDobChangedEvent;
import com.thepalladiumgroup.iqm.core.model.AgeUnit;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientAgeDetail;
import com.thepalladiumgroup.iqm.presentation.presenter.IDemographicPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.DemographicPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IDemographicView;
import com.thepalladiumgroup.iqm.presentation.view.IRegistrationView;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.layouts.BasicWizardLayout;
import org.codepond.wizardroid.persistence.ContextVariable;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

public class DemographicForm extends WizardStep implements IDemographicView {


    //region fields
    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(DemographicForm.class);

    private IDemographicPresenter presenter;
    private IRegistrationView mRegistration;

    private Activity mActivity;
    private BasicWizardLayout mWizard;
    private View currentView;
    private Context currentContex;

    private Patient demographics;
    private PatientAgeDetail patientAgeDetail;
    //endregion

    //region controls
    private TextView mId;
    @NotEmpty(messageId = R.string.validation_first_name, order = 1)
    private EditText mFName;
    private EditText mMName;
    @NotEmpty(messageId = R.string.validation_last_name, order = 2)
    private EditText mLname;

    private RadioButton mMale;
    private RadioButton mFemale;

    private EditText mAge;
    private RadioButton mAgeYrs;
    private RadioButton mAgeMnths;
    private RadioButton mAgeDays;

    private DatePicker mDob;
    //endregion

    @ContextVariable
    private String currentPatientJson;



    @Override
    public void setPresenter(IDemographicPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public IRegistrationView getRegistrationData() {
        return mRegistration;
    }

    @Override
    public Patient getDemographics() {
        return getCurrentView();
    }

    @Override
    public void setDemographics(Patient demographics) {
        setCurrentView(demographics);
    }

    @Override
    public PatientAgeDetail getPatientAgeDetail() {
        return getCurrentAgeDetailView();
    }

    @Override
    public boolean inEditMode() {
        return mRegistration.inEditMode();
    }

    @Override
    public boolean canAgeUpdateDob() {
        return mAge.hasFocus();
    }

    @Override
    public void allowAgeUpdateDob(boolean allow) {
        mAge.setTag("0");
        if (allow) {
            mAge.setTag("1");
        }
    }

    @Override
    public boolean isValidPage() {
        return viewIsValid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_demographic_form, container, false);
        currentContex = getActivity().getApplicationContext();
        mWizard = (BasicWizardLayout) this.getParentFragment();
        initialize();
        return currentView;
    }


    @Override
    public void initialize() {

        //mRegistration.setSubtitle(mActivity.getString(R.string.title_demographics));

        mId = (TextView) currentView.findViewById(R.id.textViewDemoPatientId);
        mFName = (EditText) currentView.findViewById(R.id.editTextDemoPatientFName);
        mMName = (EditText) currentView.findViewById(R.id.editTextDemoPatientMName);
        mLname = (EditText) currentView.findViewById(R.id.editTextDemoPatientLName);
        mDob = (DatePicker) currentView.findViewById(R.id.datePickerDemoPatientDob);
        mAge = (EditText) currentView.findViewById(R.id.editTextAge);
        mMale = (RadioButton) currentView.findViewById(R.id.radioButtonMale);
        mFemale = (RadioButton) currentView.findViewById(R.id.radioButtonFemale);

        mAgeYrs = (RadioButton) currentView.findViewById(R.id.radioButtonAgeYrs);
        mAgeMnths = (RadioButton) currentView.findViewById(R.id.radioButtonAgeMonths);
        mAgeDays = (RadioButton) currentView.findViewById(R.id.radioButtonAgeDays);

        setDefaults();

        presenter = new DemographicPresenter(this);
        presenter.loadDemographics();
    }

    @Override
    public boolean viewIsValid() {

        boolean valid = false;
        if (this != null) {
            valid = FormValidator.validate(this, new SimpleErrorPopupCallback(this.getContext()));
        }
        return valid;
    }

    @Override
    public void serializeView() {
        SLF_LOGGER.debug("creating Demographics json...");
        currentPatientJson = presenter.getJsonView();
        SLF_LOGGER.debug("Demographics json:" + currentPatientJson);
    }

    @Override
    public void updatePatientAge(PatientAgeDetail patientAgeDetail) {
        setCurrentAgeView(patientAgeDetail);
    }

    @Override
    public void updatePatientDob(PatientAgeDetail patientAgeDetail) {
        setCurrentDobView(patientAgeDetail);
    }

    @Override
    public void updateRegistrationData() {
        SLF_LOGGER.debug("editMode Demographics");
        demographics.updateDemographics(getDemographics());
        mRegistration.editPatient(demographics);
    }


    private void setDefaults() {

        allowAgeUpdateDob(true);

        mAge.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    int age = Integer.parseInt(s.toString());
                    if (age > 110) {
                        s.clear();
                        s.insert(0, "110");
                    }
                    if (canAgeUpdateDob()) {
                        presenter.calculateDob(age);
                    }
                }
            }
        });


        mAgeYrs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //mAgeYrs.setChecked(false);
                    mAgeMnths.setChecked(false);
                    mAgeDays.setChecked(false);
                    mAge.requestFocus();
                    presenter.calculateDobByUnit();
                }
            }
        });
        mAgeMnths.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAgeYrs.setChecked(false);
                    //mAgeMnths.setChecked(false);
                    mAgeDays.setChecked(false);
                    mAge.requestFocus();
                    presenter.calculateDobByUnit();
                }
            }
        });
        mAgeDays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAgeYrs.setChecked(false);
                    mAgeMnths.setChecked(false);
                    //mAgeDays.setChecked(false);
                    mAge.requestFocus();
                    presenter.calculateDobByUnit();

                }

            }
        });

        OnDobChangedEvent dobChangedEvent = new OnDobChangedEvent(this);
        mDob.init(dobChangedEvent.getMaxDate().getYear(), dobChangedEvent.getMaxDate().getMonthOfYear() - 1, dobChangedEvent.getMaxDate().getDayOfMonth(), dobChangedEvent);

    }

    private Patient getCurrentView() {
        //  16-Male 17-Female
        Integer sex = mMale.isChecked() == true ? 16 : 17;
        PatientAgeDetail ageDetail = getPatientAgeDetail();
        LocalDate birthDate = ageDetail.getBirthDate();

        demographics = new Patient(
                mFName.getText().toString(),
                mMName.getText().toString(),
                mLname.getText().toString(),
                sex,
                birthDate.toDate()
        );
        demographics.setId(Integer.parseInt(mId.getText().toString()));
        if (mId.getTag() != null) {
            demographics.setUuid(mId.getTag().toString());
        }

        return demographics;
    }

    private void setCurrentView(Patient demographics) {
        this.demographics = demographics;
        mId.setText(String.valueOf(this.demographics.getId()));
        mId.setTag(this.demographics.getUuid());
        mFName.setText(this.demographics.getFirstname());
        mMName.setText(this.demographics.getMiddlename());
        mLname.setText(this.demographics.getLastname());

        if (this.demographics.getDob() != null) {
            mDob.updateDate(this.demographics.getDobLocal().getYear(), this.demographics.getDobLocal().getMonthOfYear() - 1, this.demographics.getDobLocal().getDayOfMonth());
        }

        if (this.demographics.isMale()) {
            mMale.setChecked(true);
            mFemale.setChecked(false);
        } else {
            mFemale.setChecked(true);
            mMale.setChecked(false);
        }
    }
    private PatientAgeDetail getCurrentAgeDetailView() {
        int age = 0;
        if (mAge.getText().toString().length() > 0) {
            age = Integer.parseInt(mAge.getText().toString());
        }
        patientAgeDetail = new PatientAgeDetail();

        patientAgeDetail.setAge(age);
        if (mAgeYrs.isChecked()) {
            patientAgeDetail.setAgeUnit(AgeUnit.YEARS);
        }
        if (mAgeMnths.isChecked()) {
            patientAgeDetail.setAgeUnit(AgeUnit.MONTHS);
        }
        if (mAgeDays.isChecked()) {
            patientAgeDetail.setAgeUnit(AgeUnit.DAYS);
        }
        patientAgeDetail.setBirthDate(new LocalDate(mDob.getYear(), mDob.getMonth() + 1, mDob.getDayOfMonth()));

        return patientAgeDetail;
    }
    private void setCurrentDobView(PatientAgeDetail patientAgeDetail) {
        this.patientAgeDetail = patientAgeDetail;
        mDob.updateDate(this.patientAgeDetail.getBirthDate().getYear(), this.patientAgeDetail.getBirthDate().getMonthOfYear() - 1, this.patientAgeDetail.getBirthDate().getDayOfMonth());
    }
    private void setCurrentAgeView(PatientAgeDetail patientAgeDetail) {
        this.patientAgeDetail = patientAgeDetail;
        if (!canAgeUpdateDob()) {

            mAge.setText(String.valueOf(this.patientAgeDetail.getAge()));

            if (this.patientAgeDetail.getAgeUnit() == AgeUnit.YEARS) {
                mAgeYrs.setChecked(true);
                mAgeMnths.setChecked(false);
                mAgeDays.setChecked(false);
            }
            if (this.patientAgeDetail.getAgeUnit() == AgeUnit.MONTHS) {

                mAgeMnths.setChecked(true);
                mAgeDays.setChecked(false);
                mAgeYrs.setChecked(false);
            }
            if (this.patientAgeDetail.getAgeUnit() == AgeUnit.DAYS) {
                mAgeDays.setChecked(true);
                mAgeYrs.setChecked(false);
                mAgeMnths.setChecked(false);

            }
            mDob.requestFocus();

        }

    }


    @Override
    public void onExit(int exitCode) {

        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                if (inEditMode()) {
                    updateRegistrationData();
                } else {
                    serializeView();
                }
                break;
            case WizardStep.EXIT_PREVIOUS:
                //Do nothing...
                break;
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mRegistration = (IRegistrationView) mActivity;
    }
    @Override
    public void onStop() {
        super.onStop();
        FormValidator.stopLiveValidation(this);
    }
}
