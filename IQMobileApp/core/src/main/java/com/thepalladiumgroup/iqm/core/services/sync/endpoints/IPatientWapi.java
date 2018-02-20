package com.thepalladiumgroup.iqm.core.services.sync.endpoints;

import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Koske Kimutai [2016/05/12]
 */
public interface IPatientWapi {
    @GET("Patient")
    Call<List<Patient>> getPatients();
    @POST("Patient")
    Call<Patient> sendPatient(@Body PatientDTO patientDTO);
}
