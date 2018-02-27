package com.thepalladiumgroup.iqm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.presentation.presenter.IFindAddPatientPresenter;
import com.thepalladiumgroup.iqm.presentation.presenter.impl.FindAddPatientPresenter;
import com.thepalladiumgroup.iqm.presentation.view.IFindAddPatientView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FindAddPatientActivity extends AppCompatActivity implements IFindAddPatientView {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(PatientListActivity.class);

    private IFindAddPatientPresenter presenter;
    private List<Patient> patients;
    private Patient patient;
    private PatientRecyclerAdapter patientRecyclerAdapter;
    private RecyclerView recyclerView;
    private ImageButton mDeletePatient;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_add_patient);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
        //NavUtils.navigateUpFromSameTask(this);
        //finish();

        //fix to show homepage after adding new client
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setPresenter(IFindAddPatientPresenter presenter) {
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
    public void initialize() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.patient_view_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_findadd));
        myToolbar.setLogo(R.mipmap.ic_launcher);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLF_LOGGER.debug("Open New Patient");
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.patient_list_view);

        presenter = new FindAddPatientPresenter(this, (IQMobileApplication) getApplication());
        presenter.loadPatientList();
    }

    @Override
    public void loadPatientHome() {
        SLF_LOGGER.debug("Open Patient Home");
        Intent intent = new Intent(this, PateintManagerActivity.class);
        intent.putExtra("patient_id", getSelectedPatient().getId());
        startActivity(intent);
    }

    @Override
    public void deletePatient() {

        new AlertDialog.Builder(this)
                .setTitle("Delete Patient")
                .setMessage("Confirm delete [" + getSelectedPatient().getFullName() + "] ?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.deletePatient(getSelectedPatient().getId());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private List<Patient> getCurrentListView() {
        return patients;
    }

    private void setCurrentListView(List<Patient> patients) {

        this.patients = patients;
        PatientRecyclerAdapter adapter = new PatientRecyclerAdapter(this, patients,

                new PatientRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Patient actviePatient) {
                        patient = actviePatient;
                        SLF_LOGGER.debug("<< SELECTED PATIENT " + actviePatient.toString() + " >>>");
                        loadPatientHome();

                    }
                },
                new PatientRecyclerAdapter.OnItemDeleteListener() {
                    @Override
                    public void onItemDelete(Patient actviePatient) {
                        patient = actviePatient;
                        SLF_LOGGER.debug("xxxxxxxxxxxxx DELETE PATIENT " + actviePatient.toString() + " XXXXXXXXX");
                        deletePatient();
                    }
                }

        );
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
    }


}

