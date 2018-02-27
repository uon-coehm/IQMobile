package com.thepalladiumgroup.iqm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.EncounterType;
import com.thepalladiumgroup.iqm.core.model.Module;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IPatientManagerPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.PatientManagerPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IPatientManagerView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PateintManagerActivity extends AppCompatActivity implements IPatientManagerView {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(PateintManagerActivity.class);

    IPatientManagerPresenter presenter;
    List<Module> modules;
    List<Encounter> encounters;
    List<EncounterType> encounterTypes;
    private int patientid;
    private Patient patient;
    private Module module;
    private EncounterType encounterType;
    private Encounter encounter;

    private TextView mFullName;
    private TextView mSex;
    private TextView mDob;
    private TextView mAge;
    private TextView mError;
    private TextView mClientcode;
    private TextView mModule;
    private TextView mEncounterType;
    private TextView mEncounterDetails;
    private Button mHTS;
    private Button mNewEncounter;
    private Button mContinueEncounter;
    //private Button mSendEncounter;
    private Button mReviewEncounter;

    @Override
    public void setPresenter(IPatientManagerPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<Module> getModules() {
        return modules;
    }

    @Override
    public void setModules(List<Module> modules) {
        displayModules(modules);
    }

    @Override
    public List<EncounterType> getEncounterTypes() {
        return encounterTypes;
    }

    @Override
    public void setEncounterTypes(List<EncounterType> encounterTypes) {
        displayEncounterTypes(encounterTypes);
    }

    @Override
    public List<Encounter> getEncounters() {
        return encounters;
    }

    @Override
    public void setEncounters(List<Encounter> encounters) {
        displayEncounters(encounters);
    }

    @Override
    public Patient getPatient() {
        return patient;
    }

    @Override
    public void setPatient(Patient patient) {
        setCurrentView(patient);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pateint_manager);
        initialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        finish();
    }

    @Override
    public void initialize() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.patient_home_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_patient_home));


        Bundle extras = getIntent().getExtras();
        mFullName = (TextView) findViewById(R.id.textViewDashFullName);
        mSex = (TextView) findViewById(R.id.textViewDashSex);
        mDob = (TextView) findViewById(R.id.textViewDashDob);
        mAge = (TextView) findViewById(R.id.textViewDashAge);
        mError = (TextView) findViewById(R.id.textViewDashError);
        mClientcode = (TextView) findViewById(R.id.textViewDashClientCode);
        mModule = (TextView) findViewById(R.id.textViewModule);
        mEncounterType = (TextView) findViewById(R.id.textViewEncounterType);
        mEncounterDetails = (TextView) findViewById(R.id.textViewEncounterDetials);

        mHTS = (Button) findViewById(R.id.buttonNewRecord);
        mNewEncounter = (Button) findViewById(R.id.buttonNewRecord);
        mContinueEncounter = (Button) findViewById(R.id.buttonContinueRecord);
        mReviewEncounter = (Button) findViewById(R.id.buttonReviewRecord);
        //mSendEncounter = (Button) findViewById(R.id.buttonSendRecord);
        if (extras != null) {
            patientid = extras.getInt("patient_id");
        }
        presenter = new PatientManagerPresenter(this, (IQMobileApplication) getApplication());
        presenter.loadPatient(patientid);
        presenter.loadModules();
    }


    private void setCurrentView(Patient patient) {
        this.patient = patient;
        mFullName.setText(this.patient.getFullName());
        mSex.setText(this.patient.getGender());
        mDob.setText(this.patient.getDobString());
        mAge.setText(" [" + this.patient.getCurrentAgeString() + "]");
        mClientcode.setText(String.valueOf(this.patient.getClientcode()));
    }

    private void displayModules(List<Module> modules) {
        this.modules = modules;
        module = modules.get(0);
        mModule.setText(module.getDisplay());
        mModule.setTag(module.getId());
    }

    private void displayEncounterTypes(List<EncounterType> encounterTypes) {
        this.encounterTypes = encounterTypes;
        encounterType = encounterTypes.get(0);
        mEncounterType.setText(encounterType.getDisplay());
        mEncounterType.setTag(encounterType.getId());
    }

    private void displayEncounters(List<Encounter> encounters) {
        this.encounters = encounters;
        if (encounters.size() == 0) {
            encounter = new Encounter();

            mNewEncounter.setTag(0);
            mEncounterDetails.setText("");

            mNewEncounter.setEnabled(true);
            mContinueEncounter.setEnabled(false);
            mReviewEncounter.setEnabled(false);
            //mSendEncounter.setEnabled(false);

        } else {
            encounter = encounters.get(0);
            if (encounter.isCompleted()) {
                mNewEncounter.setEnabled(false);
                mContinueEncounter.setEnabled(false);
                mReviewEncounter.setEnabled(true);
                //mSendEncounter.setEnabled(true);
            } else {


                mNewEncounter.setTag(encounter.getId());

                mEncounterDetails.setText(encounter.entryDetail());

                mNewEncounter.setEnabled(false);
                mContinueEncounter.setEnabled(true);
                mReviewEncounter.setEnabled(false);
                //mSendEncounter.setEnabled(false);
            }

        }
        mEncounterType.setText(encounterTypes.get(0).getDisplay());
    }
    @Override
    public void setViewErrors(String error) {
        mError.setText("");
        if (error.length() != 0) {
            mError.setText(error);
        }
    }

    public void onClickRegistration(View view) {
        SLF_LOGGER.debug("Open Registration Info");
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("active_patient_id", patient.getId());
        startActivity(intent);
        finish();
    }

    public void onClickNewRecord(View view) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        TransactionTime.StartTime = dateFormat.format(date);

        Intent intent = new Intent(this, ObservationActivity.class);
        intent.putExtra("active_patient_id", patient.getId());
        intent.putExtra("encounter_type_id", encounterType.getId());
        intent.putExtra("encounter_id", encounter.getId());
        intent.putExtra("mode_new", true);
        intent.putExtra("mode_continue", false);
        intent.putExtra("mode_review", false);
        startActivity(intent);
        finish();
    }

    public void onClickContinueRecord(View view) {
        mContinueEncounter.setEnabled(false);
        Intent intent = new Intent(this, ObservationActivity.class);
        intent.putExtra("active_patient_id", patient.getId());
        intent.putExtra("encounter_type_id", encounterType.getId());
        intent.putExtra("encounter_id", encounter.getId());
        intent.putExtra("mode_new", false);
        intent.putExtra("mode_continue", true);
        intent.putExtra("mode_review", false);
        startActivity(intent);
        mContinueEncounter.setEnabled(true);
        finish();
    }

    public void onClickReviewRecord(View view) {
        Intent intent = new Intent(this, ObservationActivity.class);
        intent.putExtra("active_patient_id", patient.getId());
        intent.putExtra("encounter_type_id", encounterType.getId());
        intent.putExtra("encounter_id", encounter.getId());
        intent.putExtra("mode_new", false);
        intent.putExtra("mode_continue", false);
        intent.putExtra("mode_review", true);
        startActivity(intent);
        finish();
    }
}
