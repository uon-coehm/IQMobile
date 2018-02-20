package com.thepalladiumgroup.iqm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IPatientListPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.PatientListPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IPatientListView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PatientListActivity extends AppCompatActivity implements IPatientListView {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(PatientListActivity.class);

    private IPatientListPresenter presenter;
    private List<Patient> patients;
    private Patient patient;
    private PatientListAdapter patientListAdapter;
    private ListView mList;

    @Override
    public void setPresenter(IPatientListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<Patient> getPatients() {
        return getCurrentListView();
    }

    @Override
    public void setPatients(List<Patient> patients) {
        setCurrentListView(patients);
    }

    @Override
    public Patient getSelectedPatient() {
        return patient;
    }

    @Override
    public List<Patient> getSelectedPatients() {
        return patients;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        initialize();
    }

    @Override
    public void initialize() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("  Patients");
        myToolbar.setLogo(R.mipmap.ic_launcher);

        mList = (ListView) findViewById(R.id.listViewPatientList);
        presenter = new PatientListPresenter(this, (IQMobileApplication) getApplication());
        presenter.loadPatientList();
    }

    private List<Patient> getCurrentListView() {
        return patients;
    }

    private void setCurrentListView(List<Patient> patients) {
        this.patients = patients;
        patientListAdapter = new PatientListAdapter(this, R.layout.patient_list, this.patients);
        mList.setAdapter(patientListAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                patient = patientListAdapter.getItem(position);
                SLF_LOGGER.debug("Patient selected" + patient.getFullName());
                loadPatient(patient);
            }
        });
    }

    private void loadPatient(Patient patientselected) {
        Intent intent = new Intent(this, PateintManagerActivity.class);
        intent.putExtra("patient_id", patientselected.getId());
        startActivity(intent);
        finish();
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickDelete(View view) {
        if (mList.getSelectedItem() == null) {
            return;
        }
        Patient selected = (Patient) mList.getSelectedItem();
        presenter.deletePatient(selected.getId());

    }
}
