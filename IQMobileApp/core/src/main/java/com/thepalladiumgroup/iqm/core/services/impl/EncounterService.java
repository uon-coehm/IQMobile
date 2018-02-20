package com.thepalladiumgroup.iqm.core.services.impl;

import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IEncounterRepository;
import com.thepalladiumgroup.iqm.core.data.impl.EncounterRepository;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.RecordStats;
import com.thepalladiumgroup.iqm.core.services.IEncounterService;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public class EncounterService implements IEncounterService {
    private final IEncounterRepository encounterRepository;
    private final IApplicationManager applicationManager;


    public EncounterService(IApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.encounterRepository = new EncounterRepository(applicationManager);
    }

    @Override
    public Encounter saveNew(Encounter encounter) throws SQLException {
        return encounterRepository.save(encounter);
    }

    @Override
    public List<Encounter> getSummaryByPatient(int patientId, int encounterTypeId) throws SQLException {
        return encounterRepository.getSummaryByPatient(patientId, encounterTypeId);
    }

    @Override
    public List<Encounter> getByPatient(int patientId, int encounterTypeId) throws SQLException {
        return encounterRepository.getByPatient(patientId, encounterTypeId);
    }

    @Override
    public Encounter getById(int encounterId) throws SQLException {
        return encounterRepository.find(encounterId);
    }

    @Override
    public Encounter markAsComplete(Encounter encounter, boolean state) throws SQLException {
        encounter.setCompleted(state);
        encounterRepository.markCompletion(state, encounter.getId());
        return encounter;
    }

    @Override
    public RecordStats getRecordStats() throws SQLException {
        return encounterRepository.getRecordStats();
    }
}
