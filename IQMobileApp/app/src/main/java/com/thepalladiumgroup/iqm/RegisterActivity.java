package com.thepalladiumgroup.iqm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IRegistrationPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.RegistrationPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IRegistrationView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import android.support.v4.app.FragmentActivity;

public class RegisterActivity extends AppCompatActivity implements IRegistrationView {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(RegisterActivity.class);
    Patient patient;
    private IRegistrationPresenter presenter;

    @Override
    public void setSubtitle(String s) {
        getSupportActionBar().setSubtitle("   " + s);
    }

    @Override
    public void setPresenter(IRegistrationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Patient getPatientForEdit() {
        return patient;
    }

    @Override
    public void setPatientForEdit(Patient patient) {
        this.patient = patient;
    }

    @Override
    public void editPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public boolean inEditMode() {
        if (patient == null) {
            return false;
        }

        return patient.getId() > 0;
    }

    @Override
    public void setViewErrors(String s) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int patientid = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.patient_new_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_register));
        myToolbar.setLogo(R.mipmap.ic_launcher);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patientid = extras.getInt("active_patient_id");
        }
        presenter = new RegistrationPresenter(this, (IQMobileApplication) getApplication());
        presenter.loadPatientForEdit(patientid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {


            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                goBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
        //super.onBackPressed();
    }

    private void goBack() {
        Intent intent;
        if (inEditMode()) {
            SLF_LOGGER.debug("Open Pateint Home");
            intent = new Intent(this, PateintManagerActivity.class);
            intent.putExtra("patient_id", patient.getId());
        } else {
            SLF_LOGGER.debug("Open Dashboard");
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
