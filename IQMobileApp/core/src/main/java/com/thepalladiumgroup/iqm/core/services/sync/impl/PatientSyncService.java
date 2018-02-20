package com.thepalladiumgroup.iqm.core.services.sync.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IPatientRepository;
import com.thepalladiumgroup.iqm.core.data.impl.PatientRepository;
import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientDTO;
import com.thepalladiumgroup.iqm.core.services.sync.IPatientSyncService;
import com.thepalladiumgroup.iqm.core.services.sync.endpoints.IPatientWapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public class PatientSyncService extends SyncService implements IPatientSyncService {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(PatientSyncService.class);

    //private Retrofit retrofit;
    private IPatientWapi patientWapi;
    private String errorResponse;
    private List<Patient> patients = new ArrayList<>();
    private IPatientRepository patientRepository;

    public PatientSyncService(IApplicationManager applicationManager) {
        super(applicationManager);
        patientRepository = new PatientRepository(applicationManager);
    }

    @Override
    public IPatientWapi getPatientWapi() {
        return this.patientWapi;
    }

    @Override
    public String getError() {
        return errorResponse;
    }

    @Override
    public void initialze() {
        super.initialze();
        if (patientWapi == null) {
            this.patientWapi = super.createService(IPatientWapi.class);
        }
    }

    @Override
    public List<Patient> readAllPatients() throws IOException {
        initialze();
        SLF_LOGGER.debug("sending PatientWapi GET request...");
        Call<List<Patient>> call = getPatientWapi().getPatients();
        patients = call.execute().body();
        SLF_LOGGER.debug("PatientWapi request executed state:" + call.isExecuted());
        return patients;
    }

    @Override
    public void sendPatient(Patient patient) throws IOException, SQLException {
        Patient patientToSend = patient;
        if (patientToSend == null) {
            return;
        }
        PatientDTO patientDTO = PatientDTO.create(patientToSend);

        initialze();
        SLF_LOGGER.debug("sending PatientWapi POST request...");
        Call<Patient> call = getPatientWapi().sendPatient(patientDTO);
        call.execute().body();
        SLF_LOGGER.debug("PatientWapi request executed state:" + call.isExecuted());
    }
}

