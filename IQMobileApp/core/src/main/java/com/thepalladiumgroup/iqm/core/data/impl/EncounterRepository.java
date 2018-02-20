package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IEncounterRepository;
import com.thepalladiumgroup.iqm.core.data.IObservationRepository;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.Observation;
import com.thepalladiumgroup.iqm.core.model.RecordStats;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public class EncounterRepository extends BaseRepository<Encounter> implements IEncounterRepository {
    private IObservationRepository observationRepository;

    public EncounterRepository(IApplicationManager applicationManager) {
        super(applicationManager);
        observationRepository = new ObsevationRepository(applicationManager);
    }

    @Override
    public Encounter getPartnerEncounter(int partnerId, int encounterTypeId) throws SQLException {

        List<Integer> concepts = new ArrayList<>();

        //TODO : remove hard coded couple concecpts ids.
        concepts.add(37);

        List<Encounter> encounters = new ArrayList<>();
        encounters = getSummaryByPatient(partnerId, encounterTypeId);

        for (Encounter e : encounters) {

            List<Observation> obs = new ArrayList<>();
            obs = observationRepository.getByEncounterConcepts(e.getId(), concepts);
            for (Observation o : obs) {
                e.addObservation(o);
            }
        }


        if (encounters.size() > 0) {
            return encounters.get(0);
        }
        return null;
    }

    @Override
    public List<Encounter> getSummaryByPatient(int patientId, int encounterTypeId) throws SQLException {
        /*
        SELECT
encounter.id,
encounter.patient_id,
encounter.encounterType_id,
encounter.startdate,
encounter.starttime,
encounter.completed,
encounter.updatedate,
encounter.uuid
FROM
encounter

         */

        SLF_LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> with iterator >>>>>>>>>>>>>>>>>>>");

        List<Encounter> encounters = new ArrayList<>();

        List<String> cols = new ArrayList<>();

        cols.add("id");
//        cols.add("patient_id");
//        cols.add("encounterType_id");
        cols.add("startdate");
        cols.add("starttime");
        cols.add("completed");

        QueryBuilder<Encounter, Integer> qb = entityDao.queryBuilder()
                .selectColumns(cols);

        qb.where()
                .eq("patient_id", patientId).and()
                .eq("encounterType_id", encounterTypeId);

        CloseableIterator<Encounter> iterator = entityDao.iterator(qb.prepare());
        try {
            while (iterator.hasNext()) {
                Encounter account = iterator.next();
                encounters.add(account);
            }
        } finally {
            // close it at the end to close underlying SQL statement
            iterator.close();
        }
        return encounters;
    }

    @Override
    public List<Encounter> getByPatient(int patientId, int encounterTypeId) throws SQLException {
        List<Encounter> encounters = null;
        QueryBuilder<Encounter, Integer> qb = entityDao.queryBuilder();

        Where where = qb.where();

        where.eq("encountertype_id", encounterTypeId);

        where.and();

        where.eq("patient_id", patientId);

        encounters = qb.query();


        return encounters;
    }

    @Override
    public List<Encounter> getByPatient(int patientId) throws SQLException {
        List<Encounter> encounters = null;
        QueryBuilder<Encounter, Integer> qb = entityDao.queryBuilder();

        Where where = qb.where();


        where.eq("patient_id", patientId);

        encounters = qb.query();


        return encounters;
    }


    @Override
    public RecordStats getRecordStats() throws SQLException {
        GenericRawResults<String[]> rawResults = entityDao.queryRaw("select count(id) from encounter");
        List<String[]> results = rawResults.getResults();
        String[] resultArray = results.get(0);

        int recordCount = Integer.parseInt(resultArray[0]);

        GenericRawResults<String[]> rawResultsComplete = entityDao.queryRaw("select count(id) from encounter where completed=1");
        List<String[]> resultsComplete = rawResultsComplete.getResults();
        String[] resultArrayComplete = resultsComplete.get(0);

        int recordCountComplete = Integer.parseInt(resultArrayComplete[0]);

        GenericRawResults<String[]> rawResultsPending = entityDao.queryRaw("select count(id) from encounter where completed=0");
        List<String[]> resultsPending = rawResultsPending.getResults();
        String[] resultArrayPending = resultsPending.get(0);

        int recordCountPending = Integer.parseInt(resultArrayPending[0]);

        RecordStats recordStats = new RecordStats(recordCountComplete, recordCountPending, recordCount);

        return recordStats;
    }

    @Override
    public void markCompletion(boolean state, int id) throws SQLException {
        UpdateBuilder<Encounter, Integer> updateBuilder = entityDao.updateBuilder();
        updateBuilder.updateColumnValue("completed", state);
        updateBuilder.where().eq("id", id);
        updateBuilder.update();
    }
}
