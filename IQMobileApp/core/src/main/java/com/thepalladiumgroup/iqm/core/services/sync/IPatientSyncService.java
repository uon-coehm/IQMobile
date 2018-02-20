package com.thepalladiumgroup.iqm.core.services.sync;

import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.services.sync.endpoints.IPatientWapi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public interface IPatientSyncService extends ISyncService {
    IPatientWapi getPatientWapi();
    List<Patient> readAllPatients() throws IOException;

    void sendPatient(Patient patient) throws IOException, SQLException;
}
