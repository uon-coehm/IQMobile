package com.thepalladiumgroup.iqm.core.services;

import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.RecordStats;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/26]
 */
public interface IEncounterService {
    List<Encounter> getSummaryByPatient(int patientId, int encounterTypeId) throws SQLException;

    List<Encounter> getByPatient(int patientId, int encounterTypeId) throws SQLException;
    Encounter getById(int encounterId) throws SQLException;

    Encounter saveNew(Encounter encounter) throws SQLException;
    Encounter markAsComplete(Encounter encounter, boolean state) throws SQLException;

    RecordStats getRecordStats() throws SQLException;
}
