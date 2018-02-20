package com.thepalladiumgroup.iqm.core.data;

import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.RecordStats;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public interface IEncounterRepository extends IRepository<Encounter> {

    Encounter getPartnerEncounter(int partnerId, int encounterTypeId) throws SQLException;

    List<Encounter> getSummaryByPatient(int patientId, int encounterTypeId) throws SQLException;

    List<Encounter> getByPatient(int patientId, int encounterTypeId) throws SQLException;

    List<Encounter> getByPatient(int patientId) throws SQLException;

    RecordStats getRecordStats() throws SQLException;

    void markCompletion(boolean state, int id) throws SQLException;


}
