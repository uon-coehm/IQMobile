package com.thepalladiumgroup.iqm.core.services;

import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientStats;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/16]
 */
public interface IPatientService {

    List<Patient> getAll() throws SQLException;

    List<Patient> getAllDemographics() throws SQLException;

    List<Integer> getAllCompletePateintIds() throws SQLException;
    List<Patient> getAllComplete(boolean completeonly) throws SQLException;

    List<Patient> find(String search) throws SQLException;

    Patient find(int id) throws SQLException;

    Patient findForEdit(int id) throws SQLException;

    Patient findDemographics(int id) throws SQLException;

    Patient findPatientPartnerInfo(int id) throws SQLException;

    Patient findPatientPartnerInfo(int id, int encounterTypeId) throws SQLException;

    Patient getToSend(int id) throws SQLException;

    Patient findByCode(String cilentcode) throws SQLException;

    PatientStats getStats() throws SQLException;


    boolean Save(Patient patient) throws SQLException;

    boolean update(Patient patient) throws SQLException;

    void updatePartnerRelations(Patient patient) throws SQLException;

    void Delete(int id) throws SQLException;

    void deletePatientRecords(int id) throws SQLException;

    void cleanUpPatients() throws SQLException;
}
