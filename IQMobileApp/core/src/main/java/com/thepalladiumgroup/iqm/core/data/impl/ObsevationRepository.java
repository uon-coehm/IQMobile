package com.thepalladiumgroup.iqm.core.data.impl;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.thepalladiumgroup.iqm.core.IApplicationManager;
import com.thepalladiumgroup.iqm.core.data.IObservationRepository;
import com.thepalladiumgroup.iqm.core.model.Encounter;
import com.thepalladiumgroup.iqm.core.model.MConcept;
import com.thepalladiumgroup.iqm.core.model.Observation;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Koske Kimutai [2016/04/27]
 */
public class ObsevationRepository extends BaseRepository<Observation> implements IObservationRepository {
    public ObsevationRepository(IApplicationManager applicationManager) {
        super(applicationManager);
    }


    @Override
    public Observation getByEncounterAndConcept(Encounter encounter, MConcept concept) throws SQLException {
        List<Observation> observations = null;
        QueryBuilder<Observation, Integer> qb = entityDao.queryBuilder();

        Where where = qb.where();

        where.eq("encounter", encounter);

        where.and();

        where.eq("concept", concept);

        observations = qb.query();

        if (observations.size() > 0) {
            return observations.get(0);
        }
        return null;
    }

    @Override
    public List<Observation> getByEncounterConcepts(int encounterId, List<Integer> conceptIds) throws SQLException {
        List<Observation> observations = null;
        QueryBuilder<Observation, Integer> qb = entityDao.queryBuilder();
        Where where = qb.where();
        where.eq("encounter", encounterId);
        where.and();
        where.in("concept", conceptIds);
        observations = qb.query();
        return observations;

    }

    @Override
    public List<Observation> getByEncounter(int id) throws SQLException {
        List<Observation> observations = null;
        QueryBuilder<Observation, Integer> qb = entityDao.queryBuilder();

        Where where = qb.where();

        where.eq("encounter", id);


        observations = qb.query();

        return observations;

    }

    @Override
    public void delete(List<Observation> observations) {

    }
}
