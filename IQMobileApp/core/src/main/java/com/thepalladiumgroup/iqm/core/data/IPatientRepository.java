package com.thepalladiumgroup.iqm.core.data;

/**
 * Created by Koske Kimutai [2016/04/19]
 */

import com.thepalladiumgroup.iqm.core.model.Patient;
import com.thepalladiumgroup.iqm.core.model.PatientStats;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/16]
 */
public interface IPatientRepository extends IRepository<Patient> {
    Patient findByCode(String code) throws SQLException;
    Patient findWithEncounters(int id) throws SQLException;
    List<Patient> getAll(String search) throws SQLException;

    List<Patient> getAllDemographics() throws SQLException;

    Patient getDemographicsById(int id) throws SQLException;

    Patient getPatientPartnerInfo(int id) throws SQLException;

    Patient getPatientPartnerInfo(int id, int encounterTypeId) throws SQLException;

    Patient getToSend(int id) throws SQLException;

    List<Patient> getAllDemographicsById(int id) throws SQLException;

    List<Integer> getAllPatientIdToSend() throws SQLException;
    PatientStats getStats() throws SQLException;

    void deleteRecords(int patientId) throws SQLException;

    void cleanRecords() throws SQLException;
}
